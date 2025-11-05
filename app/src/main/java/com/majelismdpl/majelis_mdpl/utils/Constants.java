package com.majelismdpl.majelis_mdpl.utils;

public class Constants {
    // Menggunakan ApiConfig untuk consistency
    public static final String BASE_URL = ApiConfig.getBaseUrl();

    // === SharedPreferences keys ===
    public static final String PREF_NAME = "LoginPrefs";

    // Kunci untuk status login & role
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_ROLE = "role";

    // (PENTING) Kunci ini menyimpan seluruh data user (dalam format JSON)
    public static final String KEY_USER_DATA = "user_data";

    // (PENTING) Kunci ini menyimpan URI foto profil (sebagai String)
    // Ini sesuai dengan yang digunakan di SharedPrefManager
    public static final String KEY_PROFILE_PHOTO_URI = "KEY_PROFILE_PHOTO_URI";

    // --- Kunci-kunci lama (dihapus/diganti) ---
    // KEY_USERNAME, KEY_EMAIL, KEY_WHATSAPP, KEY_ADDRESS, KEY_PASSWORD
    // sekarang menjadi bagian dari objek JSON yang disimpan di KEY_USER_DATA.
    //
    // KEY_PROFILE_PHOTO diganti dengan KEY_PROFILE_PHOTO_URI agar lebih jelas
    // bahwa yang disimpan adalah String URI, bukan file gambar.
    // ---


    // === App constants ===
    public static final String PROJECT_NAME = "Majelis MDPL";
    public static final int CONNECTION_TIMEOUT = 30;

    // === API endpoints ===
    public static final String LOGIN_ENDPOINT = "login-api.php";
    public static final String DASHBOARD_ENDPOINT = "dashboard-api.php";
    public static final String REGISTER_ENDPOINT = "registrasi-api.php";
    public static final String GOOGLE_OAUTH_ENDPOINT = "google-oauth.php";
}