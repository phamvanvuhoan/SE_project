package com.restaurant.pos.dto.common;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    private String message;
    private LocalDateTime timestamp;
    private List<String> details;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String message) {
        this();
        this.message = message;
    }

    public ErrorResponse(String message, List<String> details) {
        this();
        this.message = message;
        this.details = details;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details; }
}
