package com.majelismdpl.majelis_mdpl.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.models.User; // --- PENTING: Import model User ---
import com.majelismdpl.majelis_mdpl.utils.SharedPrefManager;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etEmail, etWhatsapp, etAlamat, etPassword;
    private MaterialButton btnSimpan, btnBatal;
    private ShapeableImageView ivProfileEdit;
    private TextView tvGantiFoto;
    private ActivityResultLauncher<String[]> pickImageLauncher;
    private SharedPrefManager prefManager;

    // --- PERUBAHAN LOGIKA ---
    // Kita tidak lagi menyimpan string individual.
    // Kita simpan satu objek User yang sedang diedit.
    private User currentUser;
    // -------------------------

    private boolean isFotoBerubah = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        prefManager = SharedPrefManager.getInstance(this);

        // Registrasi Activity Result Launcher untuk Ganti Foto (Sudah Benar)
        // Ini sekarang menggunakan prefManager.setProfilePhotoUri()
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
            if (uri != null) {
                try {
                    getContentResolver().takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );

                    ivProfileEdit.setImageURI(uri);

                    // Simpan URI foto secara terpisah
                    prefManager.setProfilePhotoUri(uri.toString());
                    isFotoBerubah = true;

                } catch (SecurityException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        // --- PERUBAHAN LOGIKA ---
        // Memuat data dari Objek User, bukan dari string individual
        loadAndPopulateUserData();

        // Memuat foto (logika ini tetap sama)
        loadProfileImage();

        // Set Listener untuk Tombol Simpan/Batal
        btnSimpan.setOnClickListener(v -> saveProfileIfChanged()); // <-- Logika di dalam ini diperbarui
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
     * --- LOGIKA BARU ---
     * Mengambil objek User dari SharedPreferences dan mengisinya ke field.
     */
    private void loadAndPopulateUserData() {
        // 1. Ambil OBJEK User dari SharedPreferences
        currentUser = prefManager.getUser();

        // 2. Jika user tidak ada (seharusnya tidak mungkin jika sudah login), tutup halaman
        if (currentUser == null) {
            Toast.makeText(this, "Error: Data user tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 3. Tampilkan data dari objek User
        // (Asumsi model User Anda memiliki getter: getUsername(), getEmail(), dll.)
        etUsername.setText(currentUser.getUsername());
        etEmail.setText(currentUser.getEmail());
        etWhatsapp.setText(currentUser.getWhatsapp());
        etAlamat.setText(currentUser.getAlamat());

        // Bidang password tetap kosong (Logika keamanan sudah benar)
    }

    /**
     * Memuat foto profil dari URI yang disimpan.
     * Logika ini sudah benar dan tidak perlu diubah.
     */
    private void loadProfileImage() {
        // Menggunakan getProfilePhotoUri() dari SharedPrefManager yang baru
        String savedUri = prefManager.getProfilePhotoUri();
        if (savedUri != null && !savedUri.isEmpty()) {
            try {
                ivProfileEdit.setImageURI(Uri.parse(savedUri));
            } catch (SecurityException | IllegalArgumentException e) {
                // Jika URI tidak valid atau izin dicabut, tampilkan default
                ivProfileEdit.setImageResource(R.drawable.ic_aplikasi_majelismdpl);
            }
        } else {
            // Tampilkan default jika tidak ada foto
            ivProfileEdit.setImageResource(R.drawable.ic_aplikasi_majelismdpl);
        }
    }

    /**
     * --- LOGIKA BARU ---
     * Mengecek perubahan terhadap objek 'currentUser',
     * lalu menyimpan seluruh objek 'currentUser' yang diperbarui.
     */
    private void saveProfileIfChanged() {
        // 1. Ambil semua data BARU dari EditText
        String newUsername = textOf(etUsername);
        String newEmail = textOf(etEmail);
        String newWhatsapp = textOf(etWhatsapp);
        String newAddress = textOf(etAlamat);
        String newPasswordInput = textOf(etPassword);

        // Validasi dasar
        if (TextUtils.isEmpty(newUsername)) {
            Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Bandingkan data BARU dengan data LAMA (dari objek currentUser)
        boolean isUsernameChanged = !newUsername.equals(currentUser.getUsername());
        boolean isEmailChanged = !newEmail.equals(currentUser.getEmail());
        boolean isWhatsappChanged = !newWhatsapp.equals(currentUser.getWhatsapp());
        boolean isAlamatChanged = !newAddress.equals(currentUser.getAlamat());

        // Password dianggap berubah HANYA JIKA user mengetik sesuatu yang baru
        boolean isPasswordChanged = !newPasswordInput.isEmpty();

        // 3. Cek apakah ada SALAH SATU data yang berubah (termasuk foto)
        boolean isDataBerubah = isUsernameChanged ||
                isEmailChanged ||
                isWhatsappChanged ||
                isAlamatChanged ||
                isPasswordChanged ||
                isFotoBerubah;

        if (isDataBerubah) {
            // 4. PERBARUI OBJEK currentUser secara lokal
            currentUser.setUsername(newUsername);
            currentUser.setEmail(newEmail);
            currentUser.setWhatsapp(newWhatsapp);
            currentUser.setAlamat(newAddress);

            if (isPasswordChanged) {
                // (Asumsi model User Anda punya setPassword())
                currentUser.setPassword(newPasswordInput);
            }

            // 5. SIMPAN SELURUH OBJEK ke SharedPreferences
            prefManager.saveUser(currentUser);

            // Foto sudah disimpan oleh launcher, jadi tidak perlu diapa-apakan lagi

            Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK); // Memberi tahu ProfileFragment untuk refresh
            finish();

        } else {
            // JIKA TIDAK ADA PERUBAHAN
            Toast.makeText(this, "data anda belum di perbarui", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Helper untuk mengambil teks dari EditText dengan aman.
     * (Sudah benar, tidak perlu diubah)
     */
    private String textOf(TextInputEditText editText) {
        if (editText == null || editText.getText() == null) {
            return ""; // Kembalikan string kosong, BUKAN null
        }
        return editText.getText().toString().trim();
    }
}