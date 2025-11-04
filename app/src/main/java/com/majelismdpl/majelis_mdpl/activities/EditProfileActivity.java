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
    private ShapeableImageView ivProfileEdit;
    private TextView tvGantiFoto;
    private ActivityResultLauncher<String[]> pickImageLauncher;
    private SharedPrefManager prefManager;

    // --- BARU: Variabel untuk menyimpan data LAMA ---
    private String originalUsername;
    private String originalEmail;
    private String originalWhatsapp;
    private String originalAlamat;
    private String originalPassword;
    private boolean isFotoBerubah = false;
    // ---------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        prefManager = SharedPrefManager.getInstance(this);

        // --- BARU: Registrasi Activity Result Launcher untuk Ganti Foto ---
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
            if (uri != null) {
                try {
                    getContentResolver().takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );

                    ivProfileEdit.setImageURI(uri);
                    prefManager.setProfilePhotoUri(uri.toString());

                    // --- BARU: Tandai bahwa foto telah berubah ---
                    isFotoBerubah = true;
                    // -------------------------------------------

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

        // Menghubungkan View (Foto)
        ivProfileEdit = findViewById(R.id.ivProfileEdit);
        tvGantiFoto = findViewById(R.id.tvGantiFoto);

        // --- DIPERBARUI: Memuat data dan MENYIMPAN data original ---
        populateFieldsAndStoreOriginals();
        loadProfileImage();

        // Set Listener untuk Tombol Simpan/Batal
        btnSimpan.setOnClickListener(v -> saveProfileIfChanged()); // Diubah ke fungsi baru
        btnBatal.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        // Listener untuk Ganti Foto
        View.OnClickListener gantiFotoListener = v -> {
            pickImageLauncher.launch(new String[]{"image/*"});
        };
        ivProfileEdit.setOnClickListener(gantiFotoListener);
        tvGantiFoto.setOnClickListener(gantiFotoListener);
    }

    /**
     * DIPERBARUI: Fungsi ini sekarang juga menyimpan data asli ke variabel global
     * sebelum menampilkannya di EditText.
     */
    private void populateFieldsAndStoreOriginals() {
        // 1. Ambil data dari SharedPreferences
        originalUsername = prefManager.getUsername();
        originalEmail = prefManager.getEmail();
        originalWhatsapp = prefManager.getWhatsapp();
        originalAlamat = prefManager.getAddress();
        originalPassword = prefManager.getPassword();

        // 2. Lakukan null check untuk keamanan
        if (originalUsername == null) originalUsername = "";
        if (originalEmail == null) originalEmail = "";
        if (originalWhatsapp == null) originalWhatsapp = "";
        if (originalAlamat == null) originalAlamat = "";
        if (originalPassword == null) originalPassword = "";

        // 3. Tampilkan data di EditText
        if (etUsername != null) etUsername.setText(originalUsername);
        if (etEmail != null) etEmail.setText(originalEmail);
        if (etWhatsapp != null) etWhatsapp.setText(originalWhatsapp);
        if (etAlamat != null) etAlamat.setText(originalAlamat);
        if (etPassword != null) etPassword.setText(originalPassword);
    }

    private void loadProfileImage() {
        String savedUri = prefManager.getProfilePhotoUri();
        if (savedUri != null && !savedUri.isEmpty()) {
            try {
                ivProfileEdit.setImageURI(Uri.parse(savedUri));
            } catch (SecurityException | IllegalArgumentException e) {
                ivProfileEdit.setImageResource(R.drawable.ic_aplikasi_majelismdpl);
            }
        } else {
            ivProfileEdit.setImageResource(R.drawable.ic_aplikasi_majelismdpl);
        }
    }

    /**
     * FUNGSI BARU: Ini adalah inti dari logika yang Anda minta.
     * Fungsi ini akan mengecek perubahan sebelum memanggil saveProfile().
     */
    private void saveProfileIfChanged() {
        // 1. Ambil semua data BARU dari EditText
        String newUsername = textOf(etUsername);
        String newEmail = textOf(etEmail);
        String newWhatsapp = textOf(etWhatsapp);
        String newAddress = textOf(etAlamat);
        String newPassword = textOf(etPassword);

        // Validasi dasar (Username tidak boleh kosong)
        if (TextUtils.isEmpty(newUsername)) {
            Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Bandingkan data BARU dengan data LAMA
        boolean isUsernameChanged = !newUsername.equals(originalUsername);
        boolean isEmailChanged = !newEmail.equals(originalEmail);
        boolean isWhatsappChanged = !newWhatsapp.equals(originalWhatsapp);
        boolean isAlamatChanged = !newAddress.equals(originalAlamat);
        boolean isPasswordChanged = !newPassword.equals(originalPassword);

        // 3. Cek apakah ada SALAH SATU data yang berubah (termasuk foto)
        boolean isDataBerubah = isUsernameChanged ||
                isEmailChanged ||
                isWhatsappChanged ||
                isAlamatChanged ||
                isPasswordChanged ||
                isFotoBerubah;

        // 4. Terapkan Logika
        if (isDataBerubah) {
            // JIKA ADA PERUBAHAN, simpan data
            // (Foto sudah disimpan saat dipilih, jadi ini hanya simpan data teks)
            prefManager.saveProfile(newUsername, newEmail, newWhatsapp, newAddress, newPassword);
            Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK); // Memberi tahu ProfileFragment untuk refresh
            finish();

        } else {
            // JIKA TIDAK ADA PERUBAHAN
            // Tampilkan pop-up sesuai permintaan Anda
            Toast.makeText(this, "data anda belum di perbarui", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * DIPERBARUI: Dibuat lebih aman untuk menghindari NullPointerException.
     * Sekarang mengembalikan string kosong ("") jika null.
     */
    private String textOf(TextInputEditText editText) {
        if (editText == null || editText.getText() == null) {
            return ""; // Kembalikan string kosong, BUKAN null
        }
        return editText.getText().toString().trim();
    }
}
