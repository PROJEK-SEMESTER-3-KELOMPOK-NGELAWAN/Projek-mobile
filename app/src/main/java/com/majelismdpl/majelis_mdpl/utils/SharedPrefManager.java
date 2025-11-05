package com.majelismdpl.majelis_mdpl.utils;

import android.content.Context;
import android.content.SharedPreferences;

// (PENTING) Pastikan import ini ada
import com.google.gson.Gson;
import com.majelismdpl.majelis_mdpl.models.LoginResponse;
import com.majelismdpl.majelis_mdpl.models.User; // Pastikan import User ada

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = Constants.PREF_NAME;
    private static SharedPrefManager instance;
    private final SharedPreferences sharedPreferences;
    private final Gson gson; // Tambahkan Gson

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson(); // Inisialisasi Gson
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Menyimpan data lengkap saat login (User + Role + Status).
     * Dipanggil oleh LoginActivity.
     */
    public void saveLoginResponse(LoginResponse loginResponse) {
        if (loginResponse.isSuccess() && loginResponse.getUser() != null) {
            // Simpan objek User-nya
            saveUser(loginResponse.getUser());

            // Simpan data tambahan
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
            editor.putString(Constants.KEY_ROLE, loginResponse.getRole());
            editor.apply();
        }
    }

    /**
     * (BARU) Menyimpan HANYA objek User.
     * Ini akan dipanggil oleh EditProfileActivity saat memperbarui profil.
     */
    public void saveUser(User user) {
        if (user == null) return;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userJson = gson.toJson(user);
        editor.putString(Constants.KEY_USER_DATA, userJson);
        editor.apply();
    }

    /**
     * Mengambil objek User yang sedang login.
     * Ini dipanggil oleh ProfileFragment DAN EditProfileActivity.
     */
    public User getUser() {
        String userJson = sharedPreferences.getString(Constants.KEY_USER_DATA, null);
        if (userJson != null) {
            return gson.fromJson(userJson, User.class); // Ubah String kembali ke objek User
        }
        return null; // Kembalikan null jika tidak ada data user
    }

    /**
     * Cek status login, dipanggil oleh SplashActivity.
     */
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }

    /**
     * Mengambil Role (jika diperlukan).
     */
    public String getRole() {
        return sharedPreferences.getString(Constants.KEY_ROLE, null);
    }

    /**
     * Menghapus semua data sesi saat logout.
     */
    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Hapus semua data
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, false); // Pastikan status logout
        editor.apply();
    }

    // --- Metode untuk Foto Profil (Disimpan Terpisah) ---

    /**
     * Menyimpan URI foto profil yang dipilih.
     * Dipanggil oleh EditProfileActivity.
     */
    public void setProfilePhotoUri(String uriString) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (uriString != null) {
            editor.putString(Constants.KEY_PROFILE_PHOTO_URI, uriString);
        } else {
            editor.remove(Constants.KEY_PROFILE_PHOTO_URI);
        }
        editor.apply();
    }

    /**
     * Mengambil URI foto profil yang tersimpan.
     * Dipanggil oleh EditProfileActivity dan ProfileFragment.
     */
    public String getProfilePhotoUri() {
        return sharedPreferences.getString(Constants.KEY_PROFILE_PHOTO_URI, null);
    }

    // --- SEMUA METODE LAMA (saveProfile, getUsername, getEmail, dll.) TELAH DIHAPUS ---
}