package com.majelismdpl.majelis_mdpl.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

// (OPSIONAL) Import Glide jika Anda ingin memuat gambar profil dari URL
// import com.bumptech.glide.Glide;

import com.google.android.material.button.MaterialButton;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.activities.EditProfileActivity;
import com.majelismdpl.majelis_mdpl.activities.LoginActivity;
import com.majelismdpl.majelis_mdpl.utils.SharedPrefManager;

// (DIUBAH) Pastikan Anda mengimpor model User Anda
import com.majelismdpl.majelis_mdpl.models.User;

public class ProfileFragment extends Fragment {

    // 1. Deklarasikan semua View Anda
    private ImageView ivProfile;
    private TextView tvNamaPengguna, tvUsername, tvPassword, tvWhatsApp, tvEmail, tvAlamat;
    private MaterialButton btnEditProfile, btnKeluar;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout untuk fragment ini
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 2. Hubungkan View dengan ID dari XML
        ivProfile = view.findViewById(R.id.ivProfile);
        tvNamaPengguna = view.findViewById(R.id.tvNamaPengguna);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvPassword = view.findViewById(R.id.tvPassword);
        tvWhatsApp = view.findViewById(R.id.tvWhatsApp);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvAlamat = view.findViewById(R.id.tvAlamat);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnKeluar = view.findViewById(R.id.btnKeluar);

        // 3. Panggil metode untuk mengisi data
        populateProfileData();

        // 4. Atur OnClickListener untuk Tombol
        setupClickListeners();
    }

    /**
     * Mengisi data pengguna ke TextViews.
     * Ambil data dari SharedPrefManager Anda.
     */
    private void populateProfileData() {
        if (getContext() == null) return;

        try {
            // (FIX) Ini sekarang akan BERHASIL!
            User user = SharedPrefManager.getInstance(getContext()).getUser();

            if (user != null) {
                // (FIX) Semua baris ini akan terisi dengan data yang benar
                // Pastikan getNama(), getUsername(), dll. ada di User.java
                tvNamaPengguna.setText(user.getNama());
                tvUsername.setText(user.getUsername());
                tvPassword.setText("••••••••••"); // Password JANGAN pernah ditampilkan
                tvWhatsApp.setText(user.getWhatsapp());
                tvEmail.setText(user.getEmail());
                tvAlamat.setText(user.getAlamat());

                // (OPSIONAL) Muat gambar profil
                // if (user.getFotoUrl() != null && !user.getFotoUrl().isEmpty()) {
                //     Glide.with(this).load(user.getFotoUrl()).into(ivProfile);
                // }

            } else {
                Toast.makeText(getContext(), "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                logoutUser(); // Panggil logout jika data user null
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Gagal memuat data profil", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Mengatur semua listener klik untuk tombol di fragment ini.
     */
    private void setupClickListeners() {
        if (getContext() == null) return;

        // Tombol Edit Profil
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        // Tombol Keluar
        btnKeluar.setOnClickListener(v -> {
            showLogoutDialog();
        });
    }

    /**
     * Menampilkan dialog konfirmasi logout.
     */
    private void showLogoutDialog() {
        if (getContext() == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle("Konfirmasi Keluar")
                .setMessage("Apakah Anda yakin ingin keluar dari akun ini?")
                .setPositiveButton("Keluar", (dialog, which) -> logoutUser())
                .setNegativeButton("Batal", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Menghandle proses logout.
     */
    private void logoutUser() {
        if (getContext() == null) return;

        // Hapus sesi
        SharedPrefManager.getInstance(getContext()).logout();

        // Arahkan ke LoginActivity
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Tutup Activity saat ini (MainActivity)
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}