package com.majelismdpl.majelis_mdpl.api;

import com.majelismdpl.majelis_mdpl.models.GetUserTripResponse;
import com.majelismdpl.majelis_mdpl.models.LoginResponse;
import com.majelismdpl.majelis_mdpl.models.GoogleAuthResponse;
import com.majelismdpl.majelis_mdpl.models.Destination;
import com.majelismdpl.majelis_mdpl.models.Peserta;
import com.majelismdpl.majelis_mdpl.models.Trip;
import com.majelismdpl.majelis_mdpl.models.RegisterResponse;
import com.majelismdpl.majelis_mdpl.models.ProfileResponse;
import com.majelismdpl.majelis_mdpl.models.EditProfileRequest;
import com.majelismdpl.majelis_mdpl.models.TripHistoryResponse;
import com.majelismdpl.majelis_mdpl.models.TripParticipantsHistoryResponse;
import com.majelismdpl.majelis_mdpl.utils.Constants;
import com.majelismdpl.majelis_mdpl.models.MeetingPointResponse;
import com.majelismdpl.majelis_mdpl.models.TripParticipantsResponse;
import com.majelismdpl.majelis_mdpl.models.UserTripsResponse;
import com.majelismdpl.majelis_mdpl.models.TripDokumentasiResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import com.google.gson.annotations.SerializedName;

public interface ApiService {

    // ========== AUTHENTICATION ==========

    @FormUrlEncoded
    @POST(Constants.LOGIN_ENDPOINT)
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(Constants.REGISTER_ENDPOINT)
    Call<LoginResponse> register(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password,
            @Field("whatsapp") String whatsapp,
            @Field("alamat") String alamat
    );

    // ========== REGISTER WITH OTP ==========

    @FormUrlEncoded
    @POST("mobile/register-request-otp.php")
    Call<RegisterResponse> registerRequestOtp(
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email,
            @Field("no_wa") String noWa,
            @Field("alamat") String alamat
    );

    @FormUrlEncoded
    @POST("mobile/verify-otp.php")
    Call<RegisterResponse> verifyOtp(@Field("otp") String otp);

    @FormUrlEncoded
    @POST("mobile/resend-otp.php")
    Call<RegisterResponse> resendOtp(@Field("email") String email);

    // ========== GOOGLE OAUTH ==========

    @FormUrlEncoded
    @POST(Constants.GOOGLE_OAUTH_ENDPOINT)
    Call<GoogleAuthResponse> googleAuth(@Field("id_token") String idToken);

    // ========== PROFILE ==========

    /**
     * Get User Profile (POST dengan JSON body)
     */
    @POST(Constants.GET_PROFILE_ENDPOINT)
    Call<ProfileResponse> getProfile(@Body ProfileRequest request);

    /**
     * Edit Profile (JSON tanpa foto)
     */
    @POST(Constants.EDIT_PROFILE_ENDPOINT)
    Call<RegisterResponse> editProfile(@Body EditProfileRequest request);

    /**
     * Edit Profile dengan Upload Foto (Multipart)
     */
    @Multipart
    @POST(Constants.EDIT_PROFILE_ENDPOINT)
    Call<RegisterResponse> editProfileWithPhoto(
            @Part("id_user") RequestBody idUser,
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("whatsapp") RequestBody whatsapp,
            @Part("alamat") RequestBody alamat,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part fotoProfil
    );

    // ========== DASHBOARD ==========

    @GET(Constants.DASHBOARD_ENDPOINT)
    Call<LoginResponse> getDashboard();

    // ========== TRIPS ==========

    @GET("trip-api.php")
    Call<List<Trip>> getTrips();

    @FormUrlEncoded
    @POST("trip-api.php")
    Call<Trip> getTripDetail(@Field("id_trip") int tripId);

    @FormUrlEncoded
    @POST("mobile/get-user-trip.php")
    Call<GetUserTripResponse> getUserTrip(@Field("id_user") int idUser);

    /**
     * Get User Trips (BARU - untuk pemilihan trip)
     */
    @FormUrlEncoded
    @POST(Constants.GET_USER_TRIPS_ENDPOINT)
    Call<UserTripsResponse> getUserTrips(@Field("id_user") int idUser);

    // ========== TRIP PARTICIPANTS ==========

    /**
     * Get Trip Participants by Trip ID
     */
    @FormUrlEncoded
    @POST(Constants.GET_TRIP_PARTICIPANTS_ENDPOINT)
    Call<TripParticipantsResponse> getTripParticipants(@Field("id_trip") String tripId);

    // ========== TRIP DOKUMENTASI (UPDATE - Kirim id_user bukan id_trip) ==========

    /**
     * Get Trip Dokumentasi by User ID
     * Hanya menampilkan dokumentasi trip yang sudah dibeli user dan berstatus DONE
     */
    @FormUrlEncoded
    @POST(Constants.GET_TRIP_DOKUMENTASI_ENDPOINT)
    Call<TripDokumentasiResponse> getTripDokumentasi(@Field("id_user") int idUser);

    // ========== BOOKINGS ==========

    @FormUrlEncoded
    @POST("booking-api.php")
    Call<LoginResponse> createBooking(
            @Field("id_trip") int tripId,
            @Field("jumlah_peserta") int jumlahPeserta
    );

    @GET("booking-api.php")
    Call<List<Peserta>> getUserBookings();

    // ========== PAYMENTS ==========

    @FormUrlEncoded
    @POST("payment-api.php")
    Call<LoginResponse> getPaymentStatus(@Field("booking_id") int bookingId);

    // ========== DESTINATIONS ==========

    @GET("destination-api.php")
    Call<List<Destination>> getDestinations();

    // ========== MEETING POINT ==========

    @FormUrlEncoded
    @POST(Constants.GET_MEETING_POINT_ENDPOINT)
    Call<MeetingPointResponse> getUserMeetingPoint(@Field("id_user") int userId);


    // ========== TRIP HISTORY DENGAN STATUS (untuk HistoryFragment) ==========
    @FormUrlEncoded
    @POST("mobile/get-user-trip-history.php")
    Call<TripHistoryResponse> getUserTripHistory(@Field("id_user") int idUser);

    @FormUrlEncoded
    @POST("mobile/get-trip-participants-history.php")
    Call<TripParticipantsHistoryResponse> getTripParticipantsHistory(@Field("id_trip") int idTrip);




    // ========== PROFILE REQUEST MODEL ==========

    class ProfileRequest {
        @SerializedName("id_user")
        private int idUser;

        public ProfileRequest(int idUser) {
            this.idUser = idUser;
        }

        public int getIdUser() {
            return idUser;
        }

        public void setIdUser(int idUser) {
            this.idUser = idUser;
        }
    }
}
