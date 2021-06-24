package com.mss.gtr.lrpc.registry;

import com.mss.gtr.lrpc.core.ServiceMeta;
import com.mss.gtr.lrpc.core.annotation.RpcService;
import com.mss.gtr.lrpc.core.util.LRpcServiceHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * 在此处拉起RPC服务
 */
public class ServiceProvider implements InitializingBean, BeanPostProcessor {

    private String serverAddress;
    private final int port;
    private final RegistryService registryService;

    /**
     * 存储所有RPC服务
     */
    private final Map<String, Object> rpcServiceMap = new HashMap<>();

    public ServiceProvider(int port, RegistryService registryService) {
        this.port = port;
        this.registryService = registryService;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
        if (null != rpcService) {
            String serviceName = rpcService.serviceInterface().getName();
            String serviceVersion = rpcService.serviceVersion();

            ServiceMeta serviceMeta = new ServiceMeta();
            serviceMeta.setServiceName(serviceName);
            serviceMeta.setServiceVersion(serviceVersion);
            serviceMeta.setServiceAddr(serverAddress);
            serviceMeta.setServicePort(port);

            try {
                registryService.registry(serviceMeta);
                rpcServiceMap.put(LRpcServiceHelper.buildServiceKey(serviceName, serviceVersion),
                        bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bean;
    }
}
