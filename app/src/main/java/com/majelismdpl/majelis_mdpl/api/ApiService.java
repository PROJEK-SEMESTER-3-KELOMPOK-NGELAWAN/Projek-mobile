package com.majelismdpl.majelis_mdpl.api;

import com.majelismdpl.majelis_mdpl.models.LoginResponse;
import com.majelismdpl.majelis_mdpl.utils.Constants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST(Constants.LOGIN_ENDPOINT) // "login-api.php"
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );

    // Future endpoints bisa ditambahkan di sini
    // @GET(Constants.DASHBOARD_ENDPOINT)
    // Call<DashboardResponse> getDashboard();

    // @FormUrlEncoded
    // @POST(Constants.REGISTER_ENDPOINT)
    // Call<RegisterResponse> register(
    //     @Field("username") String username,
    //     @Field("password") String password
    // );
}
