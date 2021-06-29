package com.mss.gtr.lrpc.facade.dto.response;

public class Response<T> {
    private final static String DEFAULT_MESSAGE_SUCCESS = "success";
    private final static String DEFAULT_MESSAGE_FAILURE = "failure";
    private T data;
    private String message;

    public Response(T data) {
        this(data, DEFAULT_MESSAGE_SUCCESS);
    }

    public Response(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public static <T> Response ok(T data) {
        return new Response(data);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
