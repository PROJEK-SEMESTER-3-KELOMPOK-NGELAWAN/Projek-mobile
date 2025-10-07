package com.majelismdpl.majelis_mdpl.models;

public class GoogleAuthResponse {
    private boolean success;
    private String message;
    private String username;
    private String role;

    public GoogleAuthResponse(boolean success, String message, String username, String role) {
        this.success = success;
        this.message = message;
        this.username = username;
        this.role = role;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
}
