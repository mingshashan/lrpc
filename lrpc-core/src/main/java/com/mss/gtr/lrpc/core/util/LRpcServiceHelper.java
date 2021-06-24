package com.mss.gtr.lrpc.core.util;

public class LRpcServiceHelper {

    public static String buildServiceKey(String serviceName, String serviceVersion) {
        return String.join("#", serviceName, serviceVersion);
    }

}
