package com.majelismdpl.majelis_mdpl.api;

import com.majelismdpl.majelis_mdpl.models.LoginResponse;
import com.majelismdpl.majelis_mdpl.models.GoogleAuthResponse;
import com.majelismdpl.majelis_mdpl.models.Destination;
import com.majelismdpl.majelis_mdpl.models.Peserta;
import com.majelismdpl.majelis_mdpl.models.Trip;
import com.majelismdpl.majelis_mdpl.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * ============================================
 * Retrofit API Service Interface
 * Endpoint definitions menggunakan Constants
 * ============================================
 */
public interface ApiService {

    // ========== AUTHENTICATION ==========

    /**
     * Login endpoint
     */
    @FormUrlEncoded
    @POST(Constants.LOGIN_ENDPOINT)
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );

    /**
     * Register endpoint
     */
    @FormUrlEncoded
    @POST(Constants.REGISTER_ENDPOINT)
    Call<LoginResponse> register(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password,
            @Field("whatsapp") String whatsapp,
            @Field("alamat") String alamat
    );

    /**
     * Google OAuth endpoint
     */
    @FormUrlEncoded
    @POST(Constants.GOOGLE_OAUTH_ENDPOINT)
    Call<GoogleAuthResponse> googleAuth(
            @Field("id_token") String idToken
    );


    // ========== DASHBOARD ==========

    /**
     * Dashboard endpoint
     */
    @GET(Constants.DASHBOARD_ENDPOINT)
    Call<LoginResponse> getDashboard();


    // ========== TRIPS (FUTURE ENDPOINTS) ==========

    /**
     * Get all trips
     */
    @GET("trip-api.php")
    Call<List<Trip>> getTrips();

    /**
     * Get trip detail
     */
    @FormUrlEncoded
    @POST("trip-api.php")
    Call<Trip> getTripDetail(@Field("id_trip") int tripId);


    // ========== BOOKINGS (FUTURE ENDPOINTS) ==========

    /**
     * Create booking
     */
    @FormUrlEncoded
    @POST("booking-api.php")
    Call<LoginResponse> createBooking(
            @Field("id_trip") int tripId,
            @Field("jumlah_peserta") int jumlahPeserta
    );

    /**
     * Get user bookings
     */
    @GET("booking-api.php")
    Call<List<Peserta>> getUserBookings();


    // ========== PAYMENTS (FUTURE ENDPOINTS) ==========

    /**
     * Get payment status
     */
    @FormUrlEncoded
    @POST("payment-api.php")
    Call<LoginResponse> getPaymentStatus(@Field("booking_id") int bookingId);


    // ========== DESTINATIONS (FUTURE ENDPOINTS) ==========

    /**
     * Get all destinations
     */
    @GET("destination-api.php")
    Call<List<Destination>> getDestinations();
}
