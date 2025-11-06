package com.majelismdpl.majelis_mdpl.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.majelismdpl.majelis_mdpl.databinding.ActivityPesertaTripBinding;
import com.majelismdpl.majelis_mdpl.models.Peserta;
import com.majelismdpl.majelis_mdpl.models.PesertaAdapter;

import java.util.ArrayList;
import java.util.List;

public class PesertaTripActivity extends AppCompatActivity {

    private ActivityPesertaTripBinding binding;
    private PesertaAdapter pesertaAdapter;
    private String tripId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup ViewBinding
        binding = ActivityPesertaTripBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Ambil ID Trip dari Intent
        if (getIntent() != null && getIntent().hasExtra("TRIP_ID")) {
            tripId = getIntent().getStringExtra("TRIP_ID");
            if (tripId != null) tripId = tripId.trim();
        }

        if (tripId == null || tripId.isEmpty()) {
            Toast.makeText(this, "ID Trip tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupToolbar();
        setupRecyclerView();
        loadDataPeserta(tripId);
    }

    private void setupToolbar() {
        // Navigasi back pada toolbar
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        // (opsional) set title jika ingin dinamis:
        // binding.toolbar.setTitle("Daftar Peserta");
    }

    private void setupRecyclerView() {
        pesertaAdapter = new PesertaAdapter(); // pastikan adapter sudah diimplementasikan
        // Pastikan LayoutManager ada (walau sudah di-set di XML)
        if (binding.recyclerViewPeserta.getLayoutManager() == null) {
            binding.recyclerViewPeserta.setLayoutManager(new LinearLayoutManager(this));
        }
        binding.recyclerViewPeserta.setAdapter(pesertaAdapter);
    }

    private void loadDataPeserta(String id) {
        // TODO: Ganti simulasi ini dengan panggilan API / Firebase
        List<Peserta> dummyData = new ArrayList<>();

        // PERBAIKAN: Berikan nama drawable sebagai argumen ke-5 (avatarUrl)
        dummyData.add(new Peserta("1", "Dimas Dwi", "dimasdwi@mail.com", "HADIR", "dimasdwi")); // <-- Nama drawable
        dummyData.add(new Peserta("2", "Daffa Mentai", "boss@mail.com", "IZIN", "daffa"));    // <-- Nama drawable
        dummyData.add(new Peserta("3", "Achmad Rahmadani", "daffa@mail.com", "HADIR", "boss"));       // <-- Nama drawable
        dummyData.add(new Peserta("4", "Dimas Febry", "dimasfebry@mail.com", "HADIR", "dimas_gontor")); // <-- Nama drawable
        dummyData.add(new Peserta("5", "Zaidan Alhafidz", "zaidan@mail.com", "Belum Konfirmasi", "zaid")); // <-- Nama drawable
        dummyData.add(new Peserta("6", "Rintan", "Rintan@mail.com", "HADIR", "rintan")); // <-- Nama drawable tambahan

        // ... (sisanya sama)

        if (dummyData != null && !dummyData.isEmpty()) {
            pesertaAdapter.submitList(dummyData);
            binding.recyclerViewPeserta.setVisibility(android.view.View.VISIBLE);
        } else {
            binding.recyclerViewPeserta.setVisibility(android.view.View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Bebaskan reference binding (opsional)
        binding = null;
    }
}
