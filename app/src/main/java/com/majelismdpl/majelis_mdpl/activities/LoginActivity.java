package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.auth.GoogleAuthManager;
import com.majelismdpl.majelis_mdpl.utils.SharedPrefManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // Kredensial login yang valid
    private static final String VALID_USERNAME = "user";
    private static final String VALID_PASSWORD = "user123";
    private static final String USER_ROLE = "user";

    private TextInputEditText usernameInput, passwordInput;
    private TextInputLayout usernameLayout, passwordLayout;
    private MaterialButton loginButton, googleSignInButton;
    private CardView loginContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_login);
        
        // Set status bar sesuai dengan tema (light/dark mode)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            
            // Cek apakah sedang dalam dark mode
            int nightModeFlags = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
            boolean isDarkMode = nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES;
            
            int flags = android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                       android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            
            // Set light status bar hanya untuk light mode
            if (!isDarkMode && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                flags |= android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }

        // Cek apakah user sudah login

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            navigateToMainActivity();
            return;
        }

        initViews();
        setupClickListeners();
        startIntroAnimation();
    }

    private void initViews() {
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        usernameLayout = findViewById(R.id.usernameLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        loginButton = findViewById(R.id.loginButton);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        loginContainer = findViewById(R.id.loginContainer);
    }

    private void startIntroAnimation() {
        if (loginContainer != null) {
            try {
                Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
                loginContainer.startAnimation(slideUp);
            } catch (Exception e) {
                Log.e(TAG, "Animation file not found: " + e.getMessage());
                // Tidak perlu menampilkan toast error untuk animasi
            }
        }
    }

    private void setupClickListeners() {
        if (loginButton != null) {
            loginButton.setOnClickListener(v -> {
                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                clearFieldErrors();

                if (validateInputs(username, password)) {
                    performLogin(username, password);
                }
            });
        }

        if (googleSignInButton != null) {
            googleSignInButton.setOnClickListener(v -> {
                try {
                    GoogleAuthManager.startGoogleLogin(LoginActivity.this);
                } catch (Exception e) {
                    Log.e(TAG, "Google Sign-In error: " + e.getMessage());
                    Toast.makeText(this, "Google Sign-In tidak tersedia", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateInputs(String username, String password) {
        boolean isValid = true;

        if (username.isEmpty()) {
            usernameLayout.setError("Username tidak boleh kosong");
            isValid = false;
        }

        if (password.isEmpty()) {
            passwordLayout.setError("Password tidak boleh kosong");
            isValid = false;
        }

        return isValid;
    }

    private void clearFieldErrors() {
        if (usernameLayout != null) usernameLayout.setError(null);
        if (passwordLayout != null) passwordLayout.setError(null);
    }


    private boolean isValidCredentials(String username, String password) {
        return VALID_USERNAME.equals(username.trim()) && VALID_PASSWORD.equals(password.trim());
    }

    private void performLogin(String username, String password) {
        Log.d(TAG, "Attempting login for user: " + username);

        // Set loading state
        setLoadingState(true);

        // Simulasi delay untuk UX yang lebih baik
        new android.os.Handler().postDelayed(() -> {
            if (isValidCredentials(username, password)) {
                // Login berhasil
                handleLoginSuccess(username);
            } else {
                // Login gagal
                handleLoginFailure();
            }
            setLoadingState(false);
        }, 1000); // Delay 1 detik
    }

    private void handleLoginSuccess(String username) {
        Log.d(TAG, "Login successful for user: " + username);

        // Simpan data login ke SharedPreferences
        SharedPrefManager.getInstance(this).saveLoginData(username, USER_ROLE);

        // Tampilkan pesan sukses
        Toast.makeText(this, "Login berhasil! Selamat datang " + username, Toast.LENGTH_SHORT).show();

        // Navigate ke MainActivity
        navigateToMainActivity(username, USER_ROLE);
    }

    private void handleLoginFailure() {
        Log.w(TAG, "Login failed - Invalid credentials");

        // Tampilkan error pada field
        usernameLayout.setError("Username atau password salah");
        passwordLayout.setError("Username atau password salah");

        // Tampilkan toast error
        Toast.makeText(this, "Username atau password salah!", Toast.LENGTH_LONG).show();

        // Clear password field untuk keamanan
        if (passwordInput != null) {
            passwordInput.setText("");
        }
    }

    private void setLoadingState(boolean isLoading) {
        if (loginButton != null) {
            loginButton.setEnabled(!isLoading);
            loginButton.setText(isLoading ? "Memproses..." : "SIGN IN");
        }

        if (googleSignInButton != null) {
            googleSignInButton.setEnabled(!isLoading);
        }

        if (usernameInput != null) {
            usernameInput.setEnabled(!isLoading);
        }

        if (passwordInput != null) {
            passwordInput.setEnabled(!isLoading);
        }
    }


    private void navigateToMainActivity() {
        navigateToMainActivity(null, null);
    }

    private void navigateToMainActivity(String username, String role) {
        Intent intent = new Intent(this, MainActivity.class);

        if (username != null && role != null) {
            intent.putExtra("USERNAME", username);
            intent.putExtra("ROLE", role);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}