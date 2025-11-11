package com.majelismdpl.majelis_mdpl.api;

import com.majelismdpl.majelis_mdpl.models.GetUserTripResponse;
import com.majelismdpl.majelis_mdpl.models.LoginResponse;
import com.majelismdpl.majelis_mdpl.models.GoogleAuthResponse;
import com.majelismdpl.majelis_mdpl.models.Destination;
import com.majelismdpl.majelis_mdpl.models.Peserta;
import com.majelismdpl.majelis_mdpl.models.Trip;
import com.majelismdpl.majelis_mdpl.models.RegisterResponse;
import com.majelismdpl.majelis_mdpl.utils.Constants;
import com.majelismdpl.majelis_mdpl.models.MeetingPointResponse;

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
     * Register endpoint (OLD - Tidak dipakai lagi jika pakai OTP)
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

    // ========== REGISTER WITH OTP (NEW) ==========

    /**
     * Register Request OTP - Step 1
     * Kirim data registrasi dan request OTP via email
     */
    @FormUrlEncoded
    @POST("mobile/register-request-otp.php")
    Call<RegisterResponse> registerRequestOtp(
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email,
            @Field("no_wa") String noWa,
            @Field("alamat") String alamat
    );

    /**
     * Verify OTP - Step 2
     * Verifikasi OTP dan simpan user ke database
     */
    @FormUrlEncoded
    @POST("mobile/verify-otp.php")
    Call<RegisterResponse> verifyOtp(
            @Field("otp") String otp
    );

    /**
     * Resend OTP
     * Kirim ulang OTP ke email user
     */
    @FormUrlEncoded
    @POST("mobile/resend-otp.php")
    Call<RegisterResponse> resendOtp(
            @Field("email") String email
    );

    // ========== END REGISTER WITH OTP ==========

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

    // ========== GET USER TRIP (NEW ENDPOINT, PAKAI POST) ==========
    @FormUrlEncoded
    @POST("mobile/get-user-trip.php")
    Call<GetUserTripResponse> getUserTrip(
            @Field("id_user") int idUser
    );

    /**
     * Get Meeting Point User (PASTIKAN URL SAMA DENGAN BACKEND PHP)
     */
    @FormUrlEncoded
    @POST("mobile/get-meeting-point.php")
    Call<MeetingPointResponse> getUserMeetingPoint(
            @Field("id_user") int userId
    );

}
