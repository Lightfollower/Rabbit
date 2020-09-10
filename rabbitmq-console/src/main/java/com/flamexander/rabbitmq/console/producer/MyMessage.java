package com.flamexander.rabbitmq.console.producer;

import java.io.Serializable;

public class MyMessage implements Serializable {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public MyMessage(String msg) {
        this.msg = msg;
    }
}
