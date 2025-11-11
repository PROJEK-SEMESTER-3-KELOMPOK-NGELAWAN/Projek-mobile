package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent; // <-- TAMBAHKAN IMPORT INI
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.majelismdpl.majelis_mdpl.R;

public class VerificationActivity extends AppCompatActivity {

    // Deklarasi semua komponen UI
    private ImageView ivBack;
    private Button btnConfirm;
    private TextView tvResendLink, tvTimer;
    private EditText etOtp1, etOtp2, etOtp3, etOtp4, etOtp5, etOtp6;
    private EditText[] otpFields;

    private CountDownTimer countDownTimer;

    // CONTOH: Tentukan kode OTP yang benar di sini.
    // Di aplikasi nyata, Anda akan membandingkan dengan kode dari server.
    private final String KODE_OTP_BENAR = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi);
        initViews();
        setClickListeners();
        setOtpInputListeners();
        startTimer();
    }

    private void resendCode() {
        // TODO: Tambahkan logika Anda di sini
        // Panggil API Anda untuk mengirim ulang kode OTP.
        Toast.makeText(this, "Mengirim ulang kode...", Toast.LENGTH_SHORT).show();

        // Mulai ulang timer
        startTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    /**
     * Menghubungkan variabel di Java dengan ID komponen di XML.
     */
    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        btnConfirm = findViewById(R.id.btn_confirm);
        tvResendLink = findViewById(R.id.tv_resend_link);
        tvTimer = findViewById(R.id.tv_timer);
        etOtp1 = findViewById(R.id.et_otp_1);
        etOtp2 = findViewById(R.id.et_otp_2);
        etOtp3 = findViewById(R.id.et_otp_3);
        etOtp4 = findViewById(R.id.et_otp_4);
        etOtp5 = findViewById(R.id.et_otp_5);
        etOtp6 = findViewById(R.id.et_otp_6);

        otpFields = new EditText[]{etOtp1, etOtp2, etOtp3, etOtp4, etOtp5, etOtp6};
    }

    /**
     * Mengatur semua listener untuk tombol-tombol.
     */
    private void setClickListeners() {
        ivBack.setOnClickListener(v -> {
            finish();
        });

        btnConfirm.setOnClickListener(v -> {
            verifyOtp();
        });

        tvResendLink.setOnClickListener(v -> {
            if (tvResendLink.isEnabled()) {
                resendCode();
            }
        });
    }

    /**
     * Mengatur logika input OTP (pindah fokus otomatis dan backspace).
     */
    private void setOtpInputListeners() {
        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;

            otpFields[index].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpFields.length - 1) {
                        otpFields[index + 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            otpFields[index].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (index > 0 && otpFields[index].getText().toString().isEmpty()) {
                        otpFields[index - 1].requestFocus();
                        return true;
                    }
                }
                return false;
            });
        }

        if (otpFields.length > 0) {
            otpFields[0].requestFocus();
        }
    }

    /**
     * Memulai timer hitung mundur 60 detik.
     */
    private void startTimer() {
        tvResendLink.setEnabled(false);
        tvResendLink.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(" (" + (millisUntilFinished / 1000) + "s)");
            }

            @Override
            public void onFinish() {
                tvTimer.setText("");
                tvResendLink.setEnabled(true);
                tvResendLink.setTextColor(Color.parseColor("#007BFF"));
            }
        }.start();
    }

    /**
     * ===================================================================
     * METODE INI TELAH DIUBAH SESUAI PERMINTAAN ANDA
     * ===================================================================
     * Mengambil OTP dari semua kotak dan memverifikasinya.
     */
    /**
     * Mengambil OTP dari semua kotak dan memverifikasinya.
     */
    private void verifyOtp() {
        StringBuilder otpBuilder = new StringBuilder();
        for (EditText field : otpFields) {
            otpBuilder.append(field.getText().toString().trim());
        }

        String otp = otpBuilder.toString();

        // Cek apakah OTP sudah diisi 6 digit
        if (otp.length() == 6) {

            // Bandingkan dengan kode yang benar
            if (otp.equals(KODE_OTP_BENAR)) {
                // KODE BENAR
                Toast.makeText(this, "Verifikasi berhasil!", Toast.LENGTH_SHORT).show();

                // Pindah ke LoginActivity
                Intent intent = new Intent(VerificationActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Tutup activity ini

            } else {
                // KODE SALAH (tapi 6 digit)
                Toast.makeText(this, "Kode OTP salah", Toast.LENGTH_SHORT).show();
            }

        } else {
            // KODE TIDAK LENGKAP (kurang dari 6 digit)
            // INI ADALAH BLOK YANG HILANG
            Toast.makeText(this, "Harap isi semua kolom OTP", Toast.LENGTH_SHORT).show();
        }
    }
    }