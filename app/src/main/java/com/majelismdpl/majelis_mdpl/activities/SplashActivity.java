package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.majelismdpl.majelis_mdpl.R;

public class SplashActivity extends AppCompatActivity {

    // Durasi splash screen dalam milidetik (misal: 2.5 detik)
    private static final int SPLASH_DURATION = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Coba memuat layout. Jika R.layout.activity_splash tidak ada, ini bisa crash.
        // Pastikan Anda sudah membuat activity_splash.xml
        try {
            setContentView(R.layout.activity_splash);
        } catch (Exception e) {
            // Jika crash di sini, berarti activity_splash.xml bermasalah.
            // Langsung lempar error agar mudah dibaca di Logcat
            throw new RuntimeException("Error: Gagal memuat R.layout.activity_splash. Pastikan file XML sudah dibuat.", e);
        }

        // --- Membuat Status Bar Transparan (sama seperti di WelcomeActivity) ---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);

            int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

            // Jika latar belakang Anda gelap, Anda mungkin tidak perlu light status bar
            // Hapus/sesuaikan baris ini jika ikon status bar Anda jadi tidak terlihat
            // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //     flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            // }

            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
        // --- Selesai Status Bar ---


        // --- Logika Pindah Halaman ---
        // [PERBAIKAN] Mengganti getMainLoofer() menjadi getMainLooper()
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Buat Intent untuk pindah ke WelcomeActivity
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);

                // Tutup SplashActivity agar tidak bisa kembali
                finish();
            }
        }, SPLASH_DURATION);
    }
}

