package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

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

    // Welcome Screen Elements
    private ImageView logoImage;
    private TextView appTagline;
    private TextView welcomeBackText;
    private TextView welcomeSubtitle;
    private MaterialButton signInButton; // Tombol "MASUK" pertama (Welcome)

    // Login Form Elements
    private CardView loginContainer;
    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private MaterialButton loginButton; // Tombol "MASUK" kedua (Form)
    private MaterialButton googleButton;
    private TextView registerLinkText2;
    private MaterialButton forgotPasswordButton;
    private ConstraintLayout mainLayout;

    private UserManager userManager;
    private SessionManager sessionManager;

    // Status untuk transisi
    private boolean isLoginViewVisible = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Atur status bar transparan
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        ApiConfig.initialize(getApplicationContext());
        userManager = new UserManager(this);
        sessionManager = SessionManager.getInstance(this);

        if (sessionManager.isLoggedIn()) {
            Log.d(TAG, "User already logged in, navigating to Main");
            navigateToMain();
            return;
        }

        setContentView(R.layout.activity_login);

        // Memastikan keyboard tidak menutupi form login saat muncul
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initViews();
        initAnimations(); // Animasi Welcome Screen (Awal Masuk)
        setupListeners();
    }

    // --- INISIALISASI VIEWS ---
    private void initViews() {
        // Welcome Elements
        logoImage = findViewById(R.id.mdplLogo);
        appTagline = findViewById(R.id.appTagline);
        welcomeBackText = findViewById(R.id.welcomeBackText);
        welcomeSubtitle = findViewById(R.id.welcomeSubtitle);
        signInButton = findViewById(R.id.signInButton);
        mainLayout = findViewById(R.id.main);

        // Login Form Elements
        loginContainer = findViewById(R.id.loginContainer);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        googleButton = findViewById(R.id.googleSignInButton);
        registerLinkText2 = findViewById(R.id.registerLinkText2);
        forgotPasswordButton = findViewById(R.id.forgotPasswordLink);

        // Default: Sembunyikan form login (Invisible agar posisi terhitung)
        loginContainer.setVisibility(View.INVISIBLE);
    }

    // --- SETUP LISTENERS ---
    private void setupListeners() {
        // Tombol MASUK di layar Welcome -> Pindahkan ke tampilan Login
        signInButton.setOnClickListener(v -> animateToLoginView());

        // Login button click (di dalam CardView)
        loginButton.setOnClickListener(v -> handleLogin());

        // Google Sign In button click
        if (googleButton != null) {
            googleButton.setOnClickListener(v -> {
                Log.d(TAG, "🔵 Google Sign In button clicked");
                try {
                    GoogleAuthManager.startGoogleLogin(LoginActivity.this);
                } catch (Exception e) {
                    Log.e(TAG, "❌ Error opening Google OAuth: " + e.getMessage());
                    Toast.makeText(this, "Gagal membuka login Google", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Register link click listener
        if (registerLinkText2 != null) {
            registerLinkText2.setOnClickListener(v -> navigateToRegister());
        }

        // Tombol Lupa Katasandi
        if (forgotPasswordButton != null) {
            forgotPasswordButton.setOnClickListener(v -> navigateToResetPassword());
        }
    }

    // --- ANIMASI AWAL (SAAT APLIKASI DIBUKA) ---
    private void initAnimations() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(800);

        // Logo tetap dianimasikan saat awal aplikasi dibuka
        logoImage.startAnimation(fadeIn);

        Animation fadeInTagline = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeInTagline.setDuration(800);
        fadeInTagline.setStartOffset(200);
        appTagline.startAnimation(fadeInTagline);

        Animation slideUp1 = AnimationUtils.loadAnimation(this, R.anim.slide_up_fade_in);
        slideUp1.setStartOffset(400);
        welcomeBackText.startAnimation(slideUp1);

        Animation slideUp2 = AnimationUtils.loadAnimation(this, R.anim.slide_up_fade_in);
        slideUp2.setStartOffset(500);
        welcomeSubtitle.startAnimation(slideUp2);

        Animation slideUp3 = AnimationUtils.loadAnimation(this, R.anim.slide_up_fade_in);
        slideUp3.setStartOffset(600);
        signInButton.startAnimation(slideUp3);
    }

    // --- LOGIKA TRANSISI (YANG DIPERBAIKI) ---
    private void animateToLoginView() {
        if (isLoginViewVisible) return;
        isLoginViewVisible = true;

        // 1. Animasi Fade Out untuk elemen Welcome (KECUALI LOGO)
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fadeOut.setDuration(400);

        // CATATAN: logoImage TIDAK diikutkan dalam animasi fadeOut ini.
        // logoImage.startAnimation(fadeOut); <--- Dihapus agar logo tetap muncul

        appTagline.startAnimation(fadeOut);
        welcomeBackText.startAnimation(fadeOut);
        welcomeSubtitle.startAnimation(fadeOut);
        signInButton.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                // CATATAN: logoImage TIDAK disembunyikan.
                // logoImage.setVisibility(View.INVISIBLE); <--- Dihapus

                // Elemen lain disembunyikan (Invisible menjaga layout tidak jumping)
                appTagline.setVisibility(View.INVISIBLE);
                welcomeBackText.setVisibility(View.INVISIBLE);
                welcomeSubtitle.setVisibility(View.INVISIBLE);
                signInButton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        // 2. Animasi Memunculkan Login Form
        loginContainer.setVisibility(View.VISIBLE);

        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_in_full);
        slideUp.setDuration(600);
        slideUp.setStartOffset(200); // Delay singkat agar smooth

        loginContainer.startAnimation(slideUp);
    }

    // --- LOGIKA LOGIN API ---
    private void handleLogin() {
        String username = usernameInput.getText() != null ? usernameInput.getText().toString().trim() : "";
        String password = passwordInput.getText() != null ? passwordInput.getText().toString().trim() : "";

        if (username.isEmpty()) {
            Toast.makeText(this, "Username wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Password wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Loading...");

        ApiService api = ApiClient.getApiService();
        Call<LoginResponse> call = api.login(username, password);
        Log.d(TAG, "Request URL: " + call.request().url());

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loginButton.setEnabled(true);
                loginButton.setText("MASUK");

                if (!response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,
                            "Login gagal: Server error (Code: " + response.code() + ")",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                LoginResponse body = response.body();
                if (body != null && body.isSuccess()) {
                    sessionManager.saveLoginResponse(body);
                    Toast.makeText(LoginActivity.this, body.getMessage(), Toast.LENGTH_SHORT).show();
                    navigateToMain();
                } else {
                    String msg = (body != null && body.getMessage() != null) ? body.getMessage() : "Login gagal";
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "API Call Failed!", t);
                loginButton.setEnabled(true);
                loginButton.setText("MASUK");
                Toast.makeText(LoginActivity.this, "Gagal terhubung ke server", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void navigateToResetPassword() {
        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }
}