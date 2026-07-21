package com.amine.urticare.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MfaAuthenticationCache {

    private final Map<String, Authentication> cache = new ConcurrentHashMap<>();

    public void store(String transactionId, Authentication authentication) {
        cache.put(transactionId, authentication);
    }

    public Authentication get(String transactionId) {
        return cache.get(transactionId);
    }

    public void remove(String transactionId) {
        cache.remove(transactionId);
    }
}