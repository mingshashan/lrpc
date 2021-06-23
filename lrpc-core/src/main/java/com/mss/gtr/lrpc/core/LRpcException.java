package com.mss.gtr.lrpc.core;

public class LRpcException extends RuntimeException {

    public LRpcException() {
    }

    public LRpcException(String message) {
        super(message);
    }

    public LRpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public LRpcException(Throwable cause) {
        super(cause);
    }

    public LRpcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
