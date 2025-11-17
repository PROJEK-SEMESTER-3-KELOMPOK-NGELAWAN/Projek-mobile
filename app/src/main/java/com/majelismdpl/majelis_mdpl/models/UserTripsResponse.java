package com.majelismdpl.majelis_mdpl.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UserTripsResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<TripItem> data;

    @SerializedName("total_trips")
    private int totalTrips;

    @SerializedName("id_user")
    private int idUser;

    @SerializedName("base_url")
    private String baseUrl; // Tambahan untuk debugging

    // Constructor
    public UserTripsResponse() {
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TripItem> getData() {
        return data;
    }

    public void setData(List<TripItem> data) {
        this.data = data;
    }

    public int getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(int totalTrips) {
        this.totalTrips = totalTrips;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    // Inner class untuk Trip Item
    public static class TripItem {
        @SerializedName("id_trip")
        private int idTrip;

        @SerializedName("nama_gunung")
        private String namaGunung;

        @SerializedName("jenis_trip")
        private String jenisTrip;

        @SerializedName("tanggal")
        private String tanggal;

        @SerializedName("durasi")
        private String durasi;

        @SerializedName("harga")
        private int harga;

        @SerializedName("slot")
        private int slot;

        @SerializedName("status")
        private String status;

        @SerializedName("gambar_url")
        private String gambarUrl;

        @SerializedName("gambar_file")
        private String gambarFile; // Tambahan untuk debugging

        @SerializedName("id_booking")
        private int idBooking;

        @SerializedName("booking_status")
        private String bookingStatus;

        @SerializedName("tanggal_booking")
        private String tanggalBooking;

        @SerializedName("jumlah_orang")
        private int jumlahOrang;

        @SerializedName("total_peserta")
        private int totalPeserta;

        // Constructor
        public TripItem() {
        }

        // Getters
        public int getIdTrip() { return idTrip; }
        public String getNamaGunung() { return namaGunung; }
        public String getJenisTrip() { return jenisTrip; }
        public String getTanggal() { return tanggal; }
        public String getDurasi() { return durasi; }
        public int getHarga() { return harga; }
        public int getSlot() { return slot; }
        public String getStatus() { return status; }
        public String getGambarUrl() { return gambarUrl != null ? gambarUrl : ""; }
        public String getGambarFile() { return gambarFile; }
        public int getIdBooking() { return idBooking; }
        public String getBookingStatus() { return bookingStatus; }
        public String getTanggalBooking() { return tanggalBooking; }
        public int getJumlahOrang() { return jumlahOrang; }
        public int getTotalPeserta() { return totalPeserta; }

        // Setters
        public void setIdTrip(int idTrip) { this.idTrip = idTrip; }
        public void setNamaGunung(String namaGunung) { this.namaGunung = namaGunung; }
        public void setJenisTrip(String jenisTrip) { this.jenisTrip = jenisTrip; }
        public void setTanggal(String tanggal) { this.tanggal = tanggal; }
        public void setDurasi(String durasi) { this.durasi = durasi; }
        public void setHarga(int harga) { this.harga = harga; }
        public void setSlot(int slot) { this.slot = slot; }
        public void setStatus(String status) { this.status = status; }
        public void setGambarUrl(String gambarUrl) { this.gambarUrl = gambarUrl; }
        public void setGambarFile(String gambarFile) { this.gambarFile = gambarFile; }
        public void setIdBooking(int idBooking) { this.idBooking = idBooking; }
        public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
        public void setTanggalBooking(String tanggalBooking) { this.tanggalBooking = tanggalBooking; }
        public void setJumlahOrang(int jumlahOrang) { this.jumlahOrang = jumlahOrang; }
        public void setTotalPeserta(int totalPeserta) { this.totalPeserta = totalPeserta; }
    }
}
