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
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.databinding.FragmentHistoryBinding;
import com.majelismdpl.majelis_mdpl.models.Trip;
import com.majelismdpl.majelis_mdpl.models.TripHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment implements TripHistoryAdapter.OnItemClickListener {

    private FragmentHistoryBinding binding;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        // Buat data dummy
        List<Trip> tripData = createDummyData();

        // Buat instance dari adapter Anda
        TripHistoryAdapter adapter = new TripHistoryAdapter(tripData);

        // Set listener ke adapter
        adapter.setOnItemClickListener(this);

        // Set adapter ke RecyclerView
        binding.rvHistory.setAdapter(adapter);
    }

    // (PENTING) Override method onItemClick dari interface
    @Override
    public void onItemClick(Trip trip) {
        // Metode ini akan dipanggil oleh Adapter saat item diklik
        Intent intent = new Intent(getActivity(), DetailTripActivity.class);

        // --- INI PERBAIKAN UTAMANYA ---
        // Kirim ID Trip agar bisa diteruskan ke PesertaTripActivity
        intent.putExtra("TRIP_ID", trip.getId()); // <-- 1. TAMBAHKAN BARIS INI

        // Kirim sisa data seperti sebelumnya
        intent.putExtra("TRIP_TITLE", trip.getMountainName());
        intent.putExtra("TRIP_DATE", trip.getDate());
        intent.putExtra("TRIP_PARTICIPANT_COUNT", trip.getParticipants());
        intent.putExtra("TRIP_STATUS", trip.getStatus());
        intent.putExtra("TRIP_RATING", trip.getRating());
        intent.putExtra("TRIP_IMAGE_URL", trip.getImageUrl());

        // Mulai Activity baru
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // (Helper function agar onViewCreated lebih rapi)
    private List<Trip> createDummyData() {
        List<Trip> tripData = new ArrayList<>();

        // --- 2. PERBAIKI CONSTRUCTOR (Tambahkan ID unik di parameter pertama) ---
        tripData.add(new Trip("T1", "Gunung Rinjani", "15-18 Sep 2025", 10, "Selesai", 4.9, "url_rinjani"));
        tripData.add(new Trip("T2", "Gunung Semeru", "25-28 Agu 2025", 8, "Selesai", 4.8, "url_semeru"));
        tripData.add(new Trip("T3", "Gunung Bromo", "5 Jul 2025", 25, "Selesai", 4.7, "url_bromo"));
        tripData.add(new Trip("T4", "Gunung Gede", "12-13 Jun 2025", 30, "Selesai", 4.6, "url_gede"));
        tripData.add(new Trip("T5", "Gunung Kerinci", "1-4 Mei 2025", 12, "Selesai", 4.9, "url_kerinci"));
        tripData.add(new Trip("T6", "Gunung Papandayan", "20 Apr 2025", 22, "Selesai", 4.5, "url_papandayan"));
        tripData.add(new Trip("T7", "Gunung Ijen", "10 Mar 2025", 18, "Selesai", 4.8, "url_ijen"));
        tripData.add(new Trip("T8", "Gunung Ciremai", "15-16 Feb 2025", 16, "Selesai", 4.7, "url_ciremai"));
        tripData.add(new Trip("T9", "Gunung Salak", "5-6 Jan 2025", 20, "Selesai", 4.4, "url_salak"));
        tripData.add(new Trip("T10", "Gunung Merbabu", "22-24 Des 2024", 14, "Selesai", 4.7, "url_merbabu"));
        tripData.add(new Trip("T11", "Gunung Pangrango", "10-11 Nov 2024", 28, "Selesai", 4.6, "url_pangrango"));
        tripData.add(new Trip("T12", "Gunung Tambora", "1-5 Okt 2024", 7, "Selesai", 4.9, "url_tambora"));
        tripData.add(new Trip("T13", "Puncak Jaya", "1-10 Sep 2024", 5, "Selesai", 5.0, "url_puncakjaya"));
        return tripData;
    }
}