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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.activities.EditProfileActivity;
import com.majelismdpl.majelis_mdpl.activities.LoginActivity;
import com.majelismdpl.majelis_mdpl.api.ApiClient;
import com.majelismdpl.majelis_mdpl.api.ApiService;
import com.majelismdpl.majelis_mdpl.models.ProfileResponse;
import com.majelismdpl.majelis_mdpl.models.User;
import com.majelismdpl.majelis_mdpl.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private ImageView ivProfile;
    private TextView tvNamaPengguna;
    private TextView tvUsername;
    private TextView tvPassword;
    private TextView tvWhatsApp;
    private TextView tvEmail;
    private TextView tvAlamat;
    private com.google.android.material.button.MaterialButton btnEditProfile;
    private com.google.android.material.button.MaterialButton btnKeluar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ActivityResultLauncher<Intent> editProfileLauncher;

    public ProfileFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            fetchProfileFromServer();
                            populateProfileDataFromCache();
                            if (getContext() != null) {
                                Toast.makeText(getContext(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            }
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

        initializeViews(view);
        setupSwipeRefresh();
        populateProfileDataFromCache();
        fetchProfileFromServer();
        setupClickListeners();
    }

    private void initializeViews(View view) {
        ivProfile = view.findViewById(R.id.ivProfile);
        tvNamaPengguna = view.findViewById(R.id.tvNamaPengguna);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvPassword = view.findViewById(R.id.tvPassword);
        tvWhatsApp = view.findViewById(R.id.tvWhatsApp);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvAlamat = view.findViewById(R.id.tvAlamat);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnKeluar = view.findViewById(R.id.btnKeluar);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
    }

    private void setupSwipeRefresh() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeResources(R.color.brown_dark);
            swipeRefreshLayout.setOnRefreshListener(this::fetchProfileFromServer);
        }
    }

    private String buildFullPhotoUrl(String raw) {
        if (raw == null || raw.isEmpty()) return null;
        if (raw.startsWith("http")) {
            return raw; // sudah URL penuh dari server
        }
        String base = ApiClient.getBaseUrl();
        if (!base.endsWith("/")) base += "/";
        if (!raw.startsWith("img/")) {
            raw = "img/profile/" + raw;
        }
        return base + raw;
    }

    private void loadImageIntoView(String url) {
        if (getContext() == null) return;
        if (url == null || url.isEmpty()) {
            ivProfile.setImageResource(R.drawable.ic_aplikasi_majelismdpl);
            return;
        }
        Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.ic_aplikasi_majelismdpl)
                .error(R.drawable.ic_aplikasi_majelismdpl)
                .into(ivProfile);
        Log.d(TAG, "Foto profil dimuat dari URL: " + url);
    }

    private void populateProfileDataFromCache() {
        if (getContext() == null) return;

        try {
            User user = SessionManager.getInstance(getContext()).getUser();

            if (user != null) {
                tvNamaPengguna.setText(user.getUsername() != null ? user.getUsername() : "User");
                tvUsername.setText(user.getUsername() != null ? user.getUsername() : "-");
                tvPassword.setText("••••••••••");
                tvWhatsApp.setText(user.getWhatsapp() != null ? user.getWhatsapp() : "-");
                tvEmail.setText(user.getEmail() != null ? user.getEmail() : "-");
                tvAlamat.setText(user.getAlamat() != null ? user.getAlamat() : "-");

                String fotoUrl = buildFullPhotoUrl(user.getFotoUrl());
                if (fotoUrl != null) {
                    loadImageIntoView(fotoUrl);
                    return;
                }

                String fotoUriString = SessionManager.getInstance(getContext()).getProfilePhotoUri();
                if (fotoUriString != null && !fotoUriString.isEmpty()) {
                    try {
                        Uri photoUri = Uri.parse(fotoUriString);
                        if ("content".equals(photoUri.getScheme())) {
                            final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                            requireContext().getContentResolver()
                                    .takePersistableUriPermission(photoUri, takeFlags);
                        }
                        ivProfile.setImageURI(photoUri);
                        Log.d(TAG, "Foto profil dimuat dari URI lokal: " + fotoUriString);
                    } catch (Exception e) {
                        Log.e(TAG, "Error loading profile image from URI, use default: " + e.getMessage());
                        ivProfile.setImageResource(R.drawable.ic_aplikasi_majelismdpl);
                        SessionManager.getInstance(getContext()).saveProfilePhotoUri(null);
                    }
                } else {
                    ivProfile.setImageResource(R.drawable.ic_aplikasi_majelismdpl);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Error populating cache data: " + e.getMessage());
        }
    }

    private void fetchProfileFromServer() {
        if (getContext() == null) return;

        User user = SessionManager.getInstance(getContext()).getUser();
        if (user == null || user.getIdUser() <= 0) {
            hideLoading();
            Toast.makeText(getContext(), "Sesi tidak valid", Toast.LENGTH_SHORT).show();
            logoutUser();
            return;
        }

        int userId = user.getIdUser();
        Log.d(TAG, "Fetch profile for User ID: " + userId);

        ApiService apiService = ApiClient.getApiService();
        ApiService.ProfileRequest request = new ApiService.ProfileRequest(userId);
        Call<ProfileResponse> call = apiService.getProfile(request);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                hideLoading();

                if (response.isSuccessful() && response.body() != null) {
                    ProfileResponse profileResponse = response.body();

                    if (profileResponse.isSuccess() && profileResponse.getData() != null) {
                        ProfileResponse.UserData userData = profileResponse.getData();
                        updateUI(userData);
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
                hideLoading();
                Log.e(TAG, "Network error: " + t.getMessage(), t);
                Toast.makeText(getContext(),
                        "Koneksi gagal, menampilkan data cache",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideLoading() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void updateUI(ProfileResponse.UserData userData) {
        if (userData == null || getContext() == null) return;

        tvNamaPengguna.setText(userData.getUsername() != null ? userData.getUsername() : "User");
        tvUsername.setText(userData.getUsername() != null ? userData.getUsername() : "-");
        tvPassword.setText("••••••••••");
        tvWhatsApp.setText(userData.getWhatsapp() != null ? userData.getWhatsapp() : "-");
        tvEmail.setText(userData.getEmail() != null ? userData.getEmail() : "-");
        tvAlamat.setText(userData.getAlamat() != null ? userData.getAlamat() : "-");

        String raw = userData.getFotoUrl();
        if (raw == null || raw.isEmpty()) raw = userData.getFotoProfil();
        String fullUrl = buildFullPhotoUrl(raw);
        loadImageIntoView(fullUrl);
    }

    private void updateCache(ProfileResponse.UserData userData) {
        if (getContext() == null || userData == null) return;

        try {
            User user = SessionManager.getInstance(getContext()).getUser();
            if (user != null) {
                user.setUsername(userData.getUsername());
                user.setEmail(userData.getEmail());
                user.setWhatsapp(userData.getWhatsapp());
                user.setAlamat(userData.getAlamat());
                user.setRole(userData.getRole());

                String raw = userData.getFotoUrl();
                if (raw == null || raw.isEmpty()) raw = userData.getFotoProfil();
                user.setFotoUrl(raw);

                SessionManager.getInstance(getContext()).updateUser(user);
                Log.d(TAG, "Cache profil berhasil diupdate");
            }
        } catch (Exception e) {
            Log.e(TAG, "Gagal update cache: " + e.getMessage(), e);
        }
    }

    private void setupClickListeners() {
        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                editProfileLauncher.launch(intent);
            });
        }

        if (btnKeluar != null) {
            btnKeluar.setOnClickListener(v -> showLogoutDialog());
        }
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
        fetchProfileFromServer();
        populateProfileDataFromCache();
    }
}
