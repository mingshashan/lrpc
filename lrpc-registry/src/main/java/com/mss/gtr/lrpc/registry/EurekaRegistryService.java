package com.mss.gtr.lrpc.registry;

import com.mss.gtr.lrpc.core.ServiceMeta;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class EurekaRegistryService implements RegistryService {

    private ApplicationInfoManager applicationInfoManager;
    private EurekaClient eurekaClient;
    private DynamicPropertyFactory configInstance;

    public EurekaRegistryService(String registryAddr) throws UnknownHostException {

        DynamicPropertyFactory configInstance = com.netflix.config.DynamicPropertyFactory.getInstance();
        ApplicationInfoManager applicationInfoManager = initializeApplicationInfoManager(new MyDataCenterInstanceConfig());
        EurekaClient eurekaClient = initializeEurekaClient(applicationInfoManager, new DefaultEurekaClientConfig());


    }


    private synchronized ApplicationInfoManager initializeApplicationInfoManager(EurekaInstanceConfig instanceConfig) {
        if (applicationInfoManager == null) {
            InstanceInfo instanceInfo = new EurekaConfigBasedInstanceInfoProvider(instanceConfig).get();
            applicationInfoManager = new ApplicationInfoManager(instanceConfig, instanceInfo);
        }

        return applicationInfoManager;
    }

    private synchronized EurekaClient initializeEurekaClient(ApplicationInfoManager applicationInfoManager, DefaultEurekaClientConfig clientConfig) {
        if (this.eurekaClient == null) {
            this.eurekaClient = new DiscoveryClient(applicationInfoManager, clientConfig);
        }

        return eurekaClient;
    }


    @Override
    public void registry(ServiceMeta serviceMeta) throws Exception {
        Map<String, String> appMetadata = new HashMap<>();
        appMetadata.put("serviceAddr", serviceMeta.getServiceAddr());
        appMetadata.put("serviceName", serviceMeta.getServiceName());
        appMetadata.put("serviceVersion", serviceMeta.getServiceVersion());
        appMetadata.put("servicePort", String.valueOf(serviceMeta.getServicePort()));
        applicationInfoManager.registerAppMetadata(appMetadata);

    }

    @Override
    public void unRegistry(ServiceMeta serviceMeta) throws Exception {
        eurekaClient.shutdown();
    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception {

        eurekaClient.getApplications();
        return null;
    }

    @Override
    public void destroy() throws Exception {

    }
}
