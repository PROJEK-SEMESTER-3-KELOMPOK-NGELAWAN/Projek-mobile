package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.net.Uri; // --- TAMBAHAN ---
import android.os.Bundle;
import android.text.SpannableString; // --- TAMBAHAN ---
import android.text.style.UnderlineSpan; // --- TAMBAHAN ---
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView; // --- TAMBAHAN ---
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
    private TextView forgotPasswordLink; // --- TAMBAHAN ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // ... (Kode status bar Anda tetap sama) ...
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            int nightModeFlags = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
            boolean isDarkMode = nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES;
            int flags = android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            if (!isDarkMode && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                flags |= android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }

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
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink); // --- TAMBAHAN ---
    }

    private void startIntroAnimation() {
        // ... (Kode animasi Anda tetap sama) ...
        if (loginContainer != null) {
            try {
                Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
                loginContainer.startAnimation(slideUp);
            } catch (Exception e) {
                Log.e(TAG, "Animation file not found: " + e.getMessage());
            }
        }
    }

    private void setupClickListeners() {
        if (loginButton != null) {
            // ... (Kode listener loginButton Anda tetap sama) ...
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
            // ... (Kode listener googleSignInButton Anda tetap sama) ...
            googleSignInButton.setOnClickListener(v -> {
                try {
                    GoogleAuthManager.startGoogleLogin(LoginActivity.this);
                } catch (Exception e) {
                    Log.e(TAG, "Google Sign-In error: " + e.getMessage());
                    Toast.makeText(this, "Google Sign-In tidak tersedia", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // --- BLOK TAMBAHAN UNTUK LUPA KATASANDI ---
        if (forgotPasswordLink != null) {

            // Opsional: Memberi garis bawah agar terlihat seperti link
            String text = forgotPasswordLink.getText().toString();
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new UnderlineSpan(), 0, text.length(), 0);
            forgotPasswordLink.setText(spannableString);

            // Tambahkan OnClickListener
            forgotPasswordLink.setOnClickListener(v -> {
                Log.d(TAG, "Link Lupa Katasandi diklik.");

                // --- URL DIBAWAH INI SUDAH DIGANTI SESUAI IP ANDA ---
                // Ini akan berfungsi jika HP Anda terhubung ke WiFi yang sama dengan komputer Anda.
                String url = "http://192.168.1.30/majelismdpl.com/user/lupa-password.php";

                try {
                    // Buat Intent untuk membuka browser
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "Tidak dapat membuka browser untuk link lupa katasandi", e);
                    Toast.makeText(this, "Tidak dapat membuka link", Toast.LENGTH_SHORT).show();
                }
            });
        }
        // --- AKHIR BLOK TAMBAHAN ---
    }

    // ... (Sisa metode Anda: validateInputs, clearFieldErrors, isValidCredentials, dll. tetap sama) ...

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
        setLoadingState(true);
        new android.os.Handler().postDelayed(() -> {
            if (isValidCredentials(username, password)) {
                handleLoginSuccess(username);
            } else {
                handleLoginFailure();
            }
            setLoadingState(false);
        }, 1000);
    }

    private void handleLoginSuccess(String username) {
        Log.d(TAG, "Login successful for user: " + username);
        SharedPrefManager.getInstance(this).saveLoginData(username, USER_ROLE);
        Toast.makeText(this, "Login berhasil! Selamat datang " + username, Toast.LENGTH_SHORT).show();
        navigateToMainActivity(username, USER_ROLE);
    }

    private void handleLoginFailure() {
        Log.w(TAG, "Login failed - Invalid credentials");
        usernameLayout.setError("Username atau password salah");
        passwordLayout.setError("Username atau password salah");
        Toast.makeText(this, "Username atau password salah!", Toast.LENGTH_LONG).show();
        if (passwordInput != null) {
            passwordInput.setText("");
        }
    }

    private void setLoadingState(boolean isLoading) {
        if (loginButton != null) {
            loginButton.setEnabled(!isLoading);
            // Saya perbaiki teksnya agar konsisten dengan XML Anda
            loginButton.setText(isLoading ? "Memproses..." : "MASUK");
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