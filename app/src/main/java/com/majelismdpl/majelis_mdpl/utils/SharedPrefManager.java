package com.majelismdpl.majelis_mdpl.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = Constants.PREF_NAME;
    private static SharedPrefManager instance;
    private final SharedPreferences sharedPreferences;

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveLoginData(String username, String role) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_USERNAME, username);
        editor.putString(Constants.KEY_ROLE, role);
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }

    public String getUsername() {
        return sharedPreferences.getString(Constants.KEY_USERNAME, null);
    }

    public void setUsername(String username) {
        sharedPreferences.edit().putString(Constants.KEY_USERNAME, username).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(Constants.KEY_EMAIL, null);
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString(Constants.KEY_EMAIL, email).apply();
    }

    public String getWhatsapp() {
        return sharedPreferences.getString(Constants.KEY_WHATSAPP, null);
    }

    public void setWhatsapp(String whatsapp) {
        sharedPreferences.edit().putString(Constants.KEY_WHATSAPP, whatsapp).apply();
    }

    public String getAddress() {
        return sharedPreferences.getString(Constants.KEY_ADDRESS, null);
    }

    public void setAddress(String address) {
        sharedPreferences.edit().putString(Constants.KEY_ADDRESS, address).apply();
    }

    public String getPassword() {
        return sharedPreferences.getString(Constants.KEY_PASSWORD, null);
    }

    public void setPassword(String password) {
        sharedPreferences.edit().putString(Constants.KEY_PASSWORD, password).apply();
    }

    public String getProfilePhotoUri() {
        return sharedPreferences.getString(Constants.KEY_PROFILE_PHOTO, null);
    }

    public void setProfilePhotoUri(String uri) {
        sharedPreferences.edit().putString(Constants.KEY_PROFILE_PHOTO, uri).apply();
    }

    public String getRole() {
        return sharedPreferences.getString(Constants.KEY_ROLE, null);
    }

    public void saveProfile(String username, String email, String whatsapp, String address, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (username != null) editor.putString(Constants.KEY_USERNAME, username);
        if (email != null) editor.putString(Constants.KEY_EMAIL, email);
        if (whatsapp != null) editor.putString(Constants.KEY_WHATSAPP, whatsapp);
        if (address != null) editor.putString(Constants.KEY_ADDRESS, address);
        if (password != null) editor.putString(Constants.KEY_PASSWORD, password);
        editor.apply();
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
