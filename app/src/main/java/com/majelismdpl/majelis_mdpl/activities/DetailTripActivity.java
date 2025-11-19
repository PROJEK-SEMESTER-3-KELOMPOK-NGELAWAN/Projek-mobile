package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.databinding.ActivityDetailTripBinding;
import com.majelismdpl.majelis_mdpl.models.PreviewPesertaAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailTripActivity extends AppCompatActivity {

    private ActivityDetailTripBinding binding;
    private String currentTripId;
    private PreviewPesertaAdapter previewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailTripBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupClickListeners();
        setupPreviewPesertaRecycler();
        loadTripDataFromIntent();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupClickListeners() {
        binding.btnBooking.setOnClickListener(v ->
                Toast.makeText(DetailTripActivity.this, "Membuka halaman booking...", Toast.LENGTH_SHORT).show()
        );

        binding.tvLihatSemua.setOnClickListener(v -> {
            if (currentTripId != null && !currentTripId.isEmpty()) {
                Intent intent = new Intent(DetailTripActivity.this, PesertaActivity.class);
                try {
                    intent.putExtra("TRIP_ID", Integer.parseInt(currentTripId));
                } catch (NumberFormatException e) {
                    intent.putExtra("TRIP_ID", -1);
                }
                startActivity(intent);
            } else {
                Toast.makeText(DetailTripActivity.this, "Gagal memuat peserta, ID Trip tidak ditemukan.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupPreviewPesertaRecycler() {
        previewAdapter = new PreviewPesertaAdapter();
        binding.rvPreviewPeserta.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        binding.rvPreviewPeserta.setAdapter(previewAdapter);

        // Ganti kode ini agar dapat data dari API/Model trip
        List<String> pesertaThumbs = new ArrayList<>();
        pesertaThumbs.add("https://randomuser.me/api/portraits/men/1.jpg");
        pesertaThumbs.add("https://randomuser.me/api/portraits/women/2.jpg");
        pesertaThumbs.add("https://randomuser.me/api/portraits/men/3.jpg");
        pesertaThumbs.add("https://randomuser.me/api/portraits/women/4.jpg");
        pesertaThumbs.add("https://randomuser.me/api/portraits/men/5.jpg");
        previewAdapter.submitList(pesertaThumbs);

        // Nanti: ketika sudah ada API peserta, isi pesertaThumbs dari List<Peserta> atau List<PesertaHistory>:
        // for (PesertaHistory peserta : listPeserta) pesertaThumbs.add(peserta.getFotoProfil());
        // previewAdapter.submitList(pesertaThumbs);
    }

    private void loadTripDataFromIntent() {
        Intent intent = getIntent();

        currentTripId = String.valueOf(intent.getIntExtra("TRIP_ID", -1));
        String tripTitle = intent.getStringExtra("TRIP_TITLE");
        String tripDate = intent.getStringExtra("TRIP_DATE");
        String tripDuration = intent.getStringExtra("TRIP_DURATION");
        String tripImageUrl = intent.getStringExtra("TRIP_IMAGE_URL");
        String tripJenis = intent.getStringExtra("TRIP_JENIS");

        if (tripTitle != null && !tripTitle.isEmpty()) {
            binding.tvTripTitle.setText(tripTitle);
            binding.toolbar.setTitle(tripTitle);
        } else {
            binding.tvTripTitle.setText("Detail Trip");
        }

        binding.tvTripDate.setText(tripDate != null ? tripDate : "-");

        if (tripDuration != null && !tripDuration.isEmpty()) {
            binding.tvTripDuration.setText(tripDuration);
            binding.tvTripDuration.setVisibility(View.VISIBLE);
        } else {
            binding.tvTripDuration.setVisibility(View.GONE);
        }

        if (tripJenis != null && !tripJenis.isEmpty()) {
            binding.tvTripElevation.setText(tripJenis);
            binding.tvTripElevation.setVisibility(View.VISIBLE);
        } else {
            binding.tvTripElevation.setText("-");
            binding.tvTripElevation.setVisibility(View.VISIBLE);
        }

        if (tripImageUrl != null && !tripImageUrl.isEmpty()) {
            Glide.with(this)
                    .load(tripImageUrl)
                    .placeholder(R.drawable.ic_gunung_ijen)
                    .error(R.drawable.ic_gunung_ijen)
                    .centerCrop()
                    .into(binding.imgHeader);
        } else {
            binding.imgHeader.setImageResource(R.drawable.ic_gunung_ijen);
        }
    }
}
