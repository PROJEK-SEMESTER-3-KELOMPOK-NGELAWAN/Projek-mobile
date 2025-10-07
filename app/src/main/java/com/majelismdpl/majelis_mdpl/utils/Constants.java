package com.majelismdpl.majelis_mdpl.utils;

public class Constants {
    // Menggunakan ApiConfig untuk consistency
    public static final String BASE_URL = ApiConfig.getBaseUrl();

    // SharedPreferences keys
    public static final String PREF_NAME = "LoginPrefs";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_ROLE = "role";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";

    // App constants
    public static final String PROJECT_NAME = "Majelis MDPL";
    public static final int CONNECTION_TIMEOUT = 30;

    // API endpoints sesuai nama file backend Anda
    public static final String LOGIN_ENDPOINT = "login-api.php";
    public static final String DASHBOARD_ENDPOINT = "dashboard-api.php";
    public static final String REGISTER_ENDPOINT = "registrasi-api.php";
    public static final String GOOGLE_OAUTH_ENDPOINT = "google-oauth.php";
}
