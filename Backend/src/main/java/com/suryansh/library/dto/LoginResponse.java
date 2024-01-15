package com.suryansh.library.dto;

public record LoginResponse(String username, String role, boolean isLogin, JwtResponse jwtResponse) {
    public record JwtResponse(String token, String expireAfter,String refreshToken) {
    }
}
