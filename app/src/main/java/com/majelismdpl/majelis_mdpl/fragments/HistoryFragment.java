package com.majelismdpl.majelis_mdpl.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.activities.DetailTripActivity;
import com.majelismdpl.majelis_mdpl.databinding.FragmentHistoryBinding;
import com.majelismdpl.majelis_mdpl.models.TripHistoryItem;
import com.majelismdpl.majelis_mdpl.models.TripHistoryAdapter;
import com.majelismdpl.majelis_mdpl.models.TripHistoryResponse;
import com.majelismdpl.majelis_mdpl.models.User;
import com.majelismdpl.majelis_mdpl.utils.SessionManager;
import com.majelismdpl.majelis_mdpl.api.ApiService;
import com.majelismdpl.majelis_mdpl.api.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment implements TripHistoryAdapter.OnItemClickListener {

    private static final String TAG = "HistoryFragment";
    private FragmentHistoryBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TripHistoryAdapter adapter;
    private List<TripHistoryItem> tripHistoryList = new ArrayList<>();
    private List<TripHistoryItem> filteredList = new ArrayList<>();
    private EditText etSearch;

    public HistoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup RecyclerView
        binding.rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TripHistoryAdapter(filteredList);
        adapter.setOnItemClickListener(this);
        binding.rvHistory.setAdapter(adapter);

        // Setup SwipeRefreshLayout - FIXED: hanya gunakan brown_dark
        swipeRefreshLayout = binding.swipeRefreshLayout;
        swipeRefreshLayout.setColorSchemeResources(R.color.brown_dark);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            Log.d(TAG, "🔄 Refresh triggered");
            loadTripHistory();
        });

        // Setup Search
        etSearch = binding.etSearch;
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTrips(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Initial load
        loadTripHistory();
    }

    private void loadTripHistory() {
        // Show loading (jika belum ada data)
        if (tripHistoryList.isEmpty()) {
            binding.rvHistory.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        SessionManager sessionManager = SessionManager.getInstance(getContext());
        User user = sessionManager.getUser();

        if (user == null) {
            Log.e(TAG, "❌ User tidak ditemukan");
            hideLoading();
            if (getContext() != null) {
                Toast.makeText(getContext(), "Sesi tidak valid. Silakan login kembali.", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        Log.d(TAG, "📥 Loading trip history for User ID: " + user.getIdUser());

        ApiService apiService = ApiClient.getApiService();
        Call<TripHistoryResponse> call = apiService.getUserTripHistory(user.getIdUser());

        call.enqueue(new Callback<TripHistoryResponse>() {
            @Override
            public void onResponse(Call<TripHistoryResponse> call, Response<TripHistoryResponse> response) {
                hideLoading();

                if (response.isSuccessful() && response.body() != null) {
                    TripHistoryResponse historyResponse = response.body();

                    if (historyResponse.isSuccess() && historyResponse.getData() != null) {
                        List<TripHistoryItem> newData = historyResponse.getData();

                        Log.d(TAG, "✅ Loaded " + newData.size() + " trip history items");

                        // Update data
                        tripHistoryList.clear();
                        tripHistoryList.addAll(newData);

                        // Reset filter
                        filteredList.clear();
                        filteredList.addAll(tripHistoryList);
                        adapter.notifyDataSetChanged();

                        // Show RecyclerView
                        binding.rvHistory.setVisibility(View.VISIBLE);

                        if (tripHistoryList.isEmpty() && getContext() != null) {
                            Toast.makeText(getContext(), "Belum ada riwayat trip", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.w(TAG, "⚠️ Response tidak berhasil atau data kosong");
                        showEmptyState();
                    }
                } else {
                    Log.e(TAG, "❌ Response error: " + response.code());
                    showError("Gagal memuat data. Coba lagi.");
                }
            }

            @Override
            public void onFailure(Call<TripHistoryResponse> call, Throwable t) {
                hideLoading();
                Log.e(TAG, "❌ Network error: " + t.getMessage());
                showError("Tidak dapat terhubung ke server. Periksa koneksi internet Anda.");
            }
        });
    }

    private void filterTrips(String query) {
        filteredList.clear();

        if (query == null || query.trim().isEmpty()) {
            // Tampilkan semua data jika query kosong
            filteredList.addAll(tripHistoryList);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();

            // Filter berdasarkan nama gunung, jenis trip, atau tanggal
            for (TripHistoryItem trip : tripHistoryList) {
                if (trip.getMountainName() != null && trip.getMountainName().toLowerCase().contains(lowerCaseQuery) ||
                        trip.getJenisTrip() != null && trip.getJenisTrip().toLowerCase().contains(lowerCaseQuery) ||
                        trip.getDate() != null && trip.getDate().toLowerCase().contains(lowerCaseQuery) ||
                        trip.getStatus() != null && trip.getStatus().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(trip);
                }
            }

            Log.d(TAG, "🔍 Search query: '" + query + "', Found: " + filteredList.size() + " trips");
        }

        adapter.notifyDataSetChanged();

        // Tampilkan toast jika tidak ada hasil
        if (filteredList.isEmpty() && !tripHistoryList.isEmpty() && getContext() != null) {
            Toast.makeText(getContext(), "Tidak ada hasil untuk '" + query + "'", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideLoading() {
        if (binding != null) {
            binding.progressBar.setVisibility(View.GONE);

            // Stop refresh animation jika ada
            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    private void showEmptyState() {
        if (binding != null) {
            binding.rvHistory.setVisibility(View.VISIBLE);
            tripHistoryList.clear();
            filteredList.clear();
            adapter.notifyDataSetChanged();

            if (getContext() != null) {
                Toast.makeText(getContext(), "Belum ada riwayat trip", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }

        // Tetap tampilkan RecyclerView dengan data lama (jika ada)
        if (binding != null) {
            binding.rvHistory.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(TripHistoryItem trip) {
        Intent intent = new Intent(getActivity(), DetailTripActivity.class);
        intent.putExtra("TRIP_ID", trip.getIdTrip());
        intent.putExtra("TRIP_TITLE", trip.getMountainName());
        intent.putExtra("TRIP_DATE", trip.getDate());
        intent.putExtra("TRIP_DURATION", trip.getDuration());
        intent.putExtra("TRIP_IMAGE_URL", trip.getImageUrl());
        intent.putExtra("TRIP_STATUS", trip.getStatus());
        intent.putExtra("TRIP_JENIS", trip.getJenisTrip());
        intent.putExtra("TRIP_PARTICIPANT_COUNT", trip.getParticipants());
        intent.putExtra("TRIP_SLOT", trip.getSlot());
        intent.putExtra("TRIP_TOTAL_HARGA", trip.getTotalHarga());
        intent.putExtra("BOOKING_STATUS", trip.getStatus());
        intent.putExtra("TRIP_RATING", trip.getRating());
        startActivity(intent);

        Log.d(TAG, "📌 Opening detail for Trip ID: " + trip.getIdTrip());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
