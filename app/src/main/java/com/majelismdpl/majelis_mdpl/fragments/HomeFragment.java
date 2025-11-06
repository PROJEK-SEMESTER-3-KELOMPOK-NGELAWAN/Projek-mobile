package com.majelismdpl.majelis_mdpl.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// Impor Activity tujuan
import com.majelismdpl.majelis_mdpl.activities.PesertaTripActivity;
// Impor View Binding
import com.majelismdpl.majelis_mdpl.databinding.FragmentHomeBinding;
// Impor R (jika diperlukan untuk drawable, dll)
import com.majelismdpl.majelis_mdpl.R;

public class HomeFragment extends Fragment {

    // 1. Deklarasikan variabel binding
    //    Ganti 'FragmentHomeBinding' jika nama file XML Anda berbeda
    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // 2. Inflate layout menggunakan View Binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 3. Akses views langsung melalui 'binding' (tanpa findViewById)

        // Set data statis
        binding.tvTripTitle.setText("Pendakian Gunung Ijen");
        binding.tvTripDate.setText("12-13 Agustus 2025");
        binding.ivTripImage.setImageResource(R.drawable.ic_gunung_ijen);

        // 4. Tambahkan listener yang hilang (sesuai permintaan Anda sebelumnya)
        binding.menuPesertaTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Buat Intent untuk berpindah ke PesertaActivity
                Intent intent = new Intent(getActivity(), PesertaTripActivity.class);

                // (Opsional) Kirim data jika perlu
                // intent.putExtra("TRIP_ID", "id_perjalanan_123");

                // Mulai Activity baru
                startActivity(intent);
            }
        });

        // Anda bisa tambahkan listener lain di sini
        // binding.menuTitikKumpul.setOnClickListener(...);
        // binding.menuGrupWhatsapp.setOnClickListener(...);
    }

    // 5. Wajib ada di Fragment saat pakai View Binding
    //    Ini untuk mencegah memory leak
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}