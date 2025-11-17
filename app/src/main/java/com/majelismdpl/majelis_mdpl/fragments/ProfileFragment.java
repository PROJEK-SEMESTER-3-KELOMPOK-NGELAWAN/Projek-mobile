package com.majelismdpl.majelis_mdpl.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.majelismdpl.majelis_mdpl.api.ApiClient;
import com.majelismdpl.majelis_mdpl.api.ApiService;
import com.majelismdpl.majelis_mdpl.models.ProfileResponse;
import com.majelismdpl.majelis_mdpl.utils.SessionManager;
import com.majelismdpl.majelis_mdpl.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private ImageView ivProfile;
    private TextView tvNamaPengguna, tvUsername, tvPassword, tvWhatsApp, tvEmail, tvAlamat;
    private MaterialButton btnEditProfile, btnKeluar;
    private ActivityResultLauncher<Intent> editProfileLauncher;

    private View loadingOverlay;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inisialisasi launcher
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // Refresh data dari server
                            fetchProfileFromServer();
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

        // Hubungkan View
        ivProfile = view.findViewById(R.id.ivProfile);
        tvNamaPengguna = view.findViewById(R.id.tvNamaPengguna);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvPassword = view.findViewById(R.id.tvPassword);
        tvWhatsApp = view.findViewById(R.id.tvWhatsApp);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvAlamat = view.findViewById(R.id.tvAlamat);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnKeluar = view.findViewById(R.id.btnKeluar);

        // Tampilkan data dari cache dulu
        populateProfileDataFromCache();

        // Kemudian fetch data terbaru dari server
        fetchProfileFromServer();

        setupClickListeners();
    }

    /**
     * Mengisi data dari cache/SharedPreferences (untuk tampilan cepat)
     */
    private void populateProfileDataFromCache() {
        if (getContext() == null) return;

        try {
            User user = SessionManager.getInstance(getContext()).getUser();

            if (user != null) {
                tvNamaPengguna.setText(user.getUsername());
                tvUsername.setText(user.getUsername());
                tvPassword.setText("••••••••••");
                tvWhatsApp.setText(user.getWhatsapp());
                tvEmail.setText(user.getEmail());
                tvAlamat.setText(user.getAlamat());

                // Muat foto profil dari cache
                String fotoUriString = SessionManager.getInstance(getContext()).getProfilePhotoUri();
                if (fotoUriString != null && !fotoUriString.isEmpty()) {
                    try {
                        ivProfile.setImageURI(Uri.parse(fotoUriString));
                    } catch (SecurityException | IllegalArgumentException e) {
                        e.printStackTrace();
                        ivProfile.setImageResource(R.drawable.ic_aplikasi_majelismdpl);
                    }
                } else {
                    ivProfile.setImageResource(R.drawable.ic_aplikasi_majelismdpl);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetch data profil terbaru dari server
     * Menggunakan POST method dengan JSON body (konsisten dengan get-meeting-point)
     */
    private void fetchProfileFromServer() {
        if (getContext() == null) return;

        User user = SessionManager.getInstance(getContext()).getUser();
        // Ganti .getId() menjadi .getIdUser()
        if (user == null || user.getIdUser() <= 0) {
            Toast.makeText(getContext(), "Sesi tidak valid", Toast.LENGTH_SHORT).show();
            logoutUser();
            return;
        }

        int userId = user.getIdUser();

        // Show loading (optional)
        // showLoading(true);

        ApiService apiService = ApiClient.getApiService();

        // Buat request object menggunakan id_user (konsisten dengan backend/php)
        ApiService.ProfileRequest request = new ApiService.ProfileRequest(userId);
        Call<ProfileResponse> call = apiService.getProfile(request);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                // showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    ProfileResponse profileResponse = response.body();

                    if (profileResponse.isSuccess() && profileResponse.getData() != null) {
                        ProfileResponse.UserData userData = profileResponse.getData();

                        // Update UI dengan data terbaru
                        updateUI(userData);

                        // Update cache
                        updateCache(userData);

                        Log.d(TAG, "Profil berhasil diambil dari server");
                    } else {
                        Toast.makeText(getContext(),
                                profileResponse.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Response error: " + response.code());
                    Toast.makeText(getContext(),
                            "Gagal memuat data profil dari server",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                // showLoading(false);
                Log.e(TAG, "Network error: " + t.getMessage(), t);
                Toast.makeText(getContext(),
                        "Koneksi gagal, menampilkan data cache",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Update UI dengan data dari server
     */
    private void updateUI(ProfileResponse.UserData userData) {
        if (userData == null) return;

        tvNamaPengguna.setText(userData.getUsername() != null ? userData.getUsername() : "");
        tvUsername.setText(userData.getUsername() != null ? userData.getUsername() : "");
        tvPassword.setText("••••••••••");
        tvWhatsApp.setText(userData.getWhatsapp() != null ? userData.getWhatsapp() : "-");
        tvEmail.setText(userData.getEmail() != null ? userData.getEmail() : "-");
        tvAlamat.setText(userData.getAlamat() != null ? userData.getAlamat() : "-");

        // Note: Foto profil tetap menggunakan cache local karena
        // server tidak menyimpan URI local device
        // Foto profil hanya di-update setelah user edit dari EditProfileActivity
    }

    /**
     * Update cache dengan data terbaru dari server
     */
    private void updateCache(ProfileResponse.UserData userData) {
        if (getContext() == null || userData == null) return;

        try {
            User user = SessionManager.getInstance(getContext()).getUser();
            if (user != null) {
                // Update data user
                user.setUsername(userData.getUsername());
                user.setEmail(userData.getEmail());
                user.setWhatsapp(userData.getWhatsapp());
                user.setAlamat(userData.getAlamat());
                user.setRole(userData.getRole());

                // Simpan kembali ke cache
                SessionManager.getInstance(getContext()).updateUser(user);

                Log.d(TAG, "Cache berhasil diupdate");
            }
        } catch (Exception e) {
            Log.e(TAG, "Gagal update cache: " + e.getMessage(), e);
        }
    }

    /**
     * Mengatur semua listener klik
     */
    private void setupClickListeners() {
        if (getContext() == null) return;

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
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

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data saat fragment kembali aktif
        fetchProfileFromServer();
    }
}
