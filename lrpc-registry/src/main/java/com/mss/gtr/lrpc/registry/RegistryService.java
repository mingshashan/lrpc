package com.mss.gtr.lrpc.registry;

import com.mss.gtr.lrpc.core.ServiceMeta;

public interface RegistryService {

    void registry(ServiceMeta serviceMeta) throws Exception;

    void unRegistry(ServiceMeta serviceMeta) throws Exception;

    ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception;

    void destroy() throws Exception;
}
