package com.wpcommandcenter.authentication.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wpcommandcenter.authentication.exception.RegistrationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class RegistrationService {

    @Value("${firebase.api-key}")
    private String firebaseApiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RegistrationService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String registerUser(String email, String password) throws RegistrationException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}",
                email, password);

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + firebaseApiKey,
                    request,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("idToken").asText();
        } catch (HttpClientErrorException e) {
            try {
                JsonNode errorBody = objectMapper.readTree(e.getResponseBodyAsString());
                String errorMessage = errorBody.path("error").path("message").asText();
                throw new RegistrationException("User registration failed: " + errorMessage);
            } catch (IOException ioException) {
                throw new RegistrationException("Error processing user registration response");
            }
        } catch (IOException e) {
            throw new RegistrationException("Error processing user registration response");
        }
    }
}
