package com.majelismdpl.majelis_mdpl.fragments;

import android.os.Bundle;
import android.text.Editable;  // <-- IMPORT INI
import android.text.TextWatcher; // <-- IMPORT INI
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText; // <-- IMPORT INI

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

    // --- Variabel dipindahkan ke level Class ---
    private RecyclerView recyclerView;
    private EditText etSearch; // <-- Tambahkan ini
    private TripHistoryAdapter tripHistoryAdapter; // <-- Ganti nama & pindahkan
    private List<Trip> fullHistoryList = new ArrayList<>(); // <-- Ini adalah list master

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

        // 1. Inisialisasi semua View
        recyclerView = view.findViewById(R.id.rv_history);
        etSearch = view.findViewById(R.id.etSearch); // <-- Inisialisasi EditText

        // 2. Panggil data dummy Anda
        loadDummyData(); // <-- Kita buat fungsi baru agar rapi

        // 3. Setup RecyclerView dan Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tripHistoryAdapter = new TripHistoryAdapter(fullHistoryList); // <-- Gunakan variabel class
        recyclerView.setAdapter(tripHistoryAdapter);

        // 4. Tambahkan Listener ke Search Bar
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Tidak perlu
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Tidak perlu
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Panggil fungsi filter setiap kali user mengetik
                filter(s.toString());
            }
        });
    }

    /**
     * Fungsi baru untuk memfilter list
     * @param query Teks yang diketik user di search bar
     */
    private void filter(String query) {
        List<Trip> filteredList = new ArrayList<>();

        // Loop dari list MASTER (fullHistoryList)
        for (Trip item : fullHistoryList) {

            // Cek apakah nama gunung mengandung teks yang diketik
            // (Asumsi Anda punya method .getNamaGunung() di model Trip.java)
            if (item.getNamaGunung().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }

        // Update adapter dengan list yang sudah terfilter
        if (tripHistoryAdapter != null) {
            tripHistoryAdapter.filterList(filteredList);
        }
    }

    /**
     * Fungsi baru untuk mengisi data (isinya sama seperti kode Anda sebelumnya)
     */
    private void loadDummyData() {
        fullHistoryList.clear(); // Bersihkan dulu jika ada
        fullHistoryList.add(new Trip("Gunung Rinjani", "15-18 Sep 2025", 10, "Selesai", 4.9, "url_rinjani"));
        fullHistoryList.add(new Trip("Gunung Semeru", "25-28 Agu 2025", 8, "Selesai", 4.8, "url_semeru"));
        fullHistoryList.add(new Trip("Gunung Bromo", "5 Jul 2025", 25, "Selesai", 4.7, "url_bromo"));
        fullHistoryList.add(new Trip("Gunung Gede", "12-13 Jun 2025", 30, "Selesai", 4.6, "url_gede"));
        fullHistoryList.add(new Trip("Gunung Kerinci", "1-4 Mei 2025", 12, "Selesai", 4.9, "url_kerinci"));
        fullHistoryList.add(new Trip("Gunung Papandayan", "20 Apr 2025", 22, "Selesai", 4.5, "url_papandayan"));
        fullHistoryList.add(new Trip("Gunung Ijen", "10 Mar 2025", 18, "Selesai", 4.8, "url_ijen"));
        fullHistoryList.add(new Trip("Gunung Ciremai", "15-16 Feb 2025", 16, "Selesai", 4.7, "url_ciremai"));
        fullHistoryList.add(new Trip("Gunung Salak", "5-6 Jan 2025", 20, "Selesai", 4.4, "url_salak"));
        fullHistoryList.add(new Trip("Gunung Merbabu", "22-24 Des 2024", 14, "Selesai", 4.7, "url_merbabu"));
        fullHistoryList.add(new Trip("Gunung Pangrango", "10-11 Nov 2024", 28, "Selesai", 4.6, "url_pangrango"));
        fullHistoryList.add(new Trip("Gunung Tambora", "1-5 Okt 2024", 7, "Selesai", 4.9, "url_tambora"));
        fullHistoryList.add(new Trip("Puncak Jaya", "1-10 Sep 2024", 5, "Selesai", 5.0, "url_puncakjaya"));
    }
}