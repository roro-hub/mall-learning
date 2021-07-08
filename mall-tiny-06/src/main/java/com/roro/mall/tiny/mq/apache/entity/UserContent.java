package com.roro.mall.tiny.mq.apache.entity;

import lombok.Data;

@Data
public class UserContent {

    private String username;
    private String pwd;

    public UserContent(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;
    }
}
