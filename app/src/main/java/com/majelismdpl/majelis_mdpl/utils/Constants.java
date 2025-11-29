package com.majelismdpl.majelis_mdpl.utils;

public class Constants {

    // ========== API CONFIGURATION ==========
    public static String getBaseUrl() {
        return ApiConfig.getBaseUrl();
    }

    // === SharedPreferences keys ===
    public static final String PREF_NAME = "LoginPrefs";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_ROLE = "role";
    public static final String KEY_USER_DATA = "user_data";
    public static final String KEY_PROFILE_PHOTO_URI = "KEY_PROFILE_PHOTO_URI";

    // === App constants ===
    public static final String PROJECT_NAME = "Majelis MDPL";
    public static final int CONNECTION_TIMEOUT = 30;

    // === API endpoints ===
    public static final String LOGIN_ENDPOINT = "login-api.php";
    public static final String DASHBOARD_ENDPOINT = "dashboard-api.php";
    public static final String REGISTER_ENDPOINT = "registrasi-api.php";
    public static final String GOOGLE_OAUTH_ENDPOINT = "google-oauth-android.php";
    public static final String GET_USER_TRIP_ENDPOINT = "backend/mobile/get-user-trip.php";
    public static final String GET_PROFILE_ENDPOINT = "mobile/get-profile.php";
    public static final String EDIT_PROFILE_ENDPOINT = "mobile/edit-profile.php";
    public static final String GET_MEETING_POINT_ENDPOINT = "mobile/get-meeting-point.php";
    public static final String GET_TRIP_PARTICIPANTS_ENDPOINT = "mobile/get-trip-participants.php";
    public static final String GET_USER_TRIPS_ENDPOINT = "mobile/get-user-trips.php";
    public static final String GET_TRIP_DOKUMENTASI_ENDPOINT = "mobile/get-trip-dokumentasi.php";

    // === Helper methods ===
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

    public static String getUserTripUrl() {
        String baseUrl = ApiConfig.getBaseUrl();
        if (!baseUrl.endsWith("/")) baseUrl += "/";
        return baseUrl + "mobile/get-user-trip.php";
    }

    public static String getMeetingPointUrl() {
        String baseUrl = getBaseUrl();
        if (!baseUrl.endsWith("/")) baseUrl += "/";
        return baseUrl + "mobile/get-meeting-point.php";
    }

    public static String getProfileUrl() {
        String baseUrl = getBaseUrl();
        if (!baseUrl.endsWith("/")) baseUrl += "/";
        return baseUrl + "mobile/get-profile.php";
    }

    public static String getTripParticipantsUrl() {
        String baseUrl = getBaseUrl();
        if (!baseUrl.endsWith("/")) baseUrl += "/";
        return baseUrl + "mobile/get-trip-participants.php";
    }

    public static String getUserTripsUrl() {
        String baseUrl = getBaseUrl();
        if (!baseUrl.endsWith("/")) baseUrl += "/";
        return baseUrl + "mobile/get-user-trips.php";
    }

    public static String getTripDokumentasiUrl() {
        String baseUrl = getBaseUrl();
        if (!baseUrl.endsWith("/")) baseUrl += "/";
        return baseUrl + "mobile/get-trip-dokumentasi.php";
    }

    public static String getEndpointUrl(String endpoint) {
        return getBaseUrl() + endpoint;
    }
}
