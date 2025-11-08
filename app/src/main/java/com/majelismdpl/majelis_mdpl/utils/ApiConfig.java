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

    // ╔════════════════════════════════════════╗
    // ║  🟢 UBAH DI SINI SAAT NGROK BERUBAH    ║
    // ╚════════════════════════════════════════╝

    // Environment: "LOCAL", "NGROK", "PRODUCTION"
    private static final String ENVIRONMENT = "NGROK";

    // URL Ngrok (ubah saat restart ngrok)
    private static final String NGROK_URL = "https://7bc0fc991943.ngrok-free.app";

    // Local IP (ubah jika berbeda)
    private static final String LOCAL_IP = "192.168.1.7";
    private static final String LOCAL_PROJECT = "majelismdpl.com";

    // Production domain
    private static final String PROD_DOMAIN = "majelismdpl.com";

    // ════════════════════════════════════════


    // Auto-generated URLs
    private static final String LOCAL_BASE = "http://" + LOCAL_IP + "/" + LOCAL_PROJECT + "/backend/";
    private static final String PROD_BASE = "https://" + PROD_DOMAIN + "/backend/";
    private static final String BOOTSTRAP_URL = NGROK_URL + "/backend/mobile/mobile-config-api.php";

    // Runtime variables
    private static String currentApiUrl = LOCAL_BASE;
    private static boolean isInitialized = false;

    // Constants
    private static final String PREFS_NAME = "ApiConfigPrefs";
    private static final String OAUTH_ENDPOINT = "google-oauth-android.php";
    public static final String PROJECT_NAME = "Majelis MDPL";


    /**
     * Initialize - Call di Application class
     */
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

    /**
     * Set static config
     */
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

    /**
     * Fetch config dari server
     */
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

    /**
     * Load dari cache
     */
    private static void loadFromCache(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        currentApiUrl = prefs.getString("api_url", LOCAL_BASE);
    }

    /**
     * Save ke cache
     */
    private static void saveToCache(Context context, String apiUrl) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString("api_url", apiUrl)
                .putLong("last_updated", System.currentTimeMillis())
                .apply();
    }


    // ════════════════════════════════════════
    // PUBLIC METHODS
    // ════════════════════════════════════════

    /**
     * Get base URL untuk API calls
     */
    public static String getBaseUrl() {
        return currentApiUrl;
    }

    /**
     * Get OAuth URL
     */
    public static String getOAuthUrl() {
        return currentApiUrl + OAUTH_ENDPOINT;
    }

    /**
     * Get OAuth callback URL
     */
    public static String getOAuthCallbackUrl() {
        return currentApiUrl + OAUTH_ENDPOINT;
    }

    /**
     * Get logging level
     */
    public static HttpLoggingInterceptor.Level getLogLevel() {
        return "PRODUCTION".equals(ENVIRONMENT)
                ? HttpLoggingInterceptor.Level.NONE
                : HttpLoggingInterceptor.Level.BODY;
    }

    /**
     * Is development?
     */
    public static boolean isDevelopment() {
        return !"PRODUCTION".equals(ENVIRONMENT);
    }

    /**
     * Is using ngrok?
     */
    public static boolean isUsingNgrok() {
        return "NGROK".equals(ENVIRONMENT);
    }

    /**
     * Is initialized?
     */
    public static boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Get environment name
     */
    public static String getEnvironment() {
        return ENVIRONMENT;
    }

    /**
     * Refresh config dari server
     */
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
