package com.majelismdpl.majelis_mdpl.models;

import com.google.gson.annotations.SerializedName;

/**
 * ============================================
 * RegisterResponse Model
 * Response untuk Register & OTP Verification
 * ============================================
 */
public class RegisterResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("email")
    private String email;

    @SerializedName("username")
    private String username;

    @SerializedName("expires_at")
    private String expiresAt;

    // Constructor
    public RegisterResponse() {}

    public RegisterResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", expiresAt='" + expiresAt + '\'' +
                '}';
    }
}
