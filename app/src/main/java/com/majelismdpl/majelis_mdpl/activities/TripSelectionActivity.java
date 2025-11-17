package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.majelismdpl.majelis_mdpl.api.ApiClient;
import com.majelismdpl.majelis_mdpl.api.ApiService;
import com.majelismdpl.majelis_mdpl.databinding.ActivityTripSelectionBinding;
import com.majelismdpl.majelis_mdpl.models.TripSelectionAdapter;
import com.majelismdpl.majelis_mdpl.models.User;
import com.majelismdpl.majelis_mdpl.models.UserTripsResponse;
import com.majelismdpl.majelis_mdpl.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripSelectionActivity extends AppCompatActivity implements TripSelectionAdapter.OnTripClickListener {

    private static final String TAG = "TripSelectionActivity";
    private ActivityTripSelectionBinding binding;
    private TripSelectionAdapter adapter;
    private ApiService apiService;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup ViewBinding
        binding = ActivityTripSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize API
        apiService = ApiClient.getApiService();

        // Get user ID dari SessionManager
        SessionManager sessionManager = SessionManager.getInstance(this);
        User user = sessionManager.getUser();

        if (user == null) {
            Toast.makeText(this, "User tidak ditemukan, silakan login kembali", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userId = user.getIdUser();

        setupToolbar();
        setupRecyclerView();
        showLoading(true);
        loadUserTrips();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new TripSelectionAdapter(this);
        binding.recyclerViewTrips.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewTrips.setAdapter(adapter);
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.recyclerViewTrips.setVisibility(View.GONE);
            binding.layoutEmptyState.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    private void loadUserTrips() {
        Log.d(TAG, "Loading ACTIVE trips for user ID: " + userId);

        Call<UserTripsResponse> call = apiService.getUserTrips(userId);
        call.enqueue(new Callback<UserTripsResponse>() {
            @Override
            public void onResponse(Call<UserTripsResponse> call, Response<UserTripsResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    UserTripsResponse tripsResponse = response.body();

                    Log.d(TAG, "Base URL from API: " + tripsResponse.getBaseUrl());

                    if (tripsResponse.isSuccess()) {
                        List<UserTripsResponse.TripItem> trips = tripsResponse.getData();
                        List<UserTripsResponse.TripItem> availableTrips = new java.util.ArrayList<>();
                        if (trips != null) {
                            for (UserTripsResponse.TripItem trip : trips) {
                                if ("available".equalsIgnoreCase(trip.getStatus())) {
                                    availableTrips.add(trip);
                                    Log.d(TAG, "Trip: " + trip.getNamaGunung() + " (Status: " + trip.getStatus() + ")");
                                } else {
                                    Log.w(TAG, "Trip ignored (not available): " + trip.getNamaGunung() + " (Status: " + trip.getStatus() + ")");
                                }
                            }
                        }
                        if (!availableTrips.isEmpty()) {
                            // TIDAK ADA AUTO-REDIRECT; List SELALU tampil meskipun hanya 1 trip
                            adapter.submitList(availableTrips);
                            binding.recyclerViewTrips.setVisibility(View.VISIBLE);
                            binding.layoutEmptyState.setVisibility(View.GONE);
                        } else {
                            showEmptyState();
                        }
                    } else {
                        showEmptyState();
                        Toast.makeText(TripSelectionActivity.this,
                                tripsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showEmptyState();
                    Toast.makeText(TripSelectionActivity.this,
                            "Gagal memuat data trip", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserTripsResponse> call, Throwable t) {
                showLoading(false);
                showEmptyState();
                Toast.makeText(TripSelectionActivity.this,
                        "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEmptyState() {
        binding.recyclerViewTrips.setVisibility(View.GONE);
        binding.layoutEmptyState.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTripClick(UserTripsResponse.TripItem trip) {
        if ("available".equalsIgnoreCase(trip.getStatus())) {
            openPesertaTrip(trip.getIdTrip());
        } else {
            Toast.makeText(this, "Trip ini sudah tidak aktif", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPesertaTrip(int tripId) {
        Intent intent = new Intent(this, PesertaTripActivity.class);
        intent.putExtra("TRIP_ID", String.valueOf(tripId));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Jika ingin reload data setelah kembali, aktifkan baris di bawah:
        // loadUserTrips();
    }
}
