package com.majelismdpl.majelis_mdpl.activities;

import android.app.Activity;
import android.content.Intent; // Import untuk Intent
import android.net.Uri; // Import untuk Uri
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View; // Import untuk View
import android.widget.TextView; // Import untuk TextView
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher; // Import untuk Launcher
import androidx.activity.result.contract.ActivityResultContracts; // Import untuk Launcher Contract
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView; // Import untuk ImageView
import com.google.android.material.textfield.TextInputEditText;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.utils.SharedPrefManager;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etEmail, etWhatsapp, etAlamat, etPassword;
    private MaterialButton btnSimpan, btnBatal;

    // --- Variabel Baru dari Logika Ganti Foto ---
    private ShapeableImageView ivProfileEdit;
    private TextView tvGantiFoto;
    private ActivityResultLauncher<String[]> pickImageLauncher;
    private SharedPrefManager prefManager; // Dibuat jadi variabel global
    // -------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Inisialisasi prefManager di atas
        prefManager = SharedPrefManager.getInstance(this);

        // --- BARU: Registrasi Activity Result Launcher untuk Ganti Foto ---
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
            if (uri != null) {
                try {
                    // Ambil izin persisten untuk URI
                    getContentResolver().takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );

                    // Tampilkan gambar baru di ImageView
                    ivProfileEdit.setImageURI(uri);

                    // Langsung simpan URI ke SharedPref
                    prefManager.setProfilePhotoUri(uri.toString());

                } catch (SecurityException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // -----------------------------------------------------------------

        // Menghubungkan View (Text)
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etWhatsapp = findViewById(R.id.etWhatsapp);
        etAlamat = findViewById(R.id.etAlamat);
        etPassword = findViewById(R.id.etPassword);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnBatal = findViewById(R.id.btnBatal);

        // --- BARU: Menghubungkan View (Foto) ---
        ivProfileEdit = findViewById(R.id.ivProfileEdit);
        tvGantiFoto = findViewById(R.id.tvGantiFoto);
        // -------------------------------------

        // Memuat data yang ada (termasuk foto)
        populateFields();
        loadProfileImage(); // BARU

        // Set Listener untuk Tombol Simpan/Batal
        btnSimpan.setOnClickListener(v -> saveProfile());
        btnBatal.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        // --- BARU: Listener untuk Ganti Foto ---
        View.OnClickListener gantiFotoListener = v -> {
            // Meluncurkan pemilih gambar dari galeri
            pickImageLauncher.launch(new String[]{"image/*"});
        };
        ivProfileEdit.setOnClickListener(gantiFotoListener);
        tvGantiFoto.setOnClickListener(gantiFotoListener);
        // ----------------------------------------
    }

    private void populateFields() {
        // Menggunakan prefManager global
        if (etUsername != null) etUsername.setText(prefManager.getUsername());
        if (etEmail != null) etEmail.setText(prefManager.getEmail());
        if (etWhatsapp != null) etWhatsapp.setText(prefManager.getWhatsapp());
        if (etAlamat != null) etAlamat.setText(prefManager.getAddress());
        if (etPassword != null) etPassword.setText(prefManager.getPassword());
    }

    // --- BARU: Fungsi untuk memuat foto profil yang ada ---
    private void loadProfileImage() {
        String savedUri = prefManager.getProfilePhotoUri();
        if (savedUri != null && !savedUri.isEmpty()) {
            try {
                ivProfileEdit.setImageURI(Uri.parse(savedUri));
            } catch (SecurityException | IllegalArgumentException e) {
                // Jika URI tidak valid, set gambar default
                ivProfileEdit.setImageResource(R.drawable.ic_aplikasi_majelismdpl);
            }
        } else {
            // Set gambar default jika tidak ada URI
            ivProfileEdit.setImageResource(R.drawable.ic_aplikasi_majelismdpl);
        }
    }
    // ----------------------------------------------------

    private void saveProfile() {
        String username = textOf(etUsername);
        String email = textOf(etEmail);
        String whatsapp = textOf(etWhatsapp);
        String address = textOf(etAlamat);
        String password = textOf(etPassword);

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        // Menggunakan prefManager global
        // (Foto sudah disimpan saat dipilih, jadi ini hanya simpan data teks)
        prefManager.saveProfile(username, email, whatsapp, address, password);

        Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();

        setResult(Activity.RESULT_OK); // Memberi tahu ProfileFragment untuk refresh
        finish();
    }

    private String textOf(TextInputEditText editText) {
        return editText == null || editText.getText() == null ? null : editText.getText().toString().trim();
    }
}