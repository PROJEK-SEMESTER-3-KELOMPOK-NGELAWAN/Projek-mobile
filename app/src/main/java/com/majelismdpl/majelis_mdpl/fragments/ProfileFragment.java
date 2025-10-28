package com.majelismdpl.majelis_mdpl.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView; // BARU: Import ImageView
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.activities.EditProfileActivity;
import com.majelismdpl.majelis_mdpl.activities.LoginActivity;
import com.majelismdpl.majelis_mdpl.utils.SharedPrefManager;

public class ProfileFragment extends Fragment {

    private TextView tvNamaPengguna, tvUsername, tvPassword, tvWhatsApp, tvEmail, tvAlamat;
    private View btnEditProfile;
    private TextView btnKeluar;
    private ImageView ivProfile;

    // --- BARU: Variabel untuk logika password ---
    private ImageView ivPasswordToggle;
    private boolean isPasswordVisible = false;
    private String realPassword = ""; // Untuk menyimpan password asli
    // -------------------------------------------

    private ActivityResultLauncher<Intent> editProfileLauncher;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK || result.getResultCode() == Activity.RESULT_CANCELED) {
                        populateProfile();
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Pastikan nama layout Anda sudah benar
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hubungkan ke ID dari layout
        tvNamaPengguna = view.findViewById(R.id.tvNamaPengguna);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvPassword = view.findViewById(R.id.tvPassword);
        tvWhatsApp = view.findViewById(R.id.tvWhatsApp);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvAlamat = view.findViewById(R.id.tvAlamat);
        ivProfile = view.findViewById(R.id.ivProfile);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnKeluar = view.findViewById(R.id.btnKeluar);

        // BARU: Hubungkan ImageView mata
        ivPasswordToggle = view.findViewById(R.id.ivPasswordToggle);

        // Memuat data profil saat fragment dibuat
        populateProfile();
        loadProfileImage();

        btnEditProfile.setOnClickListener(v -> {
            if (getActivity() == null) return;
            Intent intent = new Intent(requireActivity(), EditProfileActivity.class);
            editProfileLauncher.launch(intent);
        });

        btnKeluar.setOnClickListener(v -> {
            if (getActivity() == null) return;
            new AlertDialog.Builder(requireContext())
                    .setTitle("Keluar")
                    .setMessage("Anda yakin ingin keluar?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        SharedPrefManager.getInstance(requireActivity()).logout();
                        Intent intent = new Intent(requireActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        requireActivity().finishAffinity();
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });

        // --- BARU: Logika untuk klik ikon mata ---
        ivPasswordToggle.setOnClickListener(v -> {
            // Balik status visibilitas
            isPasswordVisible = !isPasswordVisible;

            if (isPasswordVisible) {
                // Jika ingin TAMPILKAN password
                tvPassword.setText(realPassword); // Tampilkan password asli
                ivPasswordToggle.setImageResource(R.drawable.ic_view_eye); // Ganti ikon ke mata terbuka
                ivPasswordToggle.setAlpha(1.0f); // (Opsional) pastikan ikon jelas
            } else {
                // Jika ingin SEMBUNYIKAN password
                tvPassword.setText("••••••••••"); // Tampilkan bintang
                ivPasswordToggle.setImageResource(R.drawable.ic_hide_eye); // Ganti ikon ke mata tertutup
                ivPasswordToggle.setAlpha(0.7f); // (Opsional) samakan tampilan dengan XML
            }
        });
        // ---------------------------------------
    }

    @Override
    public void onResume() {
        super.onResume();
        populateProfile();
        loadProfileImage();
    }

    private void loadProfileImage() {
        if (getContext() != null && ivProfile != null) {
            String savedUri = SharedPrefManager.getInstance(getContext()).getProfilePhotoUri();
            if (savedUri != null && !savedUri.isEmpty()) {
                try {
                    ivProfile.setImageURI(Uri.parse(savedUri));
                } catch (SecurityException | IllegalArgumentException ignored) {
                }
            } else {
                // ivProfile.setImageResource(R.drawable.icon_leaf);
            }
        }
    }

    private void populateProfile() {
        if (getContext() == null) return;
        SharedPrefManager pref = SharedPrefManager.getInstance(getContext());
        String username = pref.getUsername();
        String email = pref.getEmail();
        String whatsapp = pref.getWhatsapp();
        String address = pref.getAddress();

        // --- BARU: Logika pengambilan password ---
        // 1. Ambil password asli dan simpan di variabel
        String password = pref.getPassword();
        this.realPassword = nonNull(password, ""); // Simpan password asli

        // 2. Selalu reset ke status tersembunyi setiap kali data dimuat
        isPasswordVisible = false;
        if (tvPassword != null) {
            tvPassword.setText("••••••••••"); // Selalu tampilkan bintang saat awal
        }
        if (ivPasswordToggle != null) {
            ivPasswordToggle.setImageResource(R.drawable.ic_hide_eye); // Selalu reset ikon
            ivPasswordToggle.setAlpha(0.7f);
        }
        // ----------------------------------------

        // Set data ke TextViews
        if (tvNamaPengguna != null) {
            tvNamaPengguna.setText(nonNull(username, "Nama Pengguna"));
        }
        if (tvUsername != null) {
            tvUsername.setText(nonNull(username, "-"));
        }
        if (tvEmail != null) {
            tvEmail.setText(nonNull(email, "-"));
        }
        if (tvWhatsApp != null) {
            tvWhatsApp.setText(nonNull(whatsapp, "-"));
        }
        if (tvAlamat != null) {
            tvAlamat.setText(nonNull(address, "-"));
        }
    }

    private String nonNull(String value, String fallback) {
        return (value == null || value.isEmpty() || value.equals("null")) ? fallback : value;
    }
}