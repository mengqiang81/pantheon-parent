package com.alibaba.mos.pantheon.rpc.api;

public class DuplicateParamNameException extends InvalidParamNameException {

    public DuplicateParamNameException(String message) {
        super(message);
    }
}
