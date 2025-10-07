package com.majelismdpl.majelis_mdpl.utils;

import okhttp3.logging.HttpLoggingInterceptor;

public class ApiConfig {
    // Set true untuk development, false untuk production
    private static final boolean IS_DEVELOPMENT = true;

    // ========== NGROK CONFIGURATION ==========
    // 🟢 UBAH INI UNTUK MENGGUNAKAN/TIDAK MENGGUNAKAN NGROK
    // true = pakai ngrok (untuk Google OAuth tanpa error)
    // false = pakai IP lokal (192.168.1.6)
    private static final boolean USE_NGROK = true; // ← UBAH JADI false JIKA TIDAK MAU PAKAI NGROK

    // 🟡 GANTI URL INI SETIAP KALI RESTART NGROK (karena URL berubah di free plan)
    // Copy URL dari terminal ngrok: https://xxxxx.ngrok-free.app
    private static final String NGROK_URL = "https://fa89b607a136.ngrok-free.app"; // ← GANTI DENGAN NGROK URL KAMU

    // ========== LOCAL CONFIGURATION ==========
    // 🔵 UBAH IP INI JIKA SERVER LARAGON DI IP BERBEDA
    private static final String DEV_SERVER_IP = "192.168.1.7"; // ← GANTI JIKA IP LARAGON BERBEDA
    private static final String DEV_PROJECT_FOLDER = "majelismdpl.com"; // ← GANTI SESUAI NAMA FOLDER PROJECT
    private static final String DEV_BACKEND_FOLDER = "backend"; // ← GANTI SESUAI NAMA FOLDER BACKEND

    // ========== PRODUCTION CONFIGURATION ==========
    // 🟠 UBAH INI SAAT DEPLOY KE HOSTING/PRODUCTION
    private static final String PROD_DOMAIN = "yourdomain.com"; // ← GANTI DENGAN DOMAIN PRODUCTION KAMU
    private static final String PROD_API_PATH = "api"; // ← GANTI SESUAI STRUKTUR FOLDER PRODUCTION

    // ========== AUTO-GENERATED URLS (JANGAN DIUBAH) ==========
    private static final String DEV_BASE_URL_LOCAL = "http://" + DEV_SERVER_IP + "/" + DEV_PROJECT_FOLDER + "/" + DEV_BACKEND_FOLDER + "/";
    private static final String DEV_BASE_URL_NGROK = NGROK_URL + "/" + DEV_PROJECT_FOLDER + "/" + DEV_BACKEND_FOLDER + "/";
    private static final String PROD_BASE_URL = "https://" + PROD_DOMAIN + "/" + PROD_API_PATH + "/";

    // OAuth endpoints (jangan diubah kecuali nama file PHP berubah)
    private static final String OAUTH_ENDPOINT = "google-oauth-android.php"; // ← GANTI JIKA NAMA FILE PHP BERUBAH

    public static String getBaseUrl() {
        if (IS_DEVELOPMENT) {
            // Jika development, pilih ngrok atau lokal
            return USE_NGROK ? DEV_BASE_URL_NGROK : DEV_BASE_URL_LOCAL;
        } else {
            // Production
            return PROD_BASE_URL;
        }
    }

    // Method untuk mendapatkan OAuth URL (khusus Android)
    public static String getOAuthUrl() {
        return getBaseUrl() + OAUTH_ENDPOINT;
    }

    // Method untuk mendapatkan OAuth Callback URL
    public static String getOAuthCallbackUrl() {
        return getBaseUrl() + OAUTH_ENDPOINT;
    }

    public static HttpLoggingInterceptor.Level getLogLevel() {
        return IS_DEVELOPMENT ?
                HttpLoggingInterceptor.Level.BODY :
                HttpLoggingInterceptor.Level.NONE;
    }

    public static boolean isDevelopment() {
        return IS_DEVELOPMENT;
    }

    public static boolean isUsingNgrok() {
        return USE_NGROK;
    }

    // Constants
    public static final String PROJECT_NAME = "Majelis MDPL";
    public static final String CUSTOM_SCHEME = "majelismdpl";

    // Method untuk mendapatkan server info (untuk debugging)
    public static String getServerInfo() {
        if (IS_DEVELOPMENT) {
            return USE_NGROK ? "NGROK: " + NGROK_URL : "LOCAL: " + DEV_SERVER_IP;
        } else {
            return "PRODUCTION: " + PROD_DOMAIN;
        }
    }

    // Method untuk debugging - print semua URL
//    public static void printAllUrls() {
//        System.out.println("=== API CONFIG DEBUG ===");
//        System.out.println("Environment: " + (IS_DEVELOPMENT ? "DEVELOPMENT" : "PRODUCTION"));
//        System.out.println("Using ngrok: " + (USE_NGROK && IS_DEVELOPMENT));
//        System.out.println("Server Info: " + getServerInfo());
//        System.out.println("Base URL: " + getBaseUrl());
//        System.out.println("OAuth URL: " + getOAuthUrl());
//        System.out.println("OAuth Callback URL: " + getOAuthCallbackUrl());
//        System.out.println("========================");
//    }
}
