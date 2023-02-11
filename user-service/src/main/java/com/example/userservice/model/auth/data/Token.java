package com.example.userservice.model.auth.data;

import lombok.Builder;
import lombok.Data;

@Data
public class Token {
    private String accessToken;
    private String refreshToken;

    @Builder
    public Token(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
