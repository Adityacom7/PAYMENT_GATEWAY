package com.payplus.authorization.dto;

public class AuthorizeResponse {
    private String authorizationId;
    private String status;
    private String message;
    private String timestamp;

    // getters & setters
    public String getAuthorizationId() { return authorizationId; }
    public void setAuthorizationId(String authorizationId) { this.authorizationId = authorizationId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
