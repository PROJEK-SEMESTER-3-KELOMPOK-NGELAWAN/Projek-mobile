package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.api.ApiClient;
import com.majelismdpl.majelis_mdpl.api.ApiService;
import com.majelismdpl.majelis_mdpl.models.LoginResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    // Deklarasi Variabel UI
    private ImageView backButton;
    private TextInputLayout emailInputLayout;
    private TextInputEditText emailEditText;
    private TextView verificationCodeLabel;
    private LinearLayout otpInputLayout;
    private TextView resendCodeTimer;
    private Button verifyButton;

    // Deklarasi Variabel Logika
    private boolean isCodeSent = false;
    private CountDownTimer countDownTimer;
    private static final long RESEND_TIMEOUT = 60000; // 60 detik
    private List<EditText> otpEditTexts;
    private String userEmail;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        // Inisialisasi API Service
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // Handle Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.back_button), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Inisialisasi View
        initializeViews();

        // 2. Setup Tampilan Awal
        setupInitialUi();

        // 3. Setup Listeners
        setupListeners();

        // 4. Setup Auto-Advance OTP
        setupOtpAutoAdvance();
    }

    private void initializeViews() {
        // Mendapatkan referensi View dari layout
        backButton = findViewById(R.id.back_button);
        emailInputLayout = findViewById(R.id.email_input_layout);
        emailEditText = findViewById(R.id.email_edit_text);
        verificationCodeLabel = findViewById(R.id.verification_code_label);
        otpInputLayout = findViewById(R.id.otp_input_layout);
        resendCodeTimer = findViewById(R.id.resend_code_timer);
        verifyButton = findViewById(R.id.verify_button);

        // Mengumpulkan semua EditText OTP
        otpEditTexts = new ArrayList<>();
        otpEditTexts.add(findViewById(R.id.et_otp_1));
        otpEditTexts.add(findViewById(R.id.et_otp_2));
        otpEditTexts.add(findViewById(R.id.et_otp_3));
        otpEditTexts.add(findViewById(R.id.et_otp_4));
        otpEditTexts.add(findViewById(R.id.et_otp_5));
        otpEditTexts.add(findViewById(R.id.et_otp_6));
    }

    private void setupInitialUi() {
        // Sembunyikan elemen OTP di awal
        verificationCodeLabel.setVisibility(View.GONE);
        otpInputLayout.setVisibility(View.GONE);
        resendCodeTimer.setVisibility(View.GONE);
        verifyButton.setText("KIRIM KODE VERIFIKASI");
    }

    private void setupListeners() {
        // Logika Tombol Kembali
        backButton.setOnClickListener(v -> finish());

        // Logika Tombol Lanjutkan/Verifikasi
        verifyButton.setOnClickListener(v -> {
            if (!isCodeSent) {
                sendVerificationCode();
            } else {
                verifyCodeAndProceed();
            }
        });

        // Listener Timer Kirim Ulang
        resendCodeTimer.setOnClickListener(v -> {
            if (resendCodeTimer.isClickable()) {
                resendCode();
            }
        });
    }

    // =========================================================================
    // TAHAP 1: KIRIM KODE VERIFIKASI
    // =========================================================================

    private void sendVerificationCode() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            emailInputLayout.setError("Masukkan email yang valid.");
            return;
        }

        emailInputLayout.setError(null);
        verifyButton.setEnabled(false);
        Toast.makeText(this, "Mengirim kode ke " + email + "...", Toast.LENGTH_SHORT).show();

        // Panggil API untuk kirim OTP reset password
        Call<LoginResponse> call = apiService.sendResetPasswordOTP(email);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                verifyButton.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        userEmail = email;
                        Toast.makeText(ResetPasswordActivity.this,
                                apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        onCodeSentSuccess();
                    } else {
                        Toast.makeText(ResetPasswordActivity.this,
                                apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ResetPasswordActivity.this,
                            "Gagal mengirim kode verifikasi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                verifyButton.setEnabled(true);
                Toast.makeText(ResetPasswordActivity.this,
                        "Kesalahan koneksi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onCodeSentSuccess() {
        isCodeSent = true;
        verifyButton.setText("LANJUTKAN");

        // Tampilkan elemen OTP
        verificationCodeLabel.setVisibility(View.VISIBLE);
        otpInputLayout.setVisibility(View.VISIBLE);
        resendCodeTimer.setVisibility(View.VISIBLE);

        // Disable email input
        emailEditText.setEnabled(false);

        // Mulai Timer
        startResendTimer();

        // Pindahkan fokus ke OTP pertama
        otpEditTexts.get(0).requestFocus();
    }

    private void resendCode() {
        resendCodeTimer.setClickable(false);
        Toast.makeText(this, "Mengirim ulang kode...", Toast.LENGTH_SHORT).show();

        // Panggil API untuk kirim ulang OTP
        Call<LoginResponse> call = apiService.resendResetPasswordOTP(userEmail);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse apiResponse = response.body();
                    Toast.makeText(ResetPasswordActivity.this,
                            apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if (apiResponse.isSuccess()) {
                        startResendTimer();
                        // Clear OTP fields
                        for (EditText et : otpEditTexts) {
                            et.setText("");
                        }
                        otpEditTexts.get(0).requestFocus();
                    } else {
                        resendCodeTimer.setClickable(true);
                    }
                } else {
                    Toast.makeText(ResetPasswordActivity.this,
                            "Gagal mengirim ulang kode", Toast.LENGTH_SHORT).show();
                    resendCodeTimer.setClickable(true);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(ResetPasswordActivity.this,
                        "Kesalahan koneksi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                resendCodeTimer.setClickable(true);
            }
        });
    }

    private void startResendTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Atur agar timer tidak bisa diklik saat menghitung
        resendCodeTimer.setClickable(false);
        resendCodeTimer.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));

        countDownTimer = new CountDownTimer(RESEND_TIMEOUT, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                String time = String.format("Resend code in 00:%02d", seconds);
                resendCodeTimer.setText(time);
            }

            @Override
            public void onFinish() {
                resendCodeTimer.setText("Kirim Ulang Kode");
                // Aktifkan klik saat timer habis
                resendCodeTimer.setClickable(true);
                resendCodeTimer.setTextColor(ContextCompat.getColor(ResetPasswordActivity.this, R.color.dark_brown_icon));
            }
        }.start();
    }

    // =========================================================================
    // TAHAP 2: VERIFIKASI KODE OTP
    // =========================================================================

    private void verifyCodeAndProceed() {
        String otpCode = getOtpCode();

        if (otpCode.length() != 6) {
            Toast.makeText(this, "Masukkan 6 digit kode verifikasi", Toast.LENGTH_SHORT).show();
            return;
        }

        verifyButton.setEnabled(false);
        Toast.makeText(this, "Memverifikasi kode...", Toast.LENGTH_SHORT).show();

        // Panggil API untuk verifikasi OTP reset password
        Call<LoginResponse> call = apiService.verifyResetPasswordOTP(userEmail, otpCode);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                verifyButton.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        onVerificationSuccess();
                    } else {
                        Toast.makeText(ResetPasswordActivity.this,
                                apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ResetPasswordActivity.this,
                            "Gagal memverifikasi kode", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                verifyButton.setEnabled(true);
                Toast.makeText(ResetPasswordActivity.this,
                        "Kesalahan koneksi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onVerificationSuccess() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        Toast.makeText(this, "Verifikasi Sukses!", Toast.LENGTH_SHORT).show();

        // Arahkan ke ChangePasswordActivity dengan mode forgot password
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra(ChangePasswordActivity.EXTRA_PASSWORD_CHANGE_MODE,
                ChangePasswordActivity.MODE_FORGOT_PASSWORD);
        intent.putExtra("USER_EMAIL", userEmail);
        startActivity(intent);
        finish();
    }

    // =========================================================================
    // UTILITAS
    // =========================================================================

    private String getOtpCode() {
        StringBuilder sb = new StringBuilder();
        for (EditText et : otpEditTexts) {
            sb.append(et.getText().toString());
        }
        return sb.toString();
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void setupOtpAutoAdvance() {
        for (int i = 0; i < otpEditTexts.size(); i++) {
            final EditText currentEditText = otpEditTexts.get(i);
            final int currentIndex = i;

            currentEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1) {
                        if (currentIndex < otpEditTexts.size() - 1) {
                            otpEditTexts.get(currentIndex + 1).requestFocus();
                        } else {
                            currentEditText.clearFocus();
                        }
                    } else if (s.length() == 0 && before == 1) {
                        if (currentIndex > 0) {
                            otpEditTexts.get(currentIndex - 1).requestFocus();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
