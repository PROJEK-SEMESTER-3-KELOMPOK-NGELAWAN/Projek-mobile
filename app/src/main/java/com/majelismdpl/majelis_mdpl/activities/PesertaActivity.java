package com.majelismdpl.majelis_mdpl.activities; // Sesuaikan dengan package Anda

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.majelismdpl.majelis_mdpl.R; // Sesuaikan dengan package Anda

public class PesertaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peserta);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_peserta);

        // Menambahkan tombol kembali (back button)
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aksi saat tombol kembali diklik
                onBackPressed();
            }
        });

        // Di sini Anda bisa mengambil data (misal dari Intent)
        // dan mengisi RecyclerView dengan daftar peserta
    }
}