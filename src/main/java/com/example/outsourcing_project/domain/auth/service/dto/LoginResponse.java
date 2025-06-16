package com.example.outsourcing_project.domain.auth.service.dto;

import lombok.Getter;

@Getter
public class LoginResponse {

    private final String bearerToken;

    public LoginResponse(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}

