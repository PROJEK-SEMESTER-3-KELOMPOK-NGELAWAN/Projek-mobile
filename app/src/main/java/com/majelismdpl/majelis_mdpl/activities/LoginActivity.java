package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.api.ApiClient;
import com.majelismdpl.majelis_mdpl.api.ApiService;
import com.majelismdpl.majelis_mdpl.models.LoginResponse;
import com.majelismdpl.majelis_mdpl.models.User;
import com.majelismdpl.majelis_mdpl.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputEditText usernameInput = findViewById(R.id.usernameInput);
        TextInputEditText passwordInput = findViewById(R.id.passwordInput);
        MaterialButton loginButton = findViewById(R.id.loginButton);
        MaterialButton googleButton = findViewById(R.id.googleSignInButton);

        loginButton.setOnClickListener(v -> {
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

            // BYPASS DEV: kredensial khusus untuk testing
            if ("user".equals(username) && "user123".equals(password)) {
                LoginResponse lr = new LoginResponse();
                lr.setSuccess(true);
                lr.setRole("user");
                User u = new User();
                u.setUsername(username);
                lr.setUser(u);
                SharedPrefManager.getInstance(LoginActivity.this).saveLoginResponse(lr);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                return;
            }

            ApiService api = ApiClient.getApiService();
            api.login(username, password).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (!response.isSuccessful() || response.body() == null) {
                        Toast.makeText(LoginActivity.this, "Login gagal: respons tidak valid", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    LoginResponse body = response.body();
                    if (body.isSuccess()) {
                        SharedPrefManager.getInstance(LoginActivity.this).saveLoginResponse(body);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        String msg = body.getMessage() != null ? body.getMessage() : "Login gagal";
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }
            });
        });

        if (googleButton != null) {
            googleButton.setOnClickListener(v ->
                    Toast.makeText(LoginActivity.this, "Google Sign-In belum diimplementasikan", Toast.LENGTH_SHORT).show()
            );
        }
    }
}