package com.payplus.gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Random;

@RestController
@RequestMapping("/api/v1")
public class GatewayController {

    private final WebClient webClient;
    private final Random random = new Random();
    
    @Value("${providers.visa}")
    private String visaUrl;
    
    @Value("${providers.mastercard}")
    private String mastercardUrl;
    
    @Value("${providers.upi}")
    private String upiUrl;

    public GatewayController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @PostMapping("/process-transaction")
    public Mono<GatewayResponse> processTransaction(@RequestBody GatewayRequest request) {
        // Simulate network delay and processing time
        int delayMs = 100 + random.nextInt(400); // 100-500ms delay
        boolean shouldSucceed = random.nextDouble() < 0.95; // 95% success rate for demo
        
        return Mono.delay(java.time.Duration.ofMillis(delayMs))
                .then(Mono.fromCallable(() -> {
                    if (shouldSucceed) {
                        return new GatewayResponse("SUCCESS", "Transaction approved by " + request.getProvider(), "ref_" + System.currentTimeMillis());
                    } else {
                        return new GatewayResponse("FAILED", "Transaction declined by " + request.getProvider(), null);
                    }
                }));
    }

    // Request and Response Classes
    public static class GatewayRequest {
        private String provider; // "visa", "mastercard", "upi"
        private String tokenizedData;
        private Double amount;
        private String currency;
        // getters and setters
        public String getProvider() { return provider; } public void setProvider(String provider) { this.provider = provider; }
        public String getTokenizedData() { return tokenizedData; } public void setTokenizedData(String tokenizedData) { this.tokenizedData = tokenizedData; }
        public Double getAmount() { return amount; } public void setAmount(Double amount) { this.amount = amount; }
        public String getCurrency() { return currency; } public void setCurrency(String currency) { this.currency = currency; }
    }

    public static class GatewayResponse {
        private final String status;
        private final String message;
        private final String transactionReference;
        // constructor, getters
        public GatewayResponse(String status, String message, String transactionReference) {
            this.status = status;
            this.message = message;
            this.transactionReference = transactionReference;
        }
        public String getStatus() { return status; } public String getMessage() { return message; } public String getTransactionReference() { return transactionReference; }
    }
}
