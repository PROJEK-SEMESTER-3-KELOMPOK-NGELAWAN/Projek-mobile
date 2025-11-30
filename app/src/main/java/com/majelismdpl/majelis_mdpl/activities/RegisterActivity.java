package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.auth.GoogleAuthManager;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    // Deklarasi variabel untuk komponen UI
    private TextInputLayout usernameLayout, passwordLayout, confirmPasswordLayout;
    private TextInputEditText usernameInput, passwordInput, confirmPasswordInput;
    private MaterialButton registerButton, googleSignInButton;
    private TextView loginLinkText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setClickListeners();
    }

    private void initViews() {
        usernameLayout = findViewById(R.id.usernameLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

        registerButton = findViewById(R.id.registerButton);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        loginLinkText2 = findViewById(R.id.loginLinkText2);
    }

    private void setClickListeners() {
        registerButton.setOnClickListener(v -> handleRegistration());

        // ============ UPDATE: Google Sign In Button ============
        googleSignInButton.setOnClickListener(v -> signInWithGoogle());
        // =======================================================

        loginLinkText2.setOnClickListener(v -> goToLogin());
    }

    /**
     * Handle registrasi - hanya validasi username & password
     * Kemudian pindah ke VerificationActivity untuk input email
     */
    private void handleRegistration() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Validasi input
        if (!validateInput(username, password, confirmPassword)) {
            return;
        }

        // Langsung pindah ke VerificationActivity dengan membawa username & password
        Toast.makeText(this, "Silakan verifikasi email Anda", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        startActivity(intent);
    }

    private boolean validateInput(String username, String password, String confirmPassword) {
        usernameLayout.setError(null);
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);

        if (username.isEmpty()) {
            usernameLayout.setError("Username tidak boleh kosong");
            usernameInput.requestFocus();
            return false;
        }

        if (username.length() < 3) {
            usernameLayout.setError("Username minimal 3 karakter");
            usernameInput.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            passwordLayout.setError("Password tidak boleh kosong");
            passwordInput.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            passwordLayout.setError("Password minimal 6 karakter");
            passwordInput.requestFocus();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordLayout.setError("Konfirmasi password tidak boleh kosong");
            confirmPasswordInput.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordLayout.setError("Password tidak cocok");
            confirmPasswordInput.requestFocus();
            return false;
        }

        return true;
    }

    // ============ UPDATE: Implement Google Sign In ============
    private void signInWithGoogle() {
        Log.d(TAG, "🔵 Google Sign Up button clicked");
        try {
            // Panggil GoogleAuthManager untuk membuka Custom Tab dengan type=signup
            GoogleAuthManager.startGoogleSignup(RegisterActivity.this);
            Log.d(TAG, "✅ Custom Tab opened successfully for signup");
        } catch (Exception e) {
            Log.e(TAG, "❌ Error opening Google OAuth: " + e.getMessage());
            Toast.makeText(this, "Gagal membuka pendaftaran Google: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    // ==========================================================

    private void goToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
