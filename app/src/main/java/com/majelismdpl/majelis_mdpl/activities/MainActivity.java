package com.majelismdpl.majelis_mdpl.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.fragments.HistoryFragment;
import com.majelismdpl.majelis_mdpl.fragments.HomeFragment;
import com.majelismdpl.majelis_mdpl.fragments.InfoFragment;
import com.majelismdpl.majelis_mdpl.fragments.ProfileFragment;
import com.majelismdpl.majelis_mdpl.utils.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cek session login menggunakan SharedPrefManager (konsisten dengan LoginActivity)
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return; // PENTING: return agar kode di bawah tidak dieksekusi
        }

        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Meload fragment default (Home) saat aplikasi pertama kali dibuka
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // Listener ketika ikon bottom navigation diklik
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_history) {
                selectedFragment = new HistoryFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            } else if (itemId == R.id.nav_info) {
                selectedFragment = new InfoFragment();
            }

            // Animasi sederhana pada ikon menu yang diklik
            View iconView = bottomNavigationView.findViewById(itemId);
            if (iconView instanceof ImageView) {
                animateMenuItem((ImageView) iconView);
            }

            return loadFragment(selectedFragment);
        });
    }

    // Fungsi animasi scale pada ikon yang dipilih
    private void animateMenuItem(@NonNull ImageView iconView) {
        // Membuat animasi zoom in, lalu zoom out
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(iconView, View.SCALE_X, 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(iconView, View.SCALE_Y, 1f, 1.2f, 1f);
        scaleX.setDuration(250);
        scaleY.setDuration(250);
        scaleX.start();
        scaleY.start();
    }

    // Fungsi mengganti fragment yang aktif
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;

    }
}
