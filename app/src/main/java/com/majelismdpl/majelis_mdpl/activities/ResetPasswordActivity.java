package com.majelismdpl.majelis_mdpl.activities;

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
import androidx.core.content.ContextCompat; // Diperlukan untuk penanganan warna modern
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.majelismdpl.majelis_mdpl.R;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

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
        // Mendapatkan referensi View dari layout (activity_reset_password)
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

        // Listener Timer Kirim Ulang (Menggunakan lambda)
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

        // TODO: Ganti dengan panggilan API pengiriman kode yang sebenarnya
        Toast.makeText(this, "Mengirim kode ke " + email + "...", Toast.LENGTH_SHORT).show();

        // Simulasi sukses API
        onCodeSentSuccess();
    }

    private void onCodeSentSuccess() {
        isCodeSent = true;
        verifyButton.setText("LANJUTKAN");

        // Tampilkan elemen OTP
        verificationCodeLabel.setVisibility(View.VISIBLE);
        otpInputLayout.setVisibility(View.VISIBLE);
        resendCodeTimer.setVisibility(View.VISIBLE);

        // Mulai Timer
        startResendTimer();

        // Pindahkan fokus ke OTP pertama
        otpEditTexts.get(0).requestFocus();
    }

    private void resendCode() {
        // TODO: Ganti dengan panggilan API kirim ulang kode
        Toast.makeText(this, "Kode dikirim ulang...", Toast.LENGTH_SHORT).show();

        // Setelah sukses, mulai timer lagi
        startResendTimer();
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

        // TODO: Ganti dengan panggilan API verifikasi kode yang sebenarnya.

        // --- Simulasi Verifikasi ---
        if (otpCode.equals("123456")) { // Ganti dengan logika verifikasi server
            onVerificationSuccess();
        } else {
            Toast.makeText(this, "Kode verifikasi salah atau kadaluwarsa.", Toast.LENGTH_SHORT).show();
        }
    }

    private void onVerificationSuccess() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // TODO: Arahkan ke SetNewPasswordActivity (Contoh: Intent)
        Toast.makeText(this, "Verifikasi Sukses! Lanjutkan ke Set Password.", Toast.LENGTH_LONG).show();
        // Intent intent = new Intent(this, SetNewPasswordActivity.class);
        // startActivity(intent);
        // finish();
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
        // Menggunakan utilitas standar Android untuk validasi email
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Logika untuk memindahkan fokus antar EditText OTP secara otomatis
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
                        // Pindah fokus ke kotak berikutnya
                        if (currentIndex < otpEditTexts.size() - 1) {
                            otpEditTexts.get(currentIndex + 1).requestFocus();
                        } else {
                            // Semua OTP terisi, sembunyikan keyboard (opsional)
                            currentEditText.clearFocus();
                        }
                    } else if (s.length() == 0 && before == 1) {
                        // Ketika menghapus (backspace), pindah fokus ke kotak sebelumnya
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