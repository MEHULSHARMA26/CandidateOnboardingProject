package com.example.candidateonboardingsystem.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }
}

