package com.majelismdpl.majelis_mdpl.models;

public class LoginResponse {
    private boolean success;
    private String message;
    private String role;

    // Default constructor (required for Gson)
    public LoginResponse() {}

    // Constructor with parameters
    public LoginResponse(boolean success, String message, String role) {
        this.success = success;
        this.message = message;
        this.role = role;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
