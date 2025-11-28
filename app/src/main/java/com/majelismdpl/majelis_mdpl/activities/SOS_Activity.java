package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.majelismdpl.majelis_mdpl.R;

public class SOS_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sos);

        // Handle padding untuk status bar / navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- LOGIC TOMBOL DARURAT ---

        // 1. Tombol Polisi (110)
        findViewById(R.id.btnPolisi).setOnClickListener(v -> makeCall("110"));

        // 2. Tombol Ambulans (118)
        findViewById(R.id.btnAmbulans).setOnClickListener(v -> makeCall("118"));

        // 3. Tombol Basarnas/SAR (115)
        findViewById(R.id.btnBasarnas).setOnClickListener(v -> makeCall("115"));

        // 4. Tombol Manual (Buka dialer kosong)
        findViewById(R.id.btnManual).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            startActivity(intent);
        });

        // --- LOGIC BATALKAN ---
        MaterialButton btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> {
            // Tutup activity kembali ke Home
            finish();
        });
    }

    // Helper method biar codingan lo clean code & reusable
    private void makeCall(String phoneNumber) {
        // Pake ACTION_DIAL biar aman (buka keypad doang, user yang tap call)
        // Kalau pake ACTION_CALL harus minta permission runtime, ribet buat MVP.
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}