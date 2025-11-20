package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.button.MaterialButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // <-- Tambahkan import ini
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.majelismdpl.majelis_mdpl.R;

public class DokumentasiActivity extends AppCompatActivity {

    private static final String DRIVE_URL = "https://drive.google.com/drive/folders/link_folder_anda";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dokumentasi);

        // Menghubungkan dan Menangani Window Insets (Perhatian: ID di bawah mungkin perlu diperiksa)
        // Jika Anda menggunakan layout terbaru, ID-nya harusnya 'main' atau layout terluar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dokumentasi_card), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // =======================================================
        // TAMBAHAN: Setup Toolbar dan Tombol Kembali
        // =======================================================
        Toolbar toolbar = findViewById(R.id.toolbar_dokumentasi);
        // Penting: Set Toolbar sebagai Action Bar
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            // Aktifkan ikon navigasi (tombol back)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Opsional: Matikan title bawaan jika Anda menggunakan TextView custom
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Tambahkan listener untuk menangani klik pada tombol navigasi (ic_arrow_back)
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perintah untuk kembali ke Activity/Fragment sebelumnya
                onBackPressed();
            }
        });
        // =======================================================


        // 1. Dapatkan referensi ke MaterialButton dari layout
        MaterialButton btnLihatGaleri = findViewById(R.id.btn_lihat_galeri);

        // 2. Tambahkan OnClickListener ke tombol
        btnLihatGaleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleDriveFolder();
            }
        });
    }

    /**
     * Fungsi untuk membuka tautan Google Drive menggunakan Intent
     */
    private void openGoogleDriveFolder() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(DRIVE_URL));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}