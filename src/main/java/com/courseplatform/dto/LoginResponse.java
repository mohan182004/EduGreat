package com.courseplatform.dto;

public class LoginResponse {
    private String token;
    private String email;
    private long expiresIn;

    public LoginResponse(String token, String email, long expiresIn) {
        this.token = token;
        this.email = email;
        this.expiresIn = expiresIn;
    }

    public String getToken() { return token; }
    public String getEmail() { return email; }
    public long getExpiresIn() { return expiresIn; }
}
