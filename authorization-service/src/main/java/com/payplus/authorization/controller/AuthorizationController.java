package com.payplus.authorization.controller;

import com.payplus.authorization.dto.AuthorizeRequest;
import com.payplus.authorization.dto.AuthorizeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class AuthorizationController {

    // NOTE: In this lightweight replica we use a simple API-key match to decide approvals.
    // In production this would be replaced with DB lookups, fraud checks, and policy engines.

    private static final String VALID_API_KEY = "merchant-secret-key"; // dev-only

    @PostMapping("/authorize")
    public ResponseEntity<AuthorizeResponse> authorize(@RequestBody AuthorizeRequest request) {
        AuthorizeResponse resp = new AuthorizeResponse();
        resp.setAuthorizationId(UUID.randomUUID().toString());
        resp.setTimestamp(Instant.now().toString());

        if (VALID_API_KEY.equals(request.getApiKey()) && request.getAmount() != null && request.getAmount() > 0) {
            resp.setStatus("APPROVED");
            resp.setMessage("Authorization approved");
        } else {
            resp.setStatus("DECLINED");
            resp.setMessage("Invalid credentials or amount");
        }

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("UP");
    }

    @GetMapping("/ready")
    public ResponseEntity<String> ready() {
        // Lightweight readiness: in prod we would check DB/Kafka connectivity
        return ResponseEntity.ok("READY");
    }
}
