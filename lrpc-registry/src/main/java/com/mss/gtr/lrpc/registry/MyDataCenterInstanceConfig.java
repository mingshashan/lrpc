package com.mss.gtr.lrpc.registry;

import com.google.inject.ProvidedBy;
import com.netflix.appinfo.DataCenterInfo;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.PropertiesInstanceConfig;
import com.netflix.appinfo.providers.MyDataCenterInstanceConfigProvider;

import javax.inject.Singleton;

@Singleton
@ProvidedBy(MyDataCenterInstanceConfigProvider.class)
public class MyDataCenterInstanceConfig extends PropertiesInstanceConfig implements EurekaInstanceConfig {

    public MyDataCenterInstanceConfig() {
    }

    public MyDataCenterInstanceConfig(String namespace) {
        super(namespace);
    }

    public MyDataCenterInstanceConfig(String namespace, DataCenterInfo dataCenterInfo) {
        super(namespace, dataCenterInfo);
    }

}
