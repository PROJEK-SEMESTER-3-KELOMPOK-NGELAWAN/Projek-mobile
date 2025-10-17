package com.majelismdpl.majelis_mdpl.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    // Deklarasikan variabel untuk view binding.
    // Nama 'FragmentHomeBinding' dibuat otomatis dari nama file layout Anda (fragment_home.xml)
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Gunakan view binding untuk menyiapkan layout. Ini menggantikan cara lama.
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Panggil method untuk menampilkan data statis ke tampilan
        displayStaticTripDetails();

        // Sesuai permintaan, bagian untuk aksi klik (click listeners) sengaja dikosongkan untuk saat ini.
        // Nanti bisa ditambahkan di sini.
    }

    /**
     * Method ini berfungsi untuk mengisi data statis (dummy) ke komponen tampilan.
     * Nantinya, di sinilah Anda akan menempatkan data yang diambil dari database atau internet.
     */
    private void displayStaticTripDetails() {
        // Mengisi data ke komponen tampilan menggunakan 'binding'
        binding.tvTripTitle.setText("Pendakian Gunung Ijen");
        binding.tvTripDate.setText("12-13 Agustus 2025");
        binding.ivTripImage.setImageResource(R.drawable.ic_gunung_ijen);

        // Untuk komponen lain seperti Status Pembayaran, Jumlah Peserta, dll,
        // datanya sudah diatur langsung di file XML melalui @string/...
        // jadi kita tidak perlu mengaturnya lagi di sini, kecuali jika datanya dinamis.
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Ini penting untuk membersihkan referensi binding saat fragment tidak lagi ditampilkan
        // untuk mencegah kebocoran memori (memory leak).
        binding = null;
    }
}