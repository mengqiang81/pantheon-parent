package com.alibaba.mos.pantheon.extend.rpc.exception;

public abstract class RpcException extends RuntimeException {

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
