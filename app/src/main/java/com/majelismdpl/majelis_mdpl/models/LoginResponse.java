package com.majelismdpl.majelis_mdpl.models;

import com.google.gson.annotations.SerializedName;

/**
 * ============================================
 * LoginResponse Model
 * Fungsi: Data response dari login-api.php
 * ============================================
 */
public class LoginResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("role")
    private String role;

    @SerializedName("username")
    private String username;

    @SerializedName("redirect_url")
    private String redirectUrl;

    @SerializedName("user")
    private User user;

    // Constructor
    public LoginResponse() {
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

    public String getUsername() {
        return username;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public User getUser() {
        // Jika user null, buat dari username & role
        if (user == null && username != null) {
            user = new User();
            user.setUsername(username);
            user.setRole(role);
        }
        return user;
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

    public void setUser(User user) {
        this.user = user;
    }
}
