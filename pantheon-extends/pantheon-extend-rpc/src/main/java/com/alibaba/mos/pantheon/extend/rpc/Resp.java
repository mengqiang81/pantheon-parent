package com.alibaba.mos.pantheon.extend.rpc;

import lombok.Data;

@Data
public class Resp<T>{

    private String code;

    private String message;

    private T data;
}
