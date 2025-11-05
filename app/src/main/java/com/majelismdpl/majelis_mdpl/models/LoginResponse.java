package com.majelismdpl.majelis_mdpl.models;

// (BARU) Import model User
import com.majelismdpl.majelis_mdpl.models.User;
// (BARU) Import SerializedName
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("role")
    private String role;

    // (BARU) Tambahkan field data 'user'.
    // Pastikan nama "user" sama dengan key di JSON respons PHP Anda
    @SerializedName("user")
    private User user;

    // Constructor (biarkan apa adanya)
    public LoginResponse() {}

    // Getters (biarkan apa adanya)
    public boolean isSuccess() {
        return success;
    }
    public String getMessage() {
        return message;
    }
    public String getRole() {
        return role;
    }

    // (BARU) Tambahkan getter untuk User
    public User getUser() {
        return user;
    }

    // Setters (biarkan apa adanya)
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setRole(String role) {
        this.role = role;
    }

    // (BARU) Tambahkan setter untuk User
    public void setUser(User user) {
        this.user = user;
    }
}