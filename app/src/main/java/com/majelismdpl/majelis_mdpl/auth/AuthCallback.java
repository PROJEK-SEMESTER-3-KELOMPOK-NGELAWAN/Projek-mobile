package com.majelismdpl.majelis_mdpl.auth;

public interface AuthCallback {
    void onAuthSuccess(String username, String role, String message);
    void onAuthError(String errorMessage);
}
