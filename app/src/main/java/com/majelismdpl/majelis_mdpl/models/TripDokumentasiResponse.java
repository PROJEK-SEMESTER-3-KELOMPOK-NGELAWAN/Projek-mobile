package com.majelismdpl.majelis_mdpl.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TripDokumentasiResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    @SerializedName("count")
    private int count;

    @SerializedName("data")
    private List<DokumentasiData> data;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<DokumentasiData> getData() {
        return data;
    }

    public void setData(List<DokumentasiData> data) {
        this.data = data;
    }

    public static class DokumentasiData {
        @SerializedName("id_trip")
        private int idTrip;

        @SerializedName("id_booking")
        private int idBooking;

        @SerializedName("galery_name")
        private String galeryName;

        @SerializedName("gdrive_link")
        private String gdriveLink;

        @SerializedName("nama_gunung")
        private String namaGunung;

        @SerializedName("tanggal")
        private String tanggal;

        @SerializedName("durasi")
        private String durasi;

        @SerializedName("jenis_trip")
        private String jenisTrip;

        @SerializedName("status")
        private String status;

        @SerializedName("gambar")
        private String gambar;

        @SerializedName("booking_status")
        private String bookingStatus;

        // Getters and Setters
        public int getIdTrip() {
            return idTrip;
        }

        public void setIdTrip(int idTrip) {
            this.idTrip = idTrip;
        }

        public int getIdBooking() {
            return idBooking;
        }

        public void setIdBooking(int idBooking) {
            this.idBooking = idBooking;
        }

        public String getGaleryName() {
            return galeryName;
        }

        public void setGaleryName(String galeryName) {
            this.galeryName = galeryName;
        }

        public String getGdriveLink() {
            return gdriveLink;
        }

        public void setGdriveLink(String gdriveLink) {
            this.gdriveLink = gdriveLink;
        }

        public String getNamaGunung() {
            return namaGunung;
        }

        public void setNamaGunung(String namaGunung) {
            this.namaGunung = namaGunung;
        }

        public String getTanggal() {
            return tanggal;
        }

        public void setTanggal(String tanggal) {
            this.tanggal = tanggal;
        }

        public String getDurasi() {
            return durasi;
        }

        public void setDurasi(String durasi) {
            this.durasi = durasi;
        }

        public String getJenisTrip() {
            return jenisTrip;
        }

        public void setJenisTrip(String jenisTrip) {
            this.jenisTrip = jenisTrip;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getGambar() {
            return gambar;
        }

        public void setGambar(String gambar) {
            this.gambar = gambar;
        }

        public String getBookingStatus() {
            return bookingStatus;
        }

        public void setBookingStatus(String bookingStatus) {
            this.bookingStatus = bookingStatus;
        }
    }
}
