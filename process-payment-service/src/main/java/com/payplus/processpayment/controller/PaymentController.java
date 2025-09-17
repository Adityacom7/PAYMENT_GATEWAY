package com.payplus.processpayment.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {

    private final WebClient webClient;
    
    @Value("${service.authorization}")
    private String authorizationServiceUrl;
    
    @Value("${service.tokenization}")
    private String tokenizationServiceUrl;

    public PaymentController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @PostMapping("/process-payment")
    public Mono<ResponseEntity<PaymentResponse>> processPayment(@RequestBody PaymentRequest request) {
        
        // 1. First, call Authorization Service
        return webClient.post()
                .uri(authorizationServiceUrl + "/api/v1/authorize")
                .bodyValue(new AuthRequest(request.getMerchantId(), request.getApiKey(), request.getAmount(), request.getCurrency()))
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .flatMap(authResponse -> {
                    if (!"APPROVED".equals(authResponse.getStatus())) {
                        // Payment declined by auth service
                        return Mono.just(ResponseEntity.ok(
                            new PaymentResponse("DECLINED", "Authorization failed: " + authResponse.getStatus(), null)
                        ));
                    }
                    
                    // 2. If approved, call Tokenization Service
                    return webClient.post()
                            .uri(tokenizationServiceUrl + "/api/v1/tokenize")
                            .bodyValue(new TokenRequest(request.getCardNumber()))
                            .retrieve()
                            .bodyToMono(TokenResponse.class)
                            .map(tokenResponse -> ResponseEntity.ok(
                                new PaymentResponse("APPROVED", "Payment processed successfully", tokenResponse.getToken())
                            ));
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(500)
                    .body(new PaymentResponse("ERROR", "Service unavailable: " + e.getMessage(), null))));
    }

    // Request and Response Classes
    public static class PaymentRequest {
        private String merchantId;
        private String apiKey;
        private Double amount;
        private String currency;
        private String cardNumber;
        // getters and setters
        public String getMerchantId() { return merchantId; } public void setMerchantId(String merchantId) { this.merchantId = merchantId; }
        public String getApiKey() { return apiKey; } public void setApiKey(String apiKey) { this.apiKey = apiKey; }
        public Double getAmount() { return amount; } public void setAmount(Double amount) { this.amount = amount; }
        public String getCurrency() { return currency; } public void setCurrency(String currency) { this.currency = currency; }
        public String getCardNumber() { return cardNumber; } public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    }

    public static class PaymentResponse {
        private final String status;
        private final String message;
        private final String token;
        // constructor, getters
        public PaymentResponse(String status, String message, String token) {
            this.status = status;
            this.message = message;
            this.token = token;
        }
        public String getStatus() { return status; } public String getMessage() { return message; } public String getToken() { return token; }
    }

    // Classes for calling Authorization Service
    public static class AuthRequest {
        private final String merchantId;
        private final String apiKey;
        private final Double amount;
        private final String currency;
        // constructor, getters
        public AuthRequest(String merchantId, String apiKey, Double amount, String currency) {
            this.merchantId = merchantId; this.apiKey = apiKey; this.amount = amount; this.currency = currency;
        }
        public String getMerchantId() { return merchantId; } public String getApiKey() { return apiKey; } 
        public Double getAmount() { return amount; } public String getCurrency() { return currency; }
    }

    public static class AuthResponse {
        private String status;
        // getter, setter
        public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    }

    // Classes for calling Tokenization Service
    public static class TokenRequest {
        private String sensitiveData;
        // getter, setter
        public String getSensitiveData() { return sensitiveData; } public void setSensitiveData(String sensitiveData) { this.sensitiveData = sensitiveData; }
    }

    public static class TokenResponse {
        private String token;
        private String status;
        // getters, setters
        public String getToken() { return token; } public void setToken(String token) { this.token = token; }
        public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    }
}