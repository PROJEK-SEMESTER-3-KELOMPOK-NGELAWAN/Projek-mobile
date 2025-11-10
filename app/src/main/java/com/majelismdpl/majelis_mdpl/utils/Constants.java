package com.majelismdpl.majelis_mdpl.utils;

/**
 * ============================================
 * Application Constants
 * Sync dengan ApiConfig
 * ============================================
 */
public class Constants {

    // ========== API CONFIGURATION ==========
    // Menggunakan ApiConfig untuk dynamic URL
    public static String getBaseUrl() {
        return ApiConfig.getBaseUrl();
    }

    // === SharedPreferences keys ===
    public static final String PREF_NAME = "LoginPrefs";

    // Kunci untuk status login & role
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_ROLE = "role";

    // Kunci untuk data user (JSON format)
    public static final String KEY_USER_DATA = "user_data";

    // Kunci untuk foto profil (URI String)
    public static final String KEY_PROFILE_PHOTO_URI = "KEY_PROFILE_PHOTO_URI";

    // === App constants ===
    public static final String PROJECT_NAME = "Majelis MDPL";
    public static final int CONNECTION_TIMEOUT = 30;

    // === API endpoints (relative paths) ===
    public static final String LOGIN_ENDPOINT = "login-api.php";
    public static final String DASHBOARD_ENDPOINT = "dashboard-api.php";
    public static final String REGISTER_ENDPOINT = "registrasi-api.php";
    public static final String GOOGLE_OAUTH_ENDPOINT = "google-oauth-android.php";
    public static final String GET_USER_TRIP_ENDPOINT = "backend/mobile/get-user-trip.php";

    // === Helper methods untuk full URL ===
    public static String getLoginUrl() {
        return getBaseUrl() + LOGIN_ENDPOINT;
    }

    public static String getDashboardUrl() {
        return getBaseUrl() + DASHBOARD_ENDPOINT;
    }

    public static String getRegisterUrl() {
        return getBaseUrl() + REGISTER_ENDPOINT;
    }

    public static String getGoogleOAuthUrl() {
        return ApiConfig.getOAuthUrl();
    }

    //    UNTUK MENADAPTKAN URL mobile/get-user-trip.php
    public static String getUserTripUrl() {
        String baseUrl = ApiConfig.getBaseUrl();
        if (!baseUrl.endsWith("/")) baseUrl += "/";
        return baseUrl + "mobile/get-user-trip.php";
    }

    //    UNTUK MENADAPTKAN URL mobile/get-meeting-point.php
    public static String getMeetingPointUrl() {
        String baseUrl = getBaseUrl();
        if (!baseUrl.endsWith("/")) baseUrl += "/";
        return baseUrl + "mobile/get-meeting-point.php";
    }



    /**
     * Get full endpoint URL
     */
    public static String getEndpointUrl(String endpoint) {
        return getBaseUrl() + endpoint;
    }
}
