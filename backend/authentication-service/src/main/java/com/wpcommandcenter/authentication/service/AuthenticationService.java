package com.wpcommandcenter.authentication.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.wpcommandcenter.authentication.controller.AuthenticationController;
import com.wpcommandcenter.authentication.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AuthenticationService {

    @Value("${firebase.api-key}")
    private String firebaseApiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, AtomicInteger> attemptsByEmail = new ConcurrentHashMap<>();
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> authenticateUser(String email, String password) throws AuthenticationException {
        logger.info("Authenticating user: " + email);

        if (isRateLimited(email)) {
            logger.warn("Rate limit exceeded for user: {}", email);
            throw new AuthenticationException("Too many attempts. Please try again later.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}",
                email, password);

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        try {
            logger.debug("Sending authentication request to Firebase");
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + firebaseApiKey,
                    request,
                    String.class
            );

            logger.debug("Received response from Firebase");
            JsonNode root = objectMapper.readTree(response.getBody());
            String idToken = root.path("idToken").asText();

            logger.debug("Verifying Firebase ID token");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

            Map<String, Object> claims = new HashMap<>(decodedToken.getClaims());
            claims.put("email", email);

            Object rolesObj = claims.get("roles");
            if (rolesObj != null) {
                if (rolesObj instanceof String) {
                    claims.put("roles", Collections.singletonList((String) rolesObj));
                } else if (rolesObj instanceof List) {
                    claims.put("roles", rolesObj);
                }
            } else {
                claims.put("roles", Collections.emptyList());
            }

            logger.info("User authenticated successfully: {}", email);
            return claims;
        } catch (HttpClientErrorException e) {
            logger.warn("Authentication failed for user: {}", email, e);
            incrementAttempts(email);
            throw new AuthenticationException("Invalid email or password");
        } catch (IOException | FirebaseException e) {
            logger.error("Error processing authentication response", e);
            throw new AuthenticationException("Error processing authentication response");
        }
    }

    private boolean isRateLimited(String email) {
        AtomicInteger attempts = attemptsByEmail.computeIfAbsent(email, k -> new AtomicInteger(0));
        return attempts.get() >= 5 && System.currentTimeMillis() - attempts.get() < 3600000;
    }

    private void incrementAttempts(String email) {
        attemptsByEmail.compute(email, (k, v) -> {
            if (v == null) {
                AtomicInteger ai = new AtomicInteger();
                ai.set((int) System.currentTimeMillis());
                return ai;
            }
            v.incrementAndGet();
            return v;
        });
    }

    @Scheduled(fixedRate = 360000) // 1 hour
    public void cleanUpAttempts() {
        long currentTime = System.currentTimeMillis();
        attemptsByEmail.entrySet().removeIf(entry -> currentTime - entry.getValue().get() > 3600000);
    }
}