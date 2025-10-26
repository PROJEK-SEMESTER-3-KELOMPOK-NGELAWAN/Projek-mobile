package com.majelismdpl.majelis_mdpl.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.majelismdpl.majelis_mdpl.R;
import com.google.android.material.button.MaterialButton;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_welcome);
        
        // Set status bar untuk light mode
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                       View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            
            // Set light status bar
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
        
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
        
        // --- Logika untuk Tombol Sign Up ---
        MaterialButton signUpButton = findViewById(R.id.signUpButton);
        if (signUpButton != null) {
            signUpButton.setOnClickListener(v -> {
                // Untuk saat ini, arahkan ke LoginActivity (nanti bisa diganti dengan RegisterActivity)
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            });
        }
        
        // --- Logika untuk Google Sign In Icon ---
        View googleIcon = findViewById(R.id.socialIconsLayout);
        if (googleIcon != null) {
            googleIcon.setOnClickListener(v -> {
                // Panggil Google OAuth
                try {
                    com.majelismdpl.majelis_mdpl.auth.GoogleAuthManager.startGoogleSignup(WelcomeActivity.this);
                } catch (Exception e) {
                    Toast.makeText(WelcomeActivity.this, "Google Sign-In tidak tersedia", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}