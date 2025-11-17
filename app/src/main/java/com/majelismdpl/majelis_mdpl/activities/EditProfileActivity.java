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
import android.widget.FrameLayout;
import android.widget.ProgressBar;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";

    private TextInputEditText etUsername, etEmail, etWhatsapp, etAlamat, etPassword;
    private MaterialButton btnSimpan, btnBatal;
    private ShapeableImageView ivProfileEdit;
    private TextView tvGantiFoto;
    private ActivityResultLauncher<String[]> pickImageLauncher;
    private SessionManager prefManager;

    private User currentUser;
    private boolean isFotoBerubah = false;
    private Uri selectedImageUri = null;

    // LOADER
    private FrameLayout loadingOverlay;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        prefManager = SessionManager.getInstance(this);

        // LOADER INIT
        loadingOverlay = findViewById(R.id.loadingOverlay);
        progressBar = findViewById(R.id.progressBar);

        // Registrasi Activity Result Launcher
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
                });

        // Hubungkan View
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etWhatsapp = findViewById(R.id.etWhatsapp);
        etAlamat = findViewById(R.id.etAlamat);
        etPassword = findViewById(R.id.etPassword);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnBatal = findViewById(R.id.btnBatal);
        ivProfileEdit = findViewById(R.id.ivProfileEdit);
        tvGantiFoto = findViewById(R.id.tvGantiFoto);

        loadAndPopulateUserData();
        loadProfileImage();

        btnSimpan.setOnClickListener(v -> saveProfileIfChanged());
        btnBatal.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        View.OnClickListener gantiFotoListener = v -> {
            pickImageLauncher.launch(new String[]{"image/*"});
        };
        ivProfileEdit.setOnClickListener(gantiFotoListener);
        tvGantiFoto.setOnClickListener(gantiFotoListener);
    }

    private void loadAndPopulateUserData() {
        currentUser = prefManager.getUser();

        if (currentUser == null) {
            Toast.makeText(this, "Error: Data user tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etUsername.setText(currentUser.getUsername());
        etEmail.setText(currentUser.getEmail());
        etWhatsapp.setText(currentUser.getWhatsapp());
        etAlamat.setText(currentUser.getAlamat());
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

    private void showLoading(boolean show) {
        if (loadingOverlay != null) {
            loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void saveProfileIfChanged() {
        String newUsername = textOf(etUsername);
        String newEmail = textOf(etEmail);
        String newWhatsapp = textOf(etWhatsapp);
        String newAddress = textOf(etAlamat);
        String newPasswordInput = textOf(etPassword);

        if (TextUtils.isEmpty(newUsername)) {
            Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUsernameChanged = !newUsername.equals(currentUser.getUsername());
        boolean isEmailChanged = !newEmail.equals(currentUser.getEmail());
        boolean isWhatsappChanged = !newWhatsapp.equals(currentUser.getWhatsapp());
        boolean isAlamatChanged = !newAddress.equals(currentUser.getAlamat());
        boolean isPasswordChanged = !newPasswordInput.isEmpty();

        boolean isDataBerubah = isUsernameChanged ||
                isEmailChanged ||
                isWhatsappChanged ||
                isAlamatChanged ||
                isPasswordChanged ||
                isFotoBerubah;

        if (isDataBerubah) {
            currentUser.setUsername(newUsername);
            currentUser.setEmail(newEmail);
            currentUser.setWhatsapp(newWhatsapp);
            currentUser.setAlamat(newAddress);

            if (isPasswordChanged) {
                currentUser.setPassword(newPasswordInput);
            }

            prefManager.updateUser(currentUser);

            // LOADER --> Mulai loading!
            showLoading(true);

            if (isFotoBerubah && selectedImageUri != null) {
                uploadProfileWithPhoto(newUsername, newEmail, newWhatsapp, newAddress, newPasswordInput);
            } else {
                updateProfileToServer(newUsername, newEmail, newWhatsapp, newAddress, newPasswordInput);
            }

        } else {
            Toast.makeText(this, "Data anda belum diperbarui", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadProfileWithPhoto(String username, String email, String whatsapp,
                                        String alamat, String password) {
        try {
            File file = getFileFromUri(selectedImageUri);
            if (file == null) {
                Toast.makeText(this, "Gagal membaca file foto", Toast.LENGTH_SHORT).show();
                showLoading(false);
                return;
            }

            RequestBody requestFile = RequestBody.create(
                    MediaType.parse(getContentResolver().getType(selectedImageUri)),
                    file
            );

            MultipartBody.Part fotoPart = MultipartBody.Part.createFormData(
                    "foto_profil",
                    file.getName(),
                    requestFile
            );

            RequestBody idUserBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(currentUser.getIdUser()));
            RequestBody usernameBody = RequestBody.create(MediaType.parse("text/plain"), username);
            RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
            RequestBody whatsappBody = RequestBody.create(MediaType.parse("text/plain"), whatsapp);
            RequestBody alamatBody = RequestBody.create(MediaType.parse("text/plain"), alamat);
            RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password.isEmpty() ? "" : password);

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
                            prefManager.setProfilePhotoUri(selectedImageUri.toString());

                            Toast.makeText(EditProfileActivity.this,
                                    "Profil dan foto berhasil diperbarui",
                                    Toast.LENGTH_SHORT).show();

                            // Otomatis refresh: Finish dan kembali (ProfileFragment akan panggil fetchProfileFromServer)
                            setResult(Activity.RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(EditProfileActivity.this,
                                    body.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditProfileActivity.this,
                                "Gagal upload ke server",
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
                password.isEmpty() ? "" : password
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

                        // Otomatis refresh: Finish dan kembali (ProfileFragment akan panggil fetchProfileFromServer)
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
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

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
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private String textOf(TextInputEditText editText) {
        if (editText == null || editText.getText() == null) {
            return "";
        }
        return editText.getText().toString().trim();
    }
}
