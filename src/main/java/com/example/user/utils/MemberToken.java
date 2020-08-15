package com.example.user.utils;

import org.apache.shiro.authc.UsernamePasswordToken;

public class MemberToken extends UsernamePasswordToken {
    private String type = "member";
    public MemberToken() {

    }
    public MemberToken(String username, String password) {
        super(username, password);
    }
    public MemberToken(String username, String password, String type) {
        super(username, password);
        this.type = type;
    }
}
