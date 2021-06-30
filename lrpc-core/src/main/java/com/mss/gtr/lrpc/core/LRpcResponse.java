package com.mss.gtr.lrpc.core;

import java.io.Serializable;

/**
 * 封装响应对象
 */
public class LRpcResponse<T> implements Serializable {

    private static final long serialVersionUID = -3950323276101218131L;

    /**
     * 响应数据
     */
    private T data;
    /**
     * 响应码
     */
    private String code;
    /**
     * 响应消息
     */
    private String message;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
