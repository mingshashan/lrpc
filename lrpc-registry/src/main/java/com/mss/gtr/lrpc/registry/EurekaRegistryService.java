package com.mss.gtr.lrpc.registry;

import com.mss.gtr.lrpc.core.ServiceMeta;

public class EurekaRegistryService implements RegistryService {


    public EurekaRegistryService(String registryAddr) {
    }

    @Override
    public void registry(ServiceMeta serviceMeta) throws Exception {

    }

    @Override
    public void unRegistry(ServiceMeta serviceMeta) throws Exception {

    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception {
        return null;
    }

    @Override
    public void destroy() throws Exception {

    }
}
