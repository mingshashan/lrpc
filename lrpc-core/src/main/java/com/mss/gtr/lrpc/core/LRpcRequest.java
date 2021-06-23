package com.mss.gtr.lrpc.core;

/**
 * 封装RPC请求对象
 */
public class LRpcRequest {
    /**
     *
     */
    private String className;

    private String methodName;

    private Object[] parameters;

    private Class<?>[] parameterTypes;

    private String version;
}
