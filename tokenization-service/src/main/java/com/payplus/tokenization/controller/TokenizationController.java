package com.payplus.tokenization.controller;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class TokenizationController {

    // In-memory store for demo purposes. Will be replaced with a database.
    private final Map<String, String> tokenMap = new HashMap<>();
    private final Map<String, String> reverseTokenMap = new HashMap<>();

    @PostMapping("/tokenize")
    public TokenizationResponse tokenize(@RequestBody TokenizationRequest request) {
        // In a real scenario, validate the data format here
        String sensitiveData = request.getSensitiveData();

        // Check if this data already has a token
        String existingToken = reverseTokenMap.get(sensitiveData);
        if (existingToken != null) {
            return new TokenizationResponse(existingToken, "DATA_ALREADY_TOKENIZED");
        }

        // Generate a new unique token
        String token = "tok_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        tokenMap.put(token, sensitiveData);
        reverseTokenMap.put(sensitiveData, token);

        return new TokenizationResponse(token, "TOKEN_CREATED");
    }

    @PostMapping("/detokenize")
    public DetokenizationResponse detokenize(@RequestBody DetokenizationRequest request) {
        String token = request.getToken();
        String sensitiveData = tokenMap.get(token);

        if (sensitiveData == null) {
            return new DetokenizationResponse(null, "TOKEN_NOT_FOUND");
        }

        return new DetokenizationResponse(sensitiveData, "DATA_RETRIEVED");
    }

    // Request and Response Classes
    public static class TokenizationRequest {
        private String sensitiveData;
        // getter and setter
        public String getSensitiveData() { return sensitiveData; }
        public void setSensitiveData(String sensitiveData) { this.sensitiveData = sensitiveData; }
    }

    public static class TokenizationResponse {
        private final String token;
        private final String status;
        // constructor, getters
        public TokenizationResponse(String token, String status) {
            this.token = token;
            this.status = status;
        }
        public String getToken() { return token; }
        public String getStatus() { return status; }
    }

    public static class DetokenizationRequest {
        private String token;
        // getter and setter
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }

    public static class DetokenizationResponse {
        private final String sensitiveData;
        private final String status;
        // constructor, getters
        public DetokenizationResponse(String sensitiveData, String status) {
            this.sensitiveData = sensitiveData;
            this.status = status;
        }
        public String getSensitiveData() { return sensitiveData; }
        public String getStatus() { return status; }
    }
}