package com.chmelar.jozef.bcfiredroid.API.Model;

import lombok.Data;

@Data
public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
}