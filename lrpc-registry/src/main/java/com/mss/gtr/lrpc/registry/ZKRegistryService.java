package com.mss.gtr.lrpc.registry;

import com.mss.gtr.lrpc.core.ServiceMeta;
import com.mss.gtr.lrpc.core.util.LRpcServiceHelper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;

public class ZKRegistryService implements RegistryService {

    public static final int BASE_SLEEP_TIME_MS = 1000;
    public static final int MAX_RETRIES = 3;
    public static final String ZK_BASE_PATH = "/l_rpc";

    private final ServiceDiscovery<ServiceMeta> serviceDiscovery;

    public ZKRegistryService(String registryAddr) throws Exception {

        CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddr, new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
        client.start();
        JsonInstanceSerializer<ServiceMeta> serializer =
                new JsonInstanceSerializer<>(ServiceMeta.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMeta.class)
                .client(client)
                .serializer(serializer)
                .basePath(ZK_BASE_PATH)
                .build();
        this.serviceDiscovery.start();
    }

    @Override
    public void registry(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance =
                ServiceInstance.<ServiceMeta>
                        builder()
                        .name(LRpcServiceHelper.buildServiceKey(serviceMeta.getServiceName(),
                                serviceMeta.getServiceVersion()))
                        .address(serviceMeta.getServiceAddr())
                        .port(serviceMeta.getServicePort())
                        .payload(serviceMeta)
                        .build();
        serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public void unRegistry(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance =
                ServiceInstance.<ServiceMeta>
                        builder()
                        .name(LRpcServiceHelper.buildServiceKey(serviceMeta.getServiceName(),
                                serviceMeta.getServiceVersion()))
                        .address(serviceMeta.getServiceAddr())
                        .port(serviceMeta.getServicePort())
                        .payload(serviceMeta)
                        .build();
        serviceDiscovery.unregisterService(serviceInstance);
    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception {

        Collection<ServiceInstance<ServiceMeta>> serviceInstances =
                serviceDiscovery.queryForInstances(serviceName);

        ServiceInstance<ServiceMeta> = new ZkConstan
        return null;
    }

    @Override
    public void destroy() throws Exception {
        serviceDiscovery.close();
    }
}
