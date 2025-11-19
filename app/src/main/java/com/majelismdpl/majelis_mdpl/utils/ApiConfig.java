package com.majelismdpl.majelis_mdpl.utils;

import android.content.Context;
import android.content.SharedPreferences;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

/**
 * ============================================
 * API Configuration Manager
 * Simple & Clean - Auto sync dengan web
 * ============================================
 */
public class ApiConfig {

    private static final String ENVIRONMENT = "NGROK";
    private static final String NGROK_URL = "https://95efb84f6406.ngrok-free.app"; // UBAH INI SETIAP URL NGROK BERBEDA
    private static final String LOCAL_IP = "192.168.1.7";
    private static final String LOCAL_PROJECT = "majelismdpl.com";
    private static final String PROD_DOMAIN = "majelismdpl.com";

    private static final String LOCAL_BASE = "http://" + LOCAL_IP + "/" + LOCAL_PROJECT + "/backend/";
    private static final String PROD_BASE = "https://" + PROD_DOMAIN + "/backend/";
    private static final String BOOTSTRAP_URL = NGROK_URL + "/backend/mobile/mobile-config-api.php";

    private static String currentApiUrl = LOCAL_BASE;
    private static boolean isInitialized = false;

    private static final String PREFS_NAME = "ApiConfigPrefs";
    private static final String OAUTH_ENDPOINT = "google-oauth-android.php";
    public static final String PROJECT_NAME = "Majelis MDPL";

    public static void initialize(Context context) {
        loadFromCache(context);

        if (!"LOCAL".equals(ENVIRONMENT)) {
            new Thread(() -> {
                try {
                    fetchConfig(context);
                    isInitialized = true;
                } catch (Exception e) {
                    setStaticConfig();
                    isInitialized = true;
                }
            }).start();
        } else {
            setStaticConfig();
            isInitialized = true;
        }
    }

    private static void setStaticConfig() {
        switch (ENVIRONMENT) {
            case "LOCAL":
                currentApiUrl = LOCAL_BASE;
                break;
            case "PRODUCTION":
                currentApiUrl = PROD_BASE;
                break;
            case "NGROK":
                currentApiUrl = NGROK_URL + "/backend/";
                break;
        }
    }

    private static void fetchConfig(Context context) throws Exception {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(BOOTSTRAP_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JSONObject json = new JSONObject(response.body().string());
                if (json.getBoolean("success")) {
                    currentApiUrl = json.getString("api_url");
                    saveToCache(context, currentApiUrl);
                }
            }
        }
    }

    private static void loadFromCache(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        currentApiUrl = prefs.getString("api_url", LOCAL_BASE);
    }

    private static void saveToCache(Context context, String apiUrl) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString("api_url", apiUrl)
                .putLong("last_updated", System.currentTimeMillis())
                .apply();
    }

    /**
     * Ambil base URL (untuk API)
     */
    public static String getBaseUrl() {
        return currentApiUrl;
    }

    /**
     * UNTUK HOME FRAGMENT JIKA TIDAK ADA TRIP YANG AKTIF
     */
    public static String getNgrokWebUrl() {
        // Pastikan ngrok URL berakhiran tanpa slash
        String base = NGROK_URL.endsWith("/") ? NGROK_URL.substring(0, NGROK_URL.length() - 1) : NGROK_URL;
        // Kembalikan URL lengkap halaman web yang diinginkan
        return base + "/index.php#paketTrips";
    }


    public static String getOAuthUrl() {
        return currentApiUrl + OAUTH_ENDPOINT;
    }

    public static String getOAuthCallbackUrl() {
        return currentApiUrl + OAUTH_ENDPOINT;
    }

    public static HttpLoggingInterceptor.Level getLogLevel() {
        return "PRODUCTION".equals(ENVIRONMENT)
                ? HttpLoggingInterceptor.Level.NONE
                : HttpLoggingInterceptor.Level.BODY;
    }

    public static boolean isDevelopment() {
        return !"PRODUCTION".equals(ENVIRONMENT);
    }

    public static boolean isUsingNgrok() {
        return "NGROK".equals(ENVIRONMENT);
    }

    public static boolean isInitialized() {
        return isInitialized;
    }

    public static String getEnvironment() {
        return ENVIRONMENT;
    }

    public static void refresh(Context context) {
        if (!"LOCAL".equals(ENVIRONMENT)) {
            new Thread(() -> {
                try {
                    fetchConfig(context);
                } catch (Exception ignored) {}
            }).start();
        }
    }
}
