package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.api.ApiClient;
import com.majelismdpl.majelis_mdpl.api.ApiService;
import com.majelismdpl.majelis_mdpl.models.LoginResponse;
import com.majelismdpl.majelis_mdpl.utils.SessionManager; // 1. Import SessionManager

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    public static final String EXTRA_PASSWORD_CHANGE_MODE = "password_change_mode";
    public static final int MODE_FORGOT_PASSWORD = 1;
    public static final int MODE_PROFILE_CHANGE = 2;

    private TextInputEditText inputNewPassword;
    private TextInputEditText inputConfirmPassword;
    private Button buttonSave;
    private TextView passwordLengthHint;
    private ImageButton backButton;

    private int currentMode;
    private String userEmail;
    private ApiService apiService;

    // 2. Tambahkan variabel SessionManager
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // 3. Inisialisasi SessionManager (Sama seperti di LoginActivity)
        sessionManager = SessionManager.getInstance(this);

        currentMode = getIntent().getIntExtra(EXTRA_PASSWORD_CHANGE_MODE, MODE_FORGOT_PASSWORD);
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        initializeViews();
        setupListeners();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        inputNewPassword = findViewById(R.id.input_new_password);
        inputConfirmPassword = findViewById(R.id.input_confirm_password);
        buttonSave = findViewById(R.id.button_save);
        passwordLengthHint = findViewById(R.id.password_length_hint);
        backButton = findViewById(R.id.back_button);

        passwordLengthHint.setTextColor(Color.BLACK);
    }

    private void setupListeners() {
        buttonSave.setOnClickListener(v -> handleChangePassword());
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void handleChangePassword() {
        String newPassword = inputNewPassword.getText().toString().trim();
        String confirmPassword = inputConfirmPassword.getText().toString().trim();

        if (validateInput(newPassword, confirmPassword)) {
            buttonSave.setEnabled(false);

            if (currentMode == MODE_FORGOT_PASSWORD) {
                saveNewPasswordForgot(userEmail, newPassword);
            } else if (currentMode == MODE_PROFILE_CHANGE) {
                // Untuk ganti password dari profil, biasanya juga minta logout agar login ulang
                saveNewPasswordForgot(userEmail, newPassword);
            }
        }
    }

    private void saveNewPasswordForgot(String email, String newPassword) {
        Call<LoginResponse> call = apiService.resetPassword(email, newPassword);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                buttonSave.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Toast.makeText(ChangePasswordActivity.this,
                                "Kata sandi berhasil diubah! Silakan login kembali.", Toast.LENGTH_LONG).show();

                        navigateToLogin();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this,
                                apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePasswordActivity.this,
                            "Gagal mengubah kata sandi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                buttonSave.setEnabled(true);
                Toast.makeText(ChangePasswordActivity.this,
                        "Kesalahan koneksi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput(String newPass, String confirmPass) {
        passwordLengthHint.setTextColor(Color.BLACK);
        passwordLengthHint.setText("Kata sandi harus minimal 6 karakter.");

        if (newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua kolom kata sandi.", Toast.LENGTH_SHORT).show();
            if (newPass.isEmpty()) inputNewPassword.setError("Wajib diisi");
            if (confirmPass.isEmpty()) inputConfirmPassword.setError("Wajib diisi");
            return false;
        }

        if (newPass.length() < 6) {
            passwordLengthHint.setTextColor(Color.RED);
            inputNewPassword.setError("Minimal 6 karakter");
            Toast.makeText(this, "Kata sandi minimal 6 karakter.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Konfirmasi kata sandi tidak cocok.", Toast.LENGTH_SHORT).show();
            inputConfirmPassword.setError("Kata sandi tidak cocok");
            return false;
        }

        return true;
    }

    private void navigateToLogin() {
        // 4. GUNAKAN SESSION MANAGER UNTUK LOGOUT
        // Ini akan otomatis menggunakan SharedPreferences yang SAMA dengan LoginActivity
        if (sessionManager != null) {
            sessionManager.logout();
        }

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        if (userEmail != null) {
            intent.putExtra("EMAIL_FOR_LOGIN", userEmail);
        }

        startActivity(intent);
        finish();
    }
}