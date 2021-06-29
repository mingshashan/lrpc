package com.mss.gtr.lrpc.registry;

public enum RegistryType {
    ZOOKEEPER(0),
    EUREKA(1);
    private int type;

    RegistryType(int type) {
        this.type = type;
    }


}
