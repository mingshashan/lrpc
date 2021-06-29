package com.mss.gtr.lrpc.registry;

import com.mss.gtr.lrpc.core.LRpcException;

public class RegistryFactory {

    private static volatile RegistryService registryService;

    public static RegistryService getInstance(RegistryType registryType, String registryAddr) throws Exception {

        if (null != registryType) {
            synchronized (RegistryType.class) {
                if (null == registryService) {
                    switch (registryType) {
                        case EUREKA:
                            registryService = new EurekaRegistryService(registryAddr);
                            break;
                        case ZOOKEEPER:
                            registryService = new ZKRegistryService(registryAddr);
                            break;
                        default:
                            throw new LRpcException("registry type is error");
                    }
                }
            }
        }

        return registryService;
    }
}
