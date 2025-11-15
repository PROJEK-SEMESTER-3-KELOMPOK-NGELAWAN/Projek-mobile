package com.majelismdpl.majelis_mdpl.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.majelismdpl.majelis_mdpl.R;
import com.google.android.material.button.MaterialButton;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // --- Set Status Bar Transparan (dari history) ---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }

        // --- PANGGIL FUNGSI ANIMASI BARU ---
        initAnimations();

        // --- Logika Tombol Sign In (dari history) ---
        MaterialButton signInButton = findViewById(R.id.signInButton);
        if (signInButton != null) {
            signInButton.setOnClickListener(v -> {
                try {
                    // Buat Intent untuk memulai LoginActivity
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_out);

                } catch (ActivityNotFoundException e) {
                    // Menampilkan pesan error jika LoginActivity tidak terdaftar di Manifest
                    Toast.makeText(WelcomeActivity.this,
                            "Error: LoginActivity tidak ditemukan. Pastikan sudah terdaftar di AndroidManifest.xml",
                            Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    /**
     * [FUNGSI BARU]
     * Fungsi ini memuat animasi dan menerapkannya ke elemen-elemen
     * di layar secara bertahap (staggered).
     */
    private void initAnimations() {
        // 1. Load animasi "fade-in" sederhana (untuk bagian atas)
        // (android.R.anim.fade_in adalah animasi bawaan Android)
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(800); // Durasi 0.8 detik

        // 2. Ambil elemen bagian atas
        ImageView logoImage = findViewById(R.id.logoImage);
        TextView appTagline = findViewById(R.id.appTagline);

        // 3. Mulai animasi bagian atas
        logoImage.startAnimation(fadeIn);

        // Kita buat animasi baru untuk tagline agar bisa diberi jeda
        Animation fadeInTagline = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeInTagline.setDuration(800);
        fadeInTagline.setStartOffset(200); // Mulai sedikit setelah logo
        appTagline.startAnimation(fadeInTagline);


        // 4. Ambil elemen bagian bawah (yang ingin Anda animasikan)
        TextView welcomeBackText = findViewById(R.id.welcomeBackText);
        TextView welcomeSubtitle = findViewById(R.id.welcomeSubtitle);
        MaterialButton signInButton = findViewById(R.id.signInButton);

        Animation slideUp1 = AnimationUtils.loadAnimation(this, R.anim.slide_up_fade_in);
        slideUp1.setStartOffset(400); // Mulai setelah 400ms
        welcomeBackText.startAnimation(slideUp1);

        Animation slideUp2 = AnimationUtils.loadAnimation(this, R.anim.slide_up_fade_in);
        slideUp2.setStartOffset(500); // Mulai setelah 500ms
        welcomeSubtitle.startAnimation(slideUp2);

        Animation slideUp3 = AnimationUtils.loadAnimation(this, R.anim.slide_up_fade_in);
        slideUp3.setStartOffset(600); // Mulai setelah 600ms
        signInButton.startAnimation(slideUp3);


    }
}