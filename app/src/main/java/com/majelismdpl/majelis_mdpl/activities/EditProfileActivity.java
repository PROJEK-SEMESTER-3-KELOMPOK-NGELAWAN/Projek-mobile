package com.majelismdpl.majelis_mdpl.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// PENTING: Import untuk Insets Handling
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;
import com.google.android.material.appbar.AppBarLayout; // Import AppBarLayout

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.api.ApiClient;
import com.majelismdpl.majelis_mdpl.api.ApiService;
import com.majelismdpl.majelis_mdpl.models.EditProfileRequest;
import com.majelismdpl.majelis_mdpl.models.RegisterResponse;
import com.majelismdpl.majelis_mdpl.models.User;
import com.majelismdpl.majelis_mdpl.utils.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB

    private TextInputEditText etUsername, etWhatsapp, etAlamat;
    private MaterialButton btnSimpan, btnBatal, btnChangePassword;
    private ShapeableImageView ivProfileEdit;
    private TextView tvGantiFoto;
    private ActivityResultLauncher<String[]> pickImageLauncher;
    private SessionManager prefManager;

    private User currentUser;
    private boolean isFotoBerubah = false;
    private Uri selectedImageUri = null;

    private FrameLayout loadingOverlay;
    private ProgressBar progressBar;

    public static final String EXTRA_PASSWORD_CHANGE_MODE = "password_change_mode";
    public static final int MODE_PROFILE_CHANGE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_edit_profile);

        // =======================================================
        // KODE PERBAIKAN STATUS BAR (Edge-to-Edge Insets Handling)
        // =======================================================

        // 1. Aktifkan Edge-to-Edge: Izinkan konten menggambar di belakang status/navigation bar
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // 2. Dapatkan View yang diperlukan
        View rootView = findViewById(android.R.id.content).getRootView();
        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);

        // --- A. Penanganan Root View (Untuk Navigation Bar) ---
        // Tujuan: Terapkan padding BOTTOM untuk Navigation Bar, tapi TOP dibuat 0
        // agar header menempel ke atas.
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Terapkan padding BOTTOM untuk Navigation Bar, TOP dibuat 0
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);

            // Jangan konsumsi insets, agar AppBarLayout bisa menerima insets TOP
            return windowInsets;
        });

        // --- B. Penanganan AppBarLayout (Untuk Status Bar) ---
        // Tujuan: Terapkan padding TOP untuk Status Bar, sehingga konten (Toolbar)
        // didorong ke bawah area status bar, namun latar belakang AppBarLayout tetap full.
        ViewCompat.setOnApplyWindowInsetsListener(appBarLayout, (v, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Terapkan padding TOP untuk Status Bar, padding lain diabaikan
            v.setPadding(0, systemBars.top, 0, 0);

            // Konsumsi insets
            return WindowInsetsCompat.CONSUMED;
        });
        // =======================================================
        // AKHIR KODE PERBAIKAN STATUS BAR
        // =======================================================


        prefManager = SessionManager.getInstance(this);

        loadingOverlay = findViewById(R.id.loadingOverlay);
        progressBar = findViewById(R.id.progressBar);

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        try {
                            getContentResolver().takePersistableUriPermission(
                                    uri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            );
                            ivProfileEdit.setImageURI(uri);
                            selectedImageUri = uri;
                            isFotoBerubah = true;
                            Log.d(TAG, "Foto dipilih: " + uri.toString());
                        } catch (SecurityException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        etUsername = findViewById(R.id.etUsername);
        etWhatsapp = findViewById(R.id.etWhatsapp);
        etAlamat = findViewById(R.id.etAlamat);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnBatal = findViewById(R.id.btnBatal);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        ivProfileEdit = findViewById(R.id.ivProfileEdit);
        tvGantiFoto = findViewById(R.id.tvGantiFoto);

        loadAndPopulateUserData();
        loadProfileImage();

        btnSimpan.setOnClickListener(v -> saveProfileIfChanged());
        btnBatal.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        if (btnChangePassword != null) {
            btnChangePassword.setOnClickListener(v -> navigateToPasswordChangeFlow());
        }

        View.OnClickListener gantiFotoListener = v -> pickImageLauncher.launch(new String[]{"image/*"});
        ivProfileEdit.setOnClickListener(gantiFotoListener);
        tvGantiFoto.setOnClickListener(gantiFotoListener);
    }

    private void navigateToPasswordChangeFlow() {
        Intent intent = new Intent(EditProfileActivity.this, ResetPasswordActivity.class);
        intent.putExtra(EXTRA_PASSWORD_CHANGE_MODE, MODE_PROFILE_CHANGE);
        startActivity(intent);
    }

    private void loadAndPopulateUserData() {
        currentUser = prefManager.getUser();
        if (currentUser == null) {
            Toast.makeText(this, "Error: Data user tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        etUsername.setText(currentUser.getUsername());
        etWhatsapp.setText(currentUser.getWhatsapp());
        etAlamat.setText(currentUser.getAlamat());
    }

    private void loadProfileImage() {
        String savedUri = prefManager.getProfilePhotoUri();
        if (savedUri != null && !savedUri.isEmpty()) {
            try {
                Uri uri = Uri.parse(savedUri);
                getContentResolver().takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
                ivProfileEdit.setImageURI(uri);
            } catch (SecurityException | IllegalArgumentException e) {
                Log.e(TAG, "Error loading saved profile image: " + e.getMessage());
                ivProfileEdit.setImageResource(R.drawable.ic_aplikasi_majelismdpl);
                prefManager.setProfilePhotoUri(null);
            }
        } else {
            ivProfileEdit.setImageResource(R.drawable.ic_aplikasi_majelismdpl);
        }
    }

    private void showLoading(boolean show) {
        if (loadingOverlay != null) {
            loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void saveProfileIfChanged() {
        String newUsername = textOf(etUsername);
        String newEmail = currentUser.getEmail();
        String newWhatsapp = textOf(etWhatsapp);
        String newAddress = textOf(etAlamat);

        if (TextUtils.isEmpty(newUsername)) {
            Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUsernameChanged = !newUsername.equals(currentUser.getUsername());
        boolean isWhatsappChanged = !newWhatsapp.equals(currentUser.getWhatsapp());
        boolean isAlamatChanged = !newAddress.equals(currentUser.getAlamat());
        boolean isFotoBerubahLocal = this.isFotoBerubah;

        boolean isDataBerubah = isUsernameChanged || isWhatsappChanged || isAlamatChanged || isFotoBerubahLocal;

        if (!isDataBerubah) {
            Toast.makeText(this, "Data anda belum diperbarui", Toast.LENGTH_LONG).show();
            return;
        }

        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        currentUser.setWhatsapp(newWhatsapp);
        currentUser.setAlamat(newAddress);
        prefManager.updateUser(currentUser);

        showLoading(true);
        btnSimpan.setEnabled(false);
        btnSimpan.setText("Menyimpan...");

        String emptyPassword = "";

        if (isFotoBerubahLocal && selectedImageUri != null) {
            uploadProfileWithPhoto(newUsername, newEmail, newWhatsapp, newAddress, emptyPassword);
        } else {
            updateProfileToServer(newUsername, newEmail, newWhatsapp, newAddress, emptyPassword);
        }
    }

    private void uploadProfileWithPhoto(String username, String email, String whatsapp,
                                        String alamat, String password) {
        try {
            if (selectedImageUri == null) {
                Toast.makeText(this, "Foto belum dipilih", Toast.LENGTH_SHORT).show();
                showLoading(false);
                return;
            }

            File file = getFileFromUri(selectedImageUri);
            if (file == null) {
                Toast.makeText(this, "Gagal membaca file foto", Toast.LENGTH_SHORT).show();
                showLoading(false);
                return;
            }

            if (file.length() > MAX_FILE_SIZE) {
                Toast.makeText(this, "Ukuran foto maksimal 2 MB", Toast.LENGTH_SHORT).show();
                showLoading(false);
                return;
            }

            String mimeType = getContentResolver().getType(selectedImageUri);
            if (mimeType == null) mimeType = "image/jpeg";

            RequestBody requestFile = RequestBody.create(
                    MediaType.parse(mimeType),
                    file
            );

            MultipartBody.Part fotoPart = MultipartBody.Part.createFormData(
                    "foto_profil",
                    file.getName(),
                    requestFile
            );

            RequestBody idUserBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    String.valueOf(currentUser.getIdUser())
            );
            RequestBody usernameBody = RequestBody.create(MediaType.parse("text/plain"), username);
            RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
            RequestBody whatsappBody = RequestBody.create(MediaType.parse("text/plain"), whatsapp);
            RequestBody alamatBody = RequestBody.create(MediaType.parse("text/plain"), alamat);
            RequestBody passwordBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    password == null ? "" : password
            );

            ApiService apiService = ApiClient.getApiService();
            Call<RegisterResponse> call = apiService.editProfileWithPhoto(
                    idUserBody,
                    usernameBody,
                    emailBody,
                    whatsappBody,
                    alamatBody,
                    passwordBody,
                    fotoPart
            );

            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    showLoading(false);
                    btnSimpan.setEnabled(true);
                    btnSimpan.setText("Simpan");

                    if (response.isSuccessful() && response.body() != null) {
                        RegisterResponse body = response.body();
                        if (body.isSuccess()) {
                            if (selectedImageUri != null) {
                                prefManager.setProfilePhotoUri(selectedImageUri.toString());
                            }
                            Toast.makeText(EditProfileActivity.this,
                                    "Profil dan foto berhasil diperbarui",
                                    Toast.LENGTH_SHORT).show();
                            setResult(Activity.RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(EditProfileActivity.this,
                                    body.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ResponseBody errorBody = response.errorBody();
                        String err = "";
                        try {
                            err = errorBody != null ? errorBody.string() : "null";
                        } catch (Exception ignored) {}
                        Log.e(TAG, "Upload gagal. code=" + response.code() + " body=" + err);
                        Toast.makeText(EditProfileActivity.this,
                                "Gagal upload ke server (" + response.code() + ")",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    showLoading(false);
                    btnSimpan.setEnabled(true);
                    btnSimpan.setText("Simpan");
                    Log.e(TAG, "Upload error: " + t.getMessage(), t);
                    Toast.makeText(EditProfileActivity.this,
                            "Error: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            showLoading(false);
            btnSimpan.setEnabled(true);
            btnSimpan.setText("Simpan");
            Log.e(TAG, "Error preparing upload: " + e.getMessage(), e);
            Toast.makeText(this, "Gagal memproses foto", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfileToServer(String username, String email, String whatsapp,
                                       String alamat, String password) {
        ApiService apiService = ApiClient.getApiService();

        EditProfileRequest request = new EditProfileRequest(
                currentUser.getIdUser(),
                username,
                email,
                whatsapp,
                alamat,
                password == null ? "" : password
        );

        Call<RegisterResponse> call = apiService.editProfile(request);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                showLoading(false);
                btnSimpan.setEnabled(true);
                btnSimpan.setText("Simpan");

                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse body = response.body();
                    if (body.isSuccess()) {
                        Toast.makeText(EditProfileActivity.this,
                                "Profil berhasil diperbarui",
                                Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(EditProfileActivity.this,
                                body.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this,
                            "Gagal update ke server",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                showLoading(false);
                btnSimpan.setEnabled(true);
                btnSimpan.setText("Simpan");
                Toast.makeText(EditProfileActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private File getFileFromUri(Uri uri) {
        try {
            String fileName = getFileName(uri);
            File file = new File(getCacheDir(), fileName);

            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                return null;
            }
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            return file;
        } catch (Exception e) {
            Log.e(TAG, "Error converting URI to File: " + e.getMessage(), e);
            return null;
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                }
            } finally {
                if (cursor != null) cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            if (result == null) result = "image_" + System.currentTimeMillis();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
            if (!result.contains(".")) {
                result += ".jpg";
            }
        }
        return result;
    }

    private String textOf(TextInputEditText editText) {
        if (editText == null || editText.getText() == null) return "";
        return editText.getText().toString().trim();
    }
}