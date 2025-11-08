package com.majelismdpl.majelis_mdpl.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri; // (BARU) Impor untuk URI
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.activities.EditProfileActivity;
import com.majelismdpl.majelis_mdpl.activities.LoginActivity;
import com.majelismdpl.majelis_mdpl.utils.SessionManager;
import com.majelismdpl.majelis_mdpl.models.User;

public class ProfileFragment extends Fragment {

    private ImageView ivProfile;
    private TextView tvNamaPengguna, tvUsername, tvPassword, tvWhatsApp, tvEmail, tvAlamat;
    private MaterialButton btnEditProfile, btnKeluar;
    private ActivityResultLauncher<Intent> editProfileLauncher;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inisialisasi launcher (Ini sudah benar)
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // Panggil populateProfileData() untuk refresh SEMUA data
                            // (termasuk foto yang baru)
                            populateProfileData();
                            Toast.makeText(getContext(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hubungkan View (Ini sudah benar)
        ivProfile = view.findViewById(R.id.ivProfile);
        tvNamaPengguna = view.findViewById(R.id.tvNamaPengguna);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvPassword = view.findViewById(R.id.tvPassword);
        tvWhatsApp = view.findViewById(R.id.tvWhatsApp);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvAlamat = view.findViewById(R.id.tvAlamat);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnKeluar = view.findViewById(R.id.btnKeluar);

        populateProfileData();
        setupClickListeners();
    }

    /**
     * Mengisi data pengguna ke TextViews DAN ImageView.
     */
    private void populateProfileData() {
        if (getContext() == null) return;

        try {
            // Ambil data user
            User user = SessionManager.getInstance(getContext()).getUser();

            if (user != null) {
                // Set semua data Teks (Sudah benar)
                tvNamaPengguna.setText(user.getUsername());
                tvUsername.setText(user.getUsername());
                tvPassword.setText("••••••••••");
                tvWhatsApp.setText(user.getWhatsapp());
                tvEmail.setText(user.getEmail());
                tvAlamat.setText(user.getAlamat());

                // --- (LOGIKA BARU DIMULAI DI SINI) ---
                // Muat gambar profil yang disimpan
                String fotoUriString = SessionManager.getInstance(getContext()).getProfilePhotoUri();
                if (fotoUriString != null && !fotoUriString.isEmpty()) {
                    try {
                        // Ubah string URI kembali menjadi URI dan set ke ImageView
                        ivProfile.setImageURI(Uri.parse(fotoUriString));
                    } catch (SecurityException | IllegalArgumentException e) {
                        // Tangani jika URI tidak valid atau izin dicabut
                        e.printStackTrace();
                        ivProfile.setImageResource(R.drawable.ic_aplikasi_majelismdpl); // Gambar default
                    }
                } else {
                    // Tampilkan gambar default jika tidak ada URI yang disimpan
                    ivProfile.setImageResource(R.drawable.ic_aplikasi_majelismdpl);
                }
                // --- (LOGIKA BARU SELESAI) ---

            } else {
                Toast.makeText(getContext(), "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                logoutUser();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Gagal memuat data profil", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Mengatur semua listener klik.
     * (Logika ini sudah benar)
     */
    private void setupClickListeners() {
        if (getContext() == null) return;

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);

            // Logika pengiriman data Anda ke EditProfileActivity sudah tidak diperlukan
            // karena EditProfileActivity sekarang memuat dari SharedPref.
            // Tapi tidak apa-apa jika dibiarkan, kode di EditProfileActivity
            // akan menimpanya dengan data dari SharedPref.

            // (DIUBAH) Gunakan launcher
            editProfileLauncher.launch(intent);
        });

        btnKeluar.setOnClickListener(v -> {
            showLogoutDialog();
        });
    }

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

    private void logoutUser() {
        if (getContext() == null) return;
        SessionManager.getInstance(getContext()).logout();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}