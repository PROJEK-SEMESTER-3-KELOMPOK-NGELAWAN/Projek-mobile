package com.majelismdpl.majelis_mdpl.api;

import com.majelismdpl.majelis_mdpl.utils.ApiConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

/**
 * ============================================
 * Retrofit API Client
 * Auto-sync dengan ApiConfig
 * ============================================
 */
public class ApiClient {

    private static Retrofit retrofit = null;

    /**
     * Get Retrofit instance dengan dynamic base URL
     */
    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = buildRetrofit();
        }
        return retrofit.create(ApiService.class);
    }

    /**
     * Build Retrofit instance
     */
    private static Retrofit buildRetrofit() {
        // Get base URL dari ApiConfig (dynamic)
        String baseUrl = ApiConfig.getBaseUrl();

        // ========== FIX: TAMBAHKAN "/" DI AKHIR ==========
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }

        // OkHttp client dengan logging (jika development)
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        // Add logging interceptor untuk development
        if (ApiConfig.isDevelopment()) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(ApiConfig.getLogLevel());
            httpClient.addInterceptor(logging);
        }

        // Build Retrofit
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Reset Retrofit instance (untuk refresh base URL)
     */
    public static void reset() {
        retrofit = null;
    }

    /**
     * Get base URL (dari ApiConfig)
     */
    public static String getBaseUrl() {
        return ApiConfig.getBaseUrl();
    }
}
