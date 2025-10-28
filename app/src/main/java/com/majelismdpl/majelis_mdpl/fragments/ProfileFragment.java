package com.majelismdpl.majelis_mdpl.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.activities.LoginActivity;
import com.majelismdpl.majelis_mdpl.activities.EditProfileActivity;
import com.majelismdpl.majelis_mdpl.utils.SharedPrefManager;
import androidx.appcompat.app.AlertDialog;

public class ProfileFragment extends Fragment {

    private TextInputEditText etUsername, etPassword, etWhatsapp, etEmail, etAlamat;
    private Button btnEditProfile, btnLogout;
    private ImageView ivProfile;
    private TextView tvHeaderName, tvHeaderEmail;
    private ActivityResultLauncher<String[]> pickImageLauncher;
    private boolean isEditing = false;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
            if (uri != null) {
                try {
                    if (getContext() != null) {
                        requireContext().getContentResolver().takePersistableUriPermission(
                                uri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );
                    }
                    if (ivProfile != null) {
                        ivProfile.setImageURI(uri);
                    }
                    if (getContext() != null) {
                        SharedPrefManager.getInstance(getContext()).setProfilePhotoUri(uri.toString());
                    }
                } catch (SecurityException | IllegalArgumentException ignored) {
                    // Abaikan jika tidak bisa menampilkan/menyimpan URI
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

        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        etWhatsapp = view.findViewById(R.id.etWhatsapp);
        etEmail = view.findViewById(R.id.etEmail);
        etAlamat = view.findViewById(R.id.etAlamat);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        ivProfile = view.findViewById(R.id.ivProfile);
        tvHeaderName = view.findViewById(R.id.tvHeaderName);
        tvHeaderEmail = view.findViewById(R.id.tvHeaderEmail);

        // Nonaktifkan input saat awal
        setEditing(false);

        // Load saved profile photo if available
        if (getContext() != null && ivProfile != null) {
            String savedUri = SharedPrefManager.getInstance(getContext()).getProfilePhotoUri();
            if (savedUri != null && !savedUri.isEmpty()) {
                try {
                    ivProfile.setImageURI(Uri.parse(savedUri));
                } catch (SecurityException | IllegalArgumentException ignored) {
                    // Abaikan jika URI tidak valid/izin ditolak
                }
            }
        }

        // Tap to change photo
        if (ivProfile != null) {
            ivProfile.setOnClickListener(v -> {
                if (pickImageLauncher != null) {
                    pickImageLauncher.launch(new String[]{"image/*"});
                }
            });
        }

        populateProfile();

        btnEditProfile.setOnClickListener(v -> {
            if (getActivity() == null) return;
            Intent intent = new Intent(requireActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            if (getActivity() == null) return;

            new AlertDialog.Builder(requireContext())
                    .setTitle("Logout")
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
    }

    @Override
    public void onResume() {
        super.onResume();
        populateProfile();
    }

    private void populateProfile() {
        if (getContext() == null) return;
        SharedPrefManager pref = SharedPrefManager.getInstance(getContext());
        String username = pref.getUsername();
        String email = pref.getEmail();
        String whatsapp = pref.getWhatsapp();
        String address = pref.getAddress();
        String password = pref.getPassword();

        if (etUsername != null) {
            etUsername.setText(nonNull(username, ""));
        }
        if (etEmail != null) {
            etEmail.setText(nonNull(email, ""));
        }
        if (etWhatsapp != null) {
            etWhatsapp.setText(nonNull(whatsapp, ""));
        }
        if (etAlamat != null) {
            etAlamat.setText(nonNull(address, ""));
        }
        if (etPassword != null) {
            etPassword.setText(nonNull(password, ""));
        }

        if (tvHeaderName != null) {
            tvHeaderName.setText(nonNull(username, ""));
        }
        if (tvHeaderEmail != null) {
            tvHeaderEmail.setText(nonNull(email, ""));
        }
    }

    private void setEditing(boolean editing) {
        isEditing = editing;
        if (etUsername != null) etUsername.setEnabled(editing);
        if (etPassword != null) etPassword.setEnabled(editing);
        if (etWhatsapp != null) etWhatsapp.setEnabled(editing);
        if (etEmail != null) etEmail.setEnabled(editing);
        if (etAlamat != null) etAlamat.setEnabled(editing);
        if (btnEditProfile != null) {
            btnEditProfile.setText(editing ? "Simpan" : "Edit Profil");
        }
    }

    private String nonNull(String value, String fallback) {
        return value == null || value.isEmpty() ? fallback : value;
    }
}
