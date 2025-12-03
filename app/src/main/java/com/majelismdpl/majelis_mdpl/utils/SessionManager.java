package com.majelismdpl.majelis_mdpl.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.majelismdpl.majelis_mdpl.database.UserManager;
import com.majelismdpl.majelis_mdpl.models.LoginResponse;
import com.majelismdpl.majelis_mdpl.models.User;

import org.json.JSONObject;

/**
 * ============================================
 * SessionManager (Pengatur Sesi Login)
 * Fungsi: Cek status login, simpan role, logout
 * Sekarang menggunakan SQLite via UserManager
 * UPDATED: Menambahkan penyimpanan id_user & user_data
 * FIXED: Ganti getFotoProfil() dengan getFotoUrl()
 * ============================================
 */
public class SessionManager {
    private static final String TAG = "SessionManager";
    private static final String SHARED_PREF_NAME = Constants.PREF_NAME;
    private static SessionManager instance;
    private final SharedPreferences sharedPreferences;
    private final UserManager userManager;

    private SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userManager = new UserManager(context);
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * UPDATED: Simpan data login (User ke SQLite + Role + id_user + user_data ke SharedPref)
     */
    public void saveLoginResponse(LoginResponse loginResponse) {
        if (loginResponse.isSuccess() && loginResponse.getUser() != null) {
            User user = loginResponse.getUser();

            // Simpan ke SQLite (existing code)
            userManager.saveLoggedInUser(user);

            // Simpan ke SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
            editor.putString(Constants.KEY_ROLE, loginResponse.getRole());

            // ========== TAMBAHAN BARU: Simpan id_user ==========
            editor.putInt("id_user", user.getIdUser());

            // ========== TAMBAHAN BARU: Simpan user_data sebagai JSON ==========
            try {
                JSONObject userJson = new JSONObject();
                userJson.put("id_user", user.getIdUser());
                userJson.put("username", user.getUsername() != null ? user.getUsername() : "");
                userJson.put("email", user.getEmail() != null ? user.getEmail() : "");
                userJson.put("role", user.getRole() != null ? user.getRole() : "user");
                userJson.put("whatsapp", user.getWhatsapp() != null ? user.getWhatsapp() : "");
                userJson.put("alamat", user.getAlamat() != null ? user.getAlamat() : "");
                userJson.put("foto_url", user.getFotoUrl() != null ? user.getFotoUrl() : ""); // ← FIXED

                editor.putString(Constants.KEY_USER_DATA, userJson.toString());

                Log.d(TAG, "✅ User data JSON saved: " + userJson.toString());
            } catch (Exception e) {
                Log.e(TAG, "❌ Error creating user JSON: " + e.getMessage());
                e.printStackTrace();
            }

            // Commit perubahan
            editor.apply();

            // Log untuk debugging
            Log.d(TAG, "✅ Login data saved successfully!");
            Log.d(TAG, "User ID: " + user.getIdUser());
            Log.d(TAG, "Username: " + user.getUsername());
            Log.d(TAG, "Role: " + loginResponse.getRole());

            // Verifikasi data tersimpan
            logAllSavedData();
        } else {
            Log.e(TAG, "❌ LoginResponse tidak valid atau User null");
        }
    }

    /**
     * Ambil user yang sedang login (dari SQLite)
     */
    public User getUser() {
        return userManager.getLoggedInUser();
    }

    /**
     * NEW: Ambil User ID langsung dari SharedPreferences
     */
    public int getUserId() {
        // Coba ambil dari SharedPreferences dulu (lebih cepat)
        int userId = sharedPreferences.getInt("id_user", 0);

        if (userId > 0) {
            return userId;
        }

        // Fallback: ambil dari SQLite jika tidak ada di SharedPref
        User user = getUser();
        if (user != null) {
            return user.getIdUser();
        }

        Log.e(TAG, "❌ User ID tidak ditemukan!");
        return 0;
    }

    /**
     * NEW: Ambil user data sebagai JSON string
     */
    public String getUserDataJson() {
        return sharedPreferences.getString(Constants.KEY_USER_DATA, "");
    }

    /**
     * Cek status login
     */
    public boolean isLoggedIn() {
        boolean isLoggedInSharedPref = sharedPreferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
        boolean isLoggedInSQLite = userManager.isUserLoggedIn();
        int userId = getUserId();

        Log.d(TAG, "Login check - SharedPref: " + isLoggedInSharedPref +
                ", SQLite: " + isLoggedInSQLite +
                ", UserID: " + userId);

        return isLoggedInSharedPref && isLoggedInSQLite && userId > 0;
    }

