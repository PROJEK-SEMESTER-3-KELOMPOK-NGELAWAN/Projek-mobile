package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.appbar.AppBarLayout; // PENTING: Import AppBarLayout

import com.majelismdpl.majelis_mdpl.R;

public class WhatsappGroupActivity extends AppCompatActivity {

    // Ganti dengan URL grup WhatsApp yang sebenarnya
    private static final String WHATSAPP_GROUP_URL = "https://chat.whatsapp.com/FXB4AEnGJOp2WnO09H00zN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Aktifkan mode Edge-to-Edge. Ini sudah benar.
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_whatsapp_grup); // Asumsi nama layout Anda

        // 1. Setup Toolbar dan AppBarLayout
        // Kita butuh AppBarLayout dan Toolbar (yang ada di dalamnya)
        AppBarLayout appBarLayout = findViewById(R.id.toolbar_whatsapp);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // getSupportActionBar().setDisplayShowTitleEnabled(false); // Biarkan dikomentari
        }

        // =======================================================
        // KODE PERBAIKAN STATUS BAR (Edge-to-Edge Insets Handling)
        // =======================================================

        // A. Penanganan untuk Container Utama (R.id.main_whatsapp)
        // Tujuan: Hanya menerapkan padding untuk Navigation Bar (bawah) & samping.
        // Padding atas dibuat 0 agar AppBarLayout bisa memanjang ke atas.
        View mainContainer = findViewById(R.id.main_whatsapp);
        ViewCompat.setOnApplyWindowInsetsListener(mainContainer, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Atur padding container utama: TOP dibuat 0
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);

            // Return insets agar AppBarLayout juga bisa mengakses insets yang sama
            return insets;
        });

        // B. Penanganan untuk AppBarLayout / Toolbar (R.id.toolbar_whatsapp atau R.id.toolbar)
        // Tujuan: Menerapkan padding atas (tinggi Status Bar) HANYA pada Toolbar,
        // sehingga kontennya tidak tertutup, namun latar belakangnya full.
        ViewCompat.setOnApplyWindowInsetsListener(appBarLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Atur padding toolbar: HANYA TOP yang menggunakan tinggi Status Bar
            // Kita atur padding pada AppBarLayout
            v.setPadding(0, systemBars.top, 0, 0);

            // Konsumsi insets agar tidak diterapkan dua kali pada turunan
            return WindowInsetsCompat.CONSUMED;
        });

        // =======================================================
        // AKHIR KODE PERBAIKAN STATUS BAR
        // =======================================================

        // 2. Listener untuk tombol back di Toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // 3. Setup Tombol Gabung WhatsApp
        MaterialButton btnGabung = findViewById(R.id.btn_gabung_whatsapp);

        btnGabung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsappGroup();
            }
        });
    }

    private void openWhatsappGroup() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(WHATSAPP_GROUP_URL));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal membuka WhatsApp. Pastikan aplikasi terinstal.", Toast.LENGTH_LONG).show();
        }
    }
}