package com.chmelar.jozef.bcfiredroid.API.Model;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginResponse implements Serializable {
    private boolean success;
    private User user;
    private String token;

    public LoginResponse() {
    }
}