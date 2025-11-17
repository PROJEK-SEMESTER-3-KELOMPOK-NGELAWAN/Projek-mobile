package com.majelismdpl.majelis_mdpl.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.majelismdpl.majelis_mdpl.database.UserManager;
import com.majelismdpl.majelis_mdpl.models.LoginResponse;
import com.majelismdpl.majelis_mdpl.models.User;

/**
 * ============================================
 * SessionManager (Pengatur Sesi Login)
 * Fungsi: Cek status login, simpan role, logout
 * Sekarang menggunakan SQLite via UserManager
 * ============================================
 */
public class SessionManager {
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
     * Simpan data login (User ke SQLite + Role ke SharedPref)
     */
    public void saveLoginResponse(LoginResponse loginResponse) {
        if (loginResponse.isSuccess() && loginResponse.getUser() != null) {
            userManager.saveLoggedInUser(loginResponse.getUser());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
            editor.putString(Constants.KEY_ROLE, loginResponse.getRole());
            editor.apply();
        }
    }

    /**
     * Ambil user yang sedang login (dari SQLite)
     */
    public User getUser() {
        return userManager.getLoggedInUser();
    }

    /**
     * Cek status login
     */
    public boolean isLoggedIn() {
        return userManager.isUserLoggedIn();
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
        userManager.logoutUser();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, false);
        editor.apply();
    }

    /**
     * Update data user (ke SQLite)
     */
    public int updateUser(User user) {
        return userManager.updateUser(user);
    }

    // --- Metode untuk Foto Profil (Disimpan di SharedPref) ---

    public void setProfilePhotoUri(String uriString) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (uriString != null) {
            editor.putString(Constants.KEY_PROFILE_PHOTO_URI, uriString);
        } else {
            editor.remove(Constants.KEY_PROFILE_PHOTO_URI);
        }
        editor.apply();
    }

    public String getProfilePhotoUri() {
        return sharedPreferences.getString(Constants.KEY_PROFILE_PHOTO_URI, null);
    }
}
