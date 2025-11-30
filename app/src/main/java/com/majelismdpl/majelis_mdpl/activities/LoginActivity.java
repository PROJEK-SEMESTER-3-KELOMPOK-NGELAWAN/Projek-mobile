package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.api.ApiClient;
import com.majelismdpl.majelis_mdpl.api.ApiService;
import com.majelismdpl.majelis_mdpl.auth.GoogleAuthManager;
import com.majelismdpl.majelis_mdpl.database.UserManager;
import com.majelismdpl.majelis_mdpl.models.LoginResponse;
import com.majelismdpl.majelis_mdpl.utils.ApiConfig;
import com.majelismdpl.majelis_mdpl.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private MaterialButton loginButton;
    private MaterialButton googleButton;
    private TextView registerLinkText2;
    private MaterialButton forgotPasswordButton;

    private UserManager userManager;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApiConfig.initialize(getApplicationContext());
        Log.d(TAG, "API Config - Base URL: " + ApiConfig.getBaseUrl());
        Log.d(TAG, "API Config - Environment: " + ApiConfig.getEnvironment());

        userManager = new UserManager(this);
        sessionManager = SessionManager.getInstance(this);

        if (sessionManager.isLoggedIn()) {
            Log.d(TAG, "User already logged in, navigating to Main");
            navigateToMain();
            return;
        }

        setContentView(R.layout.activity_login);

        // Initialize views
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        googleButton = findViewById(R.id.googleSignInButton);
        registerLinkText2 = findViewById(R.id.registerLinkText2);
        forgotPasswordButton = findViewById(R.id.forgotPasswordLink);

        // Login button click
        loginButton.setOnClickListener(v -> handleLogin());

        // ============ GOOGLE SIGN IN BUTTON ============
        if (googleButton != null) {
            googleButton.setOnClickListener(v -> {
                Log.d(TAG, "🔵 Google Sign In button clicked");
                try {
                    // Panggil GoogleAuthManager untuk membuka Custom Tab
                    GoogleAuthManager.startGoogleLogin(LoginActivity.this);
                    Log.d(TAG, "✅ Custom Tab opened successfully");
                } catch (Exception e) {
                    Log.e(TAG, "❌ Error opening Google OAuth: " + e.getMessage());
                    Toast.makeText(this, "Gagal membuka login Google: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        // ==============================================

        // Register link click listener
        if (registerLinkText2 != null) {
            registerLinkText2.setOnClickListener(v -> navigateToRegister());
        }

        // Tombol Lupa Katasandi
        if (forgotPasswordButton != null) {
            forgotPasswordButton.setOnClickListener(v -> navigateToResetPassword());
        }
    }

    private void handleLogin() {
        String username = usernameInput.getText() != null ? usernameInput.getText().toString().trim() : "";
        String password = passwordInput.getText() != null ? passwordInput.getText().toString().trim() : "";

        Log.d(TAG, "Login attempt - Username: " + username);

        // Validation
        if (username.isEmpty()) {
            Toast.makeText(this, "Username wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Password wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading
        loginButton.setEnabled(false);
        loginButton.setText("Loading...");

        // Log API Info
        Log.d(TAG, "Base URL: " + ApiConfig.getBaseUrl());

        // Call API
        ApiService api = ApiClient.getApiService();
        Call<LoginResponse> call = api.login(username, password);

        // Log request URL
        Log.d(TAG, "Request URL: " + call.request().url());

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                // Log response
                Log.d(TAG, "Response Code: " + response.code());
                Log.d(TAG, "Response Message: " + response.message());

                // Reset button
                loginButton.setEnabled(true);
                loginButton.setText("MASUK");

                // Check error body
                if (!response.isSuccessful()) {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e(TAG, "Error Body: " + errorBody);
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }

                    Toast.makeText(LoginActivity.this,
                            "Login gagal: Server error (Code: " + response.code() + ")",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (response.body() == null) {
                    Log.e(TAG, "Response body is null!");
                    Toast.makeText(LoginActivity.this,
                            "Login gagal: Response kosong",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                LoginResponse body = response.body();

                // Log response detail
                Log.d(TAG, "Login Success: " + body.isSuccess());
                Log.d(TAG, "Login Message: " + body.getMessage());
                Log.d(TAG, "User: " + (body.getUser() != null ? body.getUser().toString() : "null"));

                if (body.isSuccess()) {
                    Log.d(TAG, "Login berhasil, saving to SQLite...");

                    // Save to SQLite
                    sessionManager.saveLoginResponse(body);

                    Log.d(TAG, "Data saved, navigating to Main...");

                    Toast.makeText(LoginActivity.this,
                            body.getMessage(),
                            Toast.LENGTH_SHORT).show();

                    navigateToMain();
                } else {
                    String msg = body.getMessage() != null ? body.getMessage() : "Login gagal";
                    Log.e(TAG, "Login failed: " + msg);
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "API Call Failed!", t);
                Log.e(TAG, "Error Message: " + t.getMessage());
                Log.e(TAG, "Error Class: " + t.getClass().getName());

                // Reset button
                loginButton.setEnabled(true);
                loginButton.setText("MASUK");

                Toast.makeText(LoginActivity.this,
                        "Gagal terhubung ke server: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToMain() {
        Log.d(TAG, "Navigating to MainActivity...");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToRegister() {
        Log.d(TAG, "Navigating to RegisterActivity...");
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void navigateToResetPassword() {
        Log.d(TAG, "Navigating to ResetPasswordActivity...");
        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }
}
