package com.mss.gtr.lrpc.consumer;

import com.mss.gtr.lrpc.consumer.annotation.RpcReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class LRpcConsumerPostProcessor implements ApplicationContextAware,
        BeanClassLoaderAware, BeanFactoryPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LRpcConsumerPostProcessor.class);

    private ApplicationContext applicationContext;

    private ClassLoader classLoader;

    private final Map<String, BeanDefinition> rpcReferenceBeanDefinitionMap =
            new LinkedHashMap<>();

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanNames) {
            BeanDefinition beanDefinition =
                    beanFactory.getBeanDefinition(beanDefinitionName);

            String beanClassName = beanDefinition.getBeanClassName();

            if (null != beanClassName) {
                Class<?> clazz = ClassUtils.resolveClassName(beanClassName, this.classLoader);
                ReflectionUtils.doWithFields(clazz, this::parseRpcReference);
            }

        }

        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;

        this.rpcReferenceBeanDefinitionMap.forEach((name, definition) -> {
            if (applicationContext.containsBean(name)) {
                LOGGER.error("spring context already have a bean named : [{}]", name);
                throw new IllegalArgumentException("spring context already have a bean named : [" + name + "]");
            }
            beanDefinitionRegistry.registerBeanDefinition(name, rpcReferenceBeanDefinitionMap.get(name));
            LOGGER.info("registered RpcReferenceBean {} success.", name);
        });

    }

    private void parseRpcReference(Field field) {
        RpcReference rpcReference = AnnotationUtils.getAnnotation(field, RpcReference.class);
        if (null != rpcReference) {
            BeanDefinitionBuilder builder =
                    BeanDefinitionBuilder.genericBeanDefinition(LRpcReferenceBean.class);

            builder.setInitMethodName("init");
            builder.addPropertyValue("interfaceClass", field.getType());
            builder.addPropertyValue("serviceVersion", rpcReference.serviceVersion());
            builder.addPropertyValue("registryType", rpcReference.registryType());
            builder.addPropertyValue("registryAddr", rpcReference.registryAddr());
            builder.addPropertyValue("timeout", rpcReference.timeout());

            BeanDefinition beanDefinition = builder.getBeanDefinition();
            rpcReferenceBeanDefinitionMap.put(field.getName(), beanDefinition);
        }

    }
}
