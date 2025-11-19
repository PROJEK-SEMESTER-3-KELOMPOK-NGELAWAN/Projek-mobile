package com.majelismdpl.majelis_mdpl.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.majelismdpl.majelis_mdpl.activities.DetailTripActivity;
import com.majelismdpl.majelis_mdpl.databinding.FragmentHistoryBinding;
import com.majelismdpl.majelis_mdpl.models.TripHistoryItem;
import com.majelismdpl.majelis_mdpl.models.TripHistoryAdapter;
import com.majelismdpl.majelis_mdpl.models.TripHistoryResponse;
import com.majelismdpl.majelis_mdpl.models.User;
import com.majelismdpl.majelis_mdpl.utils.SessionManager;
import com.majelismdpl.majelis_mdpl.api.ApiService;
import com.majelismdpl.majelis_mdpl.api.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment implements TripHistoryAdapter.OnItemClickListener {

    private FragmentHistoryBinding binding;

    public HistoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        loadTripHistory();
    }

    private void loadTripHistory() {
        binding.rvHistory.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        SessionManager sessionManager = SessionManager.getInstance(getContext());
        User user = sessionManager.getUser();
        if (user == null) {
            binding.progressBar.setVisibility(View.GONE);
            return;
        }

        ApiService apiService = ApiClient.getApiService();
        Call<TripHistoryResponse> call = apiService.getUserTripHistory(user.getIdUser());

        call.enqueue(new Callback<TripHistoryResponse>() {
            @Override
            public void onResponse(Call<TripHistoryResponse> call, Response<TripHistoryResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.rvHistory.setVisibility(View.VISIBLE);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<TripHistoryItem> tripData = response.body().getData();

                    TripHistoryAdapter adapter = new TripHistoryAdapter(tripData);
                    adapter.setOnItemClickListener(HistoryFragment.this);
                    binding.rvHistory.setAdapter(adapter);
                } else {
                    // Tampilkan pesan error jika data kosong atau gagal
                }
            }

            @Override
            public void onFailure(Call<TripHistoryResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                // Tampilkan pesan error koneksi jika perlu
            }
        });
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
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
