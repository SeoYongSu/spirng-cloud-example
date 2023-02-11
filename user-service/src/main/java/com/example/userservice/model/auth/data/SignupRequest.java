package com.example.userservice.model.auth.data;

import lombok.Data;

@Data
public class SignupRequest {

    private String email;
    private String password;

}
