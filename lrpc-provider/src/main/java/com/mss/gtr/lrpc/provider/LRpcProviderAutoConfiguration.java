package com.mss.gtr.lrpc.provider;

import com.mss.gtr.lrpc.registry.RegistryFactory;
import com.mss.gtr.lrpc.registry.RegistryService;
import com.mss.gtr.lrpc.registry.RegistryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LRpcProviderProperties.class)
public class LRpcProviderAutoConfiguration {

    @Autowired
    private LRpcProviderProperties lRpcProviderProperties;

    @Bean
    public LRpcProvider init() throws Exception {
        RegistryType registryType = RegistryType.valueOf(lRpcProviderProperties.getRegistryType());

        RegistryService registryService =
                RegistryFactory.getInstance(registryType,
                        lRpcProviderProperties.getRegistryAddr());


        return new LRpcProvider(lRpcProviderProperties.getPort(),
                registryService);
    }
}
