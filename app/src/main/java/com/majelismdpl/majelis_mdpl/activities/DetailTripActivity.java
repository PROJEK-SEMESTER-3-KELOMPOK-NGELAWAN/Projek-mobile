package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.databinding.ActivityDetailTripBinding;

public class DetailTripActivity extends AppCompatActivity {

    private ActivityDetailTripBinding binding;
    private String currentTripId; // <-- PERBAIKAN 1: Variabel untuk menyimpan ID Trip

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailTripBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Panggil fungsi-fungsi setup
        setupToolbar();
        setupClickListeners();

        // Panggil fungsi untuk memuat data
        loadTripDataFromIntent();
    }

    /**
     * Mengatur Toolbar, terutama tombol kembali.
     */
    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Menutup activity ini
            }
        });
    }

    /**
     * Mengatur semua listener untuk elemen yang bisa diklik.
     */
    private void setupClickListeners() {
        // Listener untuk "Booking Trip Serupa"
        binding.btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailTripActivity.this, "Membuka halaman booking...", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener untuk "Lihat semua" (peserta)
        binding.tvLihatSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // --- PERBAIKAN 3: Logika yang diperbarui ---

                // Cek dulu apakah ID trip-nya ada (diterima dari layar sebelumnya)
                if (currentTripId != null && !currentTripId.isEmpty()) {
                    Intent intent = new Intent(DetailTripActivity.this, PesertaTripActivity.class);
                    // Ini adalah perbaikan utamanya: Kirim ID Trip ke PesertaTripActivity
                    intent.putExtra("TRIP_ID", currentTripId);
                    startActivity(intent);
                } else {
                    // Tampilkan error jika karena suatu alasan ID-nya tidak ada
                    Toast.makeText(DetailTripActivity.this, "Error: Gagal memuat data peserta, ID Trip tidak ditemukan.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Mengambil data yang dikirim dari HistoryFragment dan menampilkannya.
     */
    private void loadTripDataFromIntent() {
        // 1. Ambil Intent yang memulai activity ini
        Intent intent = getIntent();

        // 2. Ambil data dari Intent menggunakan KEY yang sama persis
        String tripTitle = intent.getStringExtra("TRIP_TITLE");
        String tripDate = intent.getStringExtra("TRIP_DATE");
        String imageUrl = intent.getStringExtra("TRIP_IMAGE_URL");
        double tripRating = intent.getDoubleExtra("TRIP_RATING", 0.0);

        // --- PERBAIKAN 2: Ambil ID Trip dan simpan ---
        currentTripId = intent.getStringExtra("TRIP_ID");

        // 3. Set data yang diterima ke Views

        // Set Judul
        if (tripTitle != null) {
            binding.tvTripTitle.setText(tripTitle);
            binding.toolbar.setTitle(tripTitle); // Ganti judul toolbar juga
        } else {
            binding.tvTripTitle.setText("Detail Trip");
        }

        // Set Tanggal
        if (tripDate != null) {
            binding.tvTripDate.setText(tripDate);
        } else {
            binding.tvTripDate.setText("Tanggal tidak tersedia");
        }

        // Set Rating
        binding.ratingBar.setRating((float) tripRating);

        // Set Gambar Header
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // (Logika Glide/Gambar Anda...)

            if (tripTitle != null && tripTitle.toLowerCase().contains("ijen")) {
                binding.imgHeader.setImageResource(R.drawable.ic_gunung_ijen);
            } else {
                binding.imgHeader.setImageResource(R.drawable.ic_gunung_ijen);
            }
        } else {
            binding.imgHeader.setImageResource(R.drawable.ic_gunung_ijen);
        }

        // 4. DATA YANG TIDAK DIKIRIM DARI HISTORY
        binding.tvTripDuration.setText("Detail Durasi (dari API)");
        binding.tvTripElevation.setText("Detail Elevasi (dari API)");
        binding.tvReviewText.setText("Ulasan lengkap akan dimuat di sini...");
    }
}