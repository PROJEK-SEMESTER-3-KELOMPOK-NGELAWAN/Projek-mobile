package com.majelismdpl.majelis_mdpl.api;

import com.majelismdpl.majelis_mdpl.utils.ApiConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = buildRetrofit();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitInstance() {
        return getClient();
    }

    public static Retrofit getInstance() {
        return getClient();
    }

    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }

    private static Retrofit buildRetrofit() {
        // Base URL dari ApiConfig, contoh: https://majelismdpl.my.id/
        String baseUrl = ApiConfig.getBaseUrl();
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        // Selalu minta JSON
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });

        if (ApiConfig.isDevelopment()) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(ApiConfig.getLogLevel());
            httpClient.addInterceptor(logging);
        }

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static void reset() {
        retrofit = null;
    }

    public static String getBaseUrl() {
        return ApiConfig.getBaseUrl();
    }
}
