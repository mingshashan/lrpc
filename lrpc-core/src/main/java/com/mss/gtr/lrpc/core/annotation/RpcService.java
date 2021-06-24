package com.mss.gtr.lrpc.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 暴露RPC服务
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcService {

    /**
     * interface
     *
     * @return
     */
    Class serviceInterface();

    String serviceVersion();
}
