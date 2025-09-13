package com.studentspring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AuthResponse {
    @JsonProperty("token")
    private String token;
    public AuthResponse(String token) { this.token = token; }
}