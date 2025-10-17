package com.majelismdpl.majelis_mdpl.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.models.Trip;
import com.majelismdpl.majelis_mdpl.models.TripHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.rv_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 1. Buat data dummy (nantinya ini berasal dari database/API)
        List<Trip> tripData = new ArrayList<>();
        tripData.add(new Trip("Gunung Rinjani", "15-18 Sep 2025", 10, "Selesai", 4.9, "url_rinjani"));
        tripData.add(new Trip("Gunung Semeru", "25-28 Agu 2025", 8, "Selesai", 4.8, "url_semeru"));
        tripData.add(new Trip("Gunung Bromo", "5 Jul 2025", 25, "Selesai", 4.7, "url_bromo"));
        tripData.add(new Trip("Gunung Gede", "12-13 Jun 2025", 30, "Selesai", 4.6, "url_gede"));
        tripData.add(new Trip("Gunung Kerinci", "1-4 Mei 2025", 12, "Selesai", 4.9, "url_kerinci"));
        tripData.add(new Trip("Gunung Papandayan", "20 Apr 2025", 22, "Selesai", 4.5, "url_papandayan"));
        tripData.add(new Trip("Gunung Ijen", "10 Mar 2025", 18, "Selesai", 4.8, "url_ijen"));
        tripData.add(new Trip("Gunung Ciremai", "15-16 Feb 2025", 16, "Selesai", 4.7, "url_ciremai"));
        tripData.add(new Trip("Gunung Salak", "5-6 Jan 2025", 20, "Selesai", 4.4, "url_salak"));
        tripData.add(new Trip("Gunung Merbabu", "22-24 Des 2024", 14, "Selesai", 4.7, "url_merbabu"));
        tripData.add(new Trip("Gunung Pangrango", "10-11 Nov 2024", 28, "Selesai", 4.6, "url_pangrango"));
        tripData.add(new Trip("Gunung Tambora", "1-5 Okt 2024", 7, "Selesai", 4.9, "url_tambora"));
        tripData.add(new Trip("Puncak Jaya", "1-10 Sep 2024", 5, "Selesai", 5.0, "url_puncakjaya"));
        // 2. Buat instance dari adapter Anda
        TripHistoryAdapter adapter = new TripHistoryAdapter(tripData);

        // 3. Set adapter ke RecyclerView
        recyclerView.setAdapter(adapter);
    }
}

