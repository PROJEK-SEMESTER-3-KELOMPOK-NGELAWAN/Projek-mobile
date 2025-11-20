package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.majelismdpl.majelis_mdpl.R;

public class WhatsappGroupActivity extends AppCompatActivity {

    // Ganti dengan TAUNTAN GRUP WHATSAPP yang sebenarnya
    private static final String WHATSAPP_GROUP_URL = "https://chat.whatsapp.com/invitecodeanda";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_whatsapp_grup); // Pastikan nama layout sesuai

        // Penanganan Window Insets (jika Anda ingin mempertahankan EdgeToEdge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_whatsapp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_whatsapp);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Matikan title bawaan karena kita menggunakan TextView custom
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

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

    /**
     * Fungsi untuk membuka tautan Grup WhatsApp
     */
    private void openWhatsappGroup() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(WHATSAPP_GROUP_URL));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // Tambahkan Toast jika aplikasi WhatsApp atau browser tidak dapat dibuka.
        }
    }
}