    /**
     * Ambil role user
     */
    public String getRole() {
        return sharedPreferences.getString(Constants.KEY_ROLE, null);
    }

    /**
     * Logout user (hapus dari SQLite + SharedPref)
     */
    public void logout() {
        Log.d(TAG, "Logging out user...");

        // Hapus dari SQLite
        userManager.logoutUser();

        // Hapus dari SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, false);
        editor.apply();

        Log.d(TAG, "✅ User logged out successfully");
    }

    /**
     * Update data user (ke SQLite)
     */
    public int updateUser(User user) {
        int result = userManager.updateUser(user);

        // Update juga di SharedPreferences jika update berhasil
        if (result > 0 && user != null) {
            try {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Update id_user (jika berubah)
                editor.putInt("id_user", user.getIdUser());

                // Update user_data JSON
                JSONObject userJson = new JSONObject();
                userJson.put("id_user", user.getIdUser());
                userJson.put("username", user.getUsername() != null ? user.getUsername() : "");
                userJson.put("email", user.getEmail() != null ? user.getEmail() : "");
                userJson.put("role", user.getRole() != null ? user.getRole() : "user");
                userJson.put("whatsapp", user.getWhatsapp() != null ? user.getWhatsapp() : "");
                userJson.put("alamat", user.getAlamat() != null ? user.getAlamat() : "");
                userJson.put("foto_url", user.getFotoUrl() != null ? user.getFotoUrl() : ""); // ← FIXED

                editor.putString(Constants.KEY_USER_DATA, userJson.toString());
                editor.apply();

                Log.d(TAG, "✅ User data updated in SharedPreferences");
            } catch (Exception e) {
                Log.e(TAG, "Error updating user data in SharedPreferences: " + e.getMessage());
            }
        }

        return result;
    }

    // --- Metode untuk Foto Profil (Disimpan di SharedPref) ---

    /**
     * Alias untuk setProfilePhotoUri, digunakan di ProfileFragment.
     */
    public void saveProfilePhotoUri(String uriString) {
        setProfilePhotoUri(uriString);
    }

    public void setProfilePhotoUri(String uriString) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (uriString != null) {
            editor.putString(Constants.KEY_PROFILE_PHOTO_URI, uriString);

            // Update juga di user_data JSON
            try {
                String userDataJson = sharedPreferences.getString(Constants.KEY_USER_DATA, "");
                if (!userDataJson.isEmpty()) {
                    JSONObject userJson = new JSONObject(userDataJson);
                    userJson.put("foto_url", uriString); // ← FIXED
                    editor.putString(Constants.KEY_USER_DATA, userJson.toString());
                }
            } catch (Exception e) {
                Log.e(TAG, "Error updating foto_url in JSON: " + e.getMessage());
            }
        } else {
            editor.remove(Constants.KEY_PROFILE_PHOTO_URI);
        }
        editor.apply();
    }

    public String getProfilePhotoUri() {
        return sharedPreferences.getString(Constants.KEY_PROFILE_PHOTO_URI, null);
    }

    /**
     * NEW: Debug method - Log semua data yang tersimpan
     */
    public void logAllSavedData() {
        Log.d(TAG, "========== SAVED DATA IN SHAREDPREFERENCES ==========");
        Log.d(TAG, "All Keys: " + sharedPreferences.getAll().keySet().toString());
        Log.d(TAG, "is_logged_in: " + sharedPreferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false));
        Log.d(TAG, "id_user: " + sharedPreferences.getInt("id_user", -1));
        Log.d(TAG, "role: " + sharedPreferences.getString(Constants.KEY_ROLE, "NOT FOUND"));
        Log.d(TAG, "user_data: " + sharedPreferences.getString(Constants.KEY_USER_DATA, "NOT FOUND"));

        // Log data dari SQLite
        User user = getUser();
        if (user != null) {
            Log.d(TAG, "SQLite User - ID: " + user.getIdUser() + ", Username: " + user.getUsername());
        } else {
            Log.d(TAG, "SQLite User: NOT FOUND");
        }
        Log.d(TAG, "=====================================================");
    }
}