package com.majelismdpl.majelis_mdpl.models;

import com.google.gson.annotations.SerializedName;

public class PesertaHistory {
    @SerializedName("id_participant")
    private int idParticipant;
    @SerializedName("nama")
    private String nama;
    @SerializedName("email")
    private String email;
    @SerializedName("nik")
    private String nik;
    @SerializedName("no_wa")
    private String noWa;
    @SerializedName("tempat_lahir")
    private String tempatLahir;
    @SerializedName("tanggal_lahir")
    private String tanggalLahir;
    @SerializedName("alamat")
    private String alamat;
    @SerializedName("username")
    private String username;
    @SerializedName("foto_profil")
    private String fotoProfil;
    @SerializedName("id_booking")
    private int idBooking;
    @SerializedName("jumlah_orang")
    private int jumlahOrang;
    @SerializedName("total_harga")
    private int totalHarga;
    @SerializedName("booking_status")
    private String bookingStatus;
    @SerializedName("tanggal_booking")
    private String tanggalBooking;
    @SerializedName("nama_gunung")
    private String namaGunung;
    @SerializedName("jenis_trip")
    private String jenisTrip;
    @SerializedName("trip_status")
    private String tripStatus;

    // Getter (tambahkan setter jika diperlukan)
    public int getIdParticipant() { return idParticipant; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public String getNik() { return nik; }
    public String getNoWa() { return noWa; }
    public String getTempatLahir() { return tempatLahir; }
    public String getTanggalLahir() { return tanggalLahir; }
    public String getAlamat() { return alamat; }
    public String getUsername() { return username; }
    public String getFotoProfil() { return fotoProfil; }
    public int getIdBooking() { return idBooking; }
    public int getJumlahOrang() { return jumlahOrang; }
    public int getTotalHarga() { return totalHarga; }
    public String getBookingStatus() { return bookingStatus; }
    public String getTanggalBooking() { return tanggalBooking; }
    public String getNamaGunung() { return namaGunung; }
    public String getJenisTrip() { return jenisTrip; }
    public String getTripStatus() { return tripStatus; }
}
