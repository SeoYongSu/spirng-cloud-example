package com.example.userservice.model.auth.data;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
