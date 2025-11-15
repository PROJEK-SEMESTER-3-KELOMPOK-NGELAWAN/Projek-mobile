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

    // --- PERBAIKAN ADA DI DALAM FUNGSI INI ---
    private void loadDataPeserta(String id) {
        // TODO: Ganti simulasi ini dengan panggilan API / Firebase
        List<Peserta> dummyData = new ArrayList<>();

        // PERBAIKAN: Berikan argumen ke-6 (nomorWa)
        dummyData.add(new Peserta("1", "Dimas Dwi", "dimasdwi@mail.com", "HADIR", "dimasdwi", "081234560001")); // <-- TAMBAHKAN NOMOR WA
        dummyData.add(new Peserta("2", "Daffa Mentai", "boss@mail.com", "IZIN", "daffa", "081234560002"));    // <-- TAMBAHKAN NOMOR WA
        dummyData.add(new Peserta("3", "Achmad Rahmadani", "daffa@mail.com", "HADIR", "boss", "081234560003"));       // <-- TAMBAHKAN NOMOR WA
        dummyData.add(new Peserta("4", "Dimas Febry", "dimasfebry@mail.com", "HADIR", "dimas_gontor", "081234560004")); // <-- TAMBAHKAN NOMOR WA
        dummyData.add(new Peserta("5", "Zaidan Alhafidz", "zaidan@mail.com", "Belum Konfirmasi", "zaid", "081234560005")); // <-- TAMBAHKAN NOMOR WA
        dummyData.add(new Peserta("6", "Rintan", "Rintan@mail.com", "HADIR", "rintan", "081234560006")); // <-- TAMBAHKAN NOMOR WA

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