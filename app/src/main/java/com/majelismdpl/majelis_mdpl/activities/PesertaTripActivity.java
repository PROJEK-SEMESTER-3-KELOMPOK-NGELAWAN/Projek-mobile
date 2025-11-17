package com.majelismdpl.majelis_mdpl.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.majelismdpl.majelis_mdpl.api.ApiClient;
import com.majelismdpl.majelis_mdpl.api.ApiService;
import com.majelismdpl.majelis_mdpl.databinding.ActivityPesertaTripBinding;
import com.majelismdpl.majelis_mdpl.models.Peserta;
import com.majelismdpl.majelis_mdpl.models.PesertaAdapter;
import com.majelismdpl.majelis_mdpl.models.TripParticipantsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesertaTripActivity extends AppCompatActivity {

    private static final String TAG = "PesertaTripActivity";
    private ActivityPesertaTripBinding binding;
    private PesertaAdapter pesertaAdapter;
    private String tripId;
    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup ViewBinding
        binding = ActivityPesertaTripBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize API
        apiService = ApiClient.getInstance().create(ApiService.class);

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
        showLoading(true);
        loadDataPeserta(tripId);
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        pesertaAdapter = new PesertaAdapter();
        if (binding.recyclerViewPeserta.getLayoutManager() == null) {
            binding.recyclerViewPeserta.setLayoutManager(new LinearLayoutManager(this));
        }
        binding.recyclerViewPeserta.setAdapter(pesertaAdapter);
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.recyclerViewPeserta.setVisibility(View.GONE);
            binding.tvEmptyState.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    private void loadDataPeserta(String id) {
        Log.d(TAG, "Loading participants for trip ID: " + id);

        Call<TripParticipantsResponse> call = apiService.getTripParticipants(id);
        call.enqueue(new Callback<TripParticipantsResponse>() {
            @Override
            public void onResponse(Call<TripParticipantsResponse> call, Response<TripParticipantsResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    TripParticipantsResponse tripResponse = response.body();

                    if (tripResponse.isSuccess()) {
                        List<Peserta> participants = tripResponse.getData();

                        if (participants != null && !participants.isEmpty()) {
                            Log.d(TAG, "Participants loaded: " + participants.size());
                            pesertaAdapter.submitList(participants);
                            binding.recyclerViewPeserta.setVisibility(View.VISIBLE);
                            binding.tvEmptyState.setVisibility(View.GONE);

                            // Update toolbar title dengan jumlah peserta
                            binding.toolbar.setTitle("Daftar Peserta (" + participants.size() + ")");
                        } else {
                            showEmptyState("Belum ada peserta untuk trip ini");
                        }
                    } else {
                        Log.e(TAG, "API returned error: " + tripResponse.getMessage());

                        // Cek apakah error karena trip sudah tidak aktif
                        if (tripResponse.getMessage().contains("tidak aktif") ||
                                tripResponse.getMessage().contains("done")) {
                            showEmptyState("Trip ini sudah tidak aktif");
                            Toast.makeText(PesertaTripActivity.this,
                                    "Trip ini sudah tidak aktif, tidak bisa melihat peserta",
                                    Toast.LENGTH_LONG).show();

                            // Tutup activity setelah 2 detik
                            binding.getRoot().postDelayed(() -> finish(), 2000);
                        } else {
                            showEmptyState("Gagal memuat data peserta");
                            Toast.makeText(PesertaTripActivity.this,
                                    tripResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Log.e(TAG, "Response not successful: " + response.code());
                    showEmptyState("Gagal memuat data peserta");
                    Toast.makeText(PesertaTripActivity.this,
                            "Gagal memuat data peserta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TripParticipantsResponse> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "API call failed", t);
                showEmptyState("Koneksi gagal");
                Toast.makeText(PesertaTripActivity.this,
                        "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEmptyState(String message) {
        binding.recyclerViewPeserta.setVisibility(View.GONE);
        binding.tvEmptyState.setVisibility(View.VISIBLE);
        binding.tvEmptyState.setText(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
