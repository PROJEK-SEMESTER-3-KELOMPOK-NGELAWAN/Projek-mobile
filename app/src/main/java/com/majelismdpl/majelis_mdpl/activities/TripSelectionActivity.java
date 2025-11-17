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
        Log.d(TAG, "Loading trips for user ID: " + userId);

        Call<UserTripsResponse> call = apiService.getUserTrips(userId);
        call.enqueue(new Callback<UserTripsResponse>() {
            @Override
            public void onResponse(Call<UserTripsResponse> call, Response<UserTripsResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    UserTripsResponse tripsResponse = response.body();

                    // Log base URL untuk debugging
                    Log.d(TAG, "Base URL from API: " + tripsResponse.getBaseUrl());

                    if (tripsResponse.isSuccess()) {
                        List<UserTripsResponse.TripItem> trips = tripsResponse.getData();

                        if (trips != null && !trips.isEmpty()) {
                            Log.d(TAG, "Trips loaded: " + trips.size());

                            // Log detail setiap trip untuk debugging
                            for (UserTripsResponse.TripItem trip : trips) {
                                Log.d(TAG, "Trip: " + trip.getNamaGunung());
                                Log.d(TAG, "  - gambar_file: " + trip.getGambarFile());
                                Log.d(TAG, "  - gambar_url: " + trip.getGambarUrl());
                            }

                            // Jika hanya 1 trip, langsung buka PesertaTripActivity
                            if (trips.size() == 1) {
                                openPesertaTrip(trips.get(0).getIdTrip());
                            } else {
                                // Jika lebih dari 1, tampilkan list
                                adapter.submitList(trips);
                                binding.recyclerViewTrips.setVisibility(View.VISIBLE);
                                binding.layoutEmptyState.setVisibility(View.GONE);
                            }
                        } else {
                            showEmptyState();
                        }
                    } else {
                        Log.e(TAG, "API returned error: " + tripsResponse.getMessage());
                        showEmptyState();
                        Toast.makeText(TripSelectionActivity.this,
                                tripsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Response not successful: " + response.code());
                    showEmptyState();
                    Toast.makeText(TripSelectionActivity.this,
                            "Gagal memuat data trip", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserTripsResponse> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "API call failed", t);
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
        // Buka PesertaTripActivity dengan id_trip yang dipilih
        openPesertaTrip(trip.getIdTrip());
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
        // Refresh data saat kembali dari PesertaTripActivity
        if (adapter != null && adapter.getItemCount() > 0) {
            // Data sudah ada, tidak perlu reload
        }
    }
}
