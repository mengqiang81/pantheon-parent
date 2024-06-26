package com.alibaba.mos.pantheon.rpc.api;

public class DuplicateMethodNameException extends RpcException {

    public DuplicateMethodNameException(String message) {
        super(message);
    }
}
