package com.endpoint.kafkamessaging.auth;

public interface AuthService {
    void putAccessToken(String accessToken, Long userId);
    Long loginWithAccessToken(String code);
}

