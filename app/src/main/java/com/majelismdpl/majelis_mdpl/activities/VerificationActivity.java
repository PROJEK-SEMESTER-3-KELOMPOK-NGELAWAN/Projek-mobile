package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.api.ApiClient;
import com.majelismdpl.majelis_mdpl.api.ApiService;
import com.majelismdpl.majelis_mdpl.models.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationActivity extends AppCompatActivity {

    private static final String TAG = "VerificationActivity";

    // UI Components
    private ImageView ivBack;
    private TextInputLayout emailLayout;
    private TextInputEditText emailInput;
    private MaterialButton btnSendOtp, btnConfirm;
    private TextView tvResendLink, tvTimer;
    private EditText etOtp1, etOtp2, etOtp3, etOtp4, etOtp5, etOtp6;
    private EditText[] otpFields;

    // Data dari RegisterActivity
    private String username;
    private String password;

    // Data yang diinput user
    private String userEmail;

    private CountDownTimer countDownTimer;
    private boolean otpSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi);

        // Ambil username dan password dari Intent
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");

        if (username == null || password == null) {
            Toast.makeText(this, "Data registrasi tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setClickListeners();
        setOtpInputListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        emailLayout = findViewById(R.id.emailLayout);
        emailInput = findViewById(R.id.emailInput);
        btnSendOtp = findViewById(R.id.btn_send_otp);
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

        // Disable OTP fields dan tombol konfirmasi sampai OTP dikirim
        setOtpFieldsEnabled(false);
        btnConfirm.setEnabled(false);
    }

    private void setClickListeners() {
        ivBack.setOnClickListener(v -> finish());

        btnSendOtp.setOnClickListener(v -> sendOtpRequest());

        btnConfirm.setOnClickListener(v -> verifyOtp());

        tvResendLink.setOnClickListener(v -> {
            if (tvResendLink.isEnabled()) {
                resendOtp();
            }
        });
    }

    /**
     * Kirim request OTP ke email yang diinput user
     */
    private void sendOtpRequest() {
        userEmail = emailInput.getText().toString().trim();

        // Validasi email
        if (userEmail.isEmpty()) {
            emailLayout.setError("Email tidak boleh kosong");
            emailInput.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            emailLayout.setError("Format email tidak valid");
            emailInput.requestFocus();
            return;
        }

        emailLayout.setError(null);

        // Disable tombol
        btnSendOtp.setEnabled(false);
        btnSendOtp.setText("Mengirim...");
        emailInput.setEnabled(false);

        // Kirim OTP ke server
        ApiService apiService = ApiClient.getApiService();
        Call<RegisterResponse> call = apiService.registerRequestOtp(
                username,
                password,
                userEmail,
                "", // no_wa
                ""  // alamat
        );

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                btnSendOtp.setEnabled(true);
                btnSendOtp.setText("Kirim OTP");

                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();

                    if (registerResponse.isSuccess()) {
                        Toast.makeText(VerificationActivity.this,
                                "OTP berhasil dikirim ke " + userEmail,
                                Toast.LENGTH_LONG).show();

                        // Enable OTP fields dan tombol konfirmasi
                        otpSent = true;
                        setOtpFieldsEnabled(true);
                        btnConfirm.setEnabled(true);
                        btnSendOtp.setEnabled(false);
                        btnSendOtp.setText("OTP Terkirim");

                        // Start timer
                        startTimer();

                        // Focus ke OTP field pertama
                        otpFields[0].requestFocus();

                    } else {
                        Toast.makeText(VerificationActivity.this,
                                registerResponse.getMessage(),
                                Toast.LENGTH_LONG).show();
                        emailInput.setEnabled(true);
                    }
                } else {
                    Toast.makeText(VerificationActivity.this,
                            "Gagal menghubungi server",
                            Toast.LENGTH_SHORT).show();
                    emailInput.setEnabled(true);
                    Log.e(TAG, "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                btnSendOtp.setEnabled(true);
                btnSendOtp.setText("Kirim OTP");
                emailInput.setEnabled(true);

                Toast.makeText(VerificationActivity.this,
                        "Koneksi gagal: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Network error", t);
            }
        });
    }

    /**
     * Verify OTP
     */
    private void verifyOtp() {
        if (!otpSent) {
            Toast.makeText(this, "Silakan kirim OTP terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder otpBuilder = new StringBuilder();
        for (EditText field : otpFields) {
            otpBuilder.append(field.getText().toString().trim());
        }

        String otp = otpBuilder.toString();

        if (otp.length() != 6) {
            Toast.makeText(this, "Harap isi semua kolom OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable tombol
        btnConfirm.setEnabled(false);
        btnConfirm.setText("Memverifikasi...");

        // Kirim OTP ke server
        ApiService apiService = ApiClient.getApiService();
        Call<RegisterResponse> call = apiService.verifyOtp(otp);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                btnConfirm.setEnabled(true);
                btnConfirm.setText("Konfirmasi");

                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse verifyResponse = response.body();

                    if (verifyResponse.isSuccess()) {
                        Toast.makeText(VerificationActivity.this,
                                "Verifikasi berhasil! Silakan login",
                                Toast.LENGTH_LONG).show();

                        // Pindah ke LoginActivity
                        Intent intent = new Intent(VerificationActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("registered_username", username);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(VerificationActivity.this,
                                verifyResponse.getMessage(),
                                Toast.LENGTH_LONG).show();
                        clearOtpFields();
                    }
                } else {
                    Toast.makeText(VerificationActivity.this,
                            "Gagal memverifikasi OTP",
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                btnConfirm.setEnabled(true);
                btnConfirm.setText("Konfirmasi");

                Toast.makeText(VerificationActivity.this,
                        "Koneksi gagal: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Network error", t);
            }
        });
    }

    /**
     * Resend OTP
     */
    private void resendOtp() {
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        tvResendLink.setEnabled(false);

        ApiService apiService = ApiClient.getApiService();
        Call<RegisterResponse> call = apiService.resendOtp(userEmail);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse resendResponse = response.body();

                    if (resendResponse.isSuccess()) {
                        Toast.makeText(VerificationActivity.this,
                                "OTP baru telah dikirim ke email Anda",
                                Toast.LENGTH_LONG).show();

                        startTimer();
                        clearOtpFields();
                    } else {
                        Toast.makeText(VerificationActivity.this,
                                resendResponse.getMessage(),
                                Toast.LENGTH_LONG).show();
                        tvResendLink.setEnabled(true);
                    }
                } else {
                    Toast.makeText(VerificationActivity.this,
                            "Gagal mengirim ulang OTP",
                            Toast.LENGTH_SHORT).show();
                    tvResendLink.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(VerificationActivity.this,
                        "Koneksi gagal",
                        Toast.LENGTH_SHORT).show();
                tvResendLink.setEnabled(true);
            }
        });
    }

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
    }

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

    private void setOtpFieldsEnabled(boolean enabled) {
        for (EditText field : otpFields) {
            field.setEnabled(enabled);
        }
    }

    private void clearOtpFields() {
        for (EditText field : otpFields) {
            field.setText("");
        }
        otpFields[0].requestFocus();
    }
}
