package com.mss.gtr.lrpc.core;

import java.io.Serializable;

/**
 * 封装RPC请求对象
 */
public class LRpcRequest implements Serializable {

    private static final long serialVersionUID = 2674257425171256133L;

    private String className;

    private String methodName;

    private Object[] parameters;

    private Class<?>[] parameterTypes;

    private String version;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
