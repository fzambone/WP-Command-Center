package com.wpcommandcenter.authentication.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wpcommandcenter.authentication.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AuthenticationService {

    @Value("${firebase.api-key}")
    private String firebaseApiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, AtomicInteger> attemptsByEmail = new ConcurrentHashMap<>();

    public AuthenticationService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String authenticateUser(String email, String password) throws AuthenticationException {
        if (isRateLimited(email)) {
            throw new AuthenticationException("Too many attempts. Please try again later.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}",
                email, password);

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + firebaseApiKey,
                    request,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("idToken").asText();
        } catch (HttpClientErrorException e) {
            incrementAttempts(email);
            throw new AuthenticationException("Invalid email or password");
        } catch (IOException e) {
            throw new AuthenticationException("Error processing authentication response");
        }
    }

    private boolean isRateLimited(String email) {
        AtomicInteger attempts = attemptsByEmail.computeIfAbsent(email, k -> new AtomicInteger(0));
        return attempts.get() >= 5; // Limit to 5 attempts
    }

    private void incrementAttempts(String email) {
        attemptsByEmail.computeIfAbsent(email, k -> new AtomicInteger(0)).incrementAndGet();
    }
}