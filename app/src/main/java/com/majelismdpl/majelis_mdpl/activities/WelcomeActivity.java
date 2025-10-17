package com.majelismdpl.majelis_mdpl.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.majelismdpl.majelis_mdpl.R;
import com.google.android.material.button.MaterialButton;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // --- Logika untuk Tombol Sign In ---
        MaterialButton signInButton = findViewById(R.id.signInButton);
        if (signInButton != null) {
            signInButton.setOnClickListener(v -> {
                try {
                    // Buat Intent untuk memulai LoginActivity
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Menampilkan pesan error jika LoginActivity tidak terdaftar di Manifest
                    Toast.makeText(WelcomeActivity.this,
                            "Error: LoginActivity tidak ditemukan. Pastikan sudah terdaftar di AndroidManifest.xml",
                            Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(this, "Error: Tombol 'signInButton' tidak ditemukan.", Toast.LENGTH_LONG).show();
        }
    }
}