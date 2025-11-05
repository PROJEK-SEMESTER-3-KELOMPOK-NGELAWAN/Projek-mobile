package com.majelismdpl.majelis_mdpl.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

public class ApiClient {

    // TENTUKAN BASE_URL ANDA DI SINI
    // (Ini adalah IP dari screenshot Anda sebelumnya)
    private static final String BASE_URL = "http://192.168.1.30/majelismdpl.com/";

    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    // --- PERBAIKAN: INI ADALAH METODE YANG HILANG ---
    // (Tambahkan fungsi ini agar Constants.java bisa mengambil BASE_URL)
    public static String getBaseUrl() {
        return BASE_URL;
    }
}