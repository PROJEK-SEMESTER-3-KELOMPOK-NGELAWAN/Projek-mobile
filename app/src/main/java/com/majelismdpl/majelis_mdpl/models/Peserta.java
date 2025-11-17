package com.majelismdpl.majelis_mdpl.models;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class Peserta {

    @SerializedName("id")
    private String id;

    @SerializedName("nama")
    private String nama;

    @SerializedName("email")
    private String email;

    @SerializedName("status")
    private String status;

    @SerializedName("avatarUrl")
    private String avatarUrl;

    @SerializedName("nomorWa")
    private String nomorWa;

    @SerializedName("nik")
    private String nik;

    @SerializedName("tempat_lahir")
    private String tempatLahir;

    @SerializedName("tanggal_lahir")
    private String tanggalLahir;

    @SerializedName("alamat")
    private String alamat;

    @SerializedName("id_booking")
    private int idBooking;

    @SerializedName("booking_status")
    private String bookingStatus;

    @SerializedName("total_harga")
    private int totalHarga;

    @SerializedName("jumlah_orang")
    private int jumlahOrang;

    @SerializedName("username")
    private String username;

    @SerializedName("foto_profil")
    private String fotoProfil;

    // Constructor kosong
    public Peserta() {
    }

    // Constructor dengan parameter dasar (untuk backward compatibility)
    public Peserta(String id, String nama, String email, String status, String avatarUrl, String nomorWa) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.status = status;
        this.avatarUrl = avatarUrl;
        this.nomorWa = nomorWa;
    }

    // Constructor lengkap
    public Peserta(String id, String nama, String email, String status, String avatarUrl, String nomorWa,
                   String nik, String tempatLahir, String tanggalLahir, String alamat, int idBooking,
                   String bookingStatus, int totalHarga, int jumlahOrang, String username, String fotoProfil) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.status = status;
        this.avatarUrl = avatarUrl;
        this.nomorWa = nomorWa;
        this.nik = nik;
        this.tempatLahir = tempatLahir;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
        this.idBooking = idBooking;
        this.bookingStatus = bookingStatus;
        this.totalHarga = totalHarga;
        this.jumlahOrang = jumlahOrang;
        this.username = username;
        this.fotoProfil = fotoProfil;
    }

    // Getters
    public String getId() { return id; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public String getStatus() { return status; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getNomorWa() { return nomorWa; }
    public String getNik() { return nik; }
    public String getTempatLahir() { return tempatLahir; }
    public String getTanggalLahir() { return tanggalLahir; }
    public String getAlamat() { return alamat; }
    public int getIdBooking() { return idBooking; }
    public String getBookingStatus() { return bookingStatus; }
    public int getTotalHarga() { return totalHarga; }
    public int getJumlahOrang() { return jumlahOrang; }
    public String getUsername() { return username; }
    public String getFotoProfil() { return fotoProfil; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setNama(String nama) { this.nama = nama; }
    public void setEmail(String email) { this.email = email; }
    public void setStatus(String status) { this.status = status; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public void setNomorWa(String nomorWa) { this.nomorWa = nomorWa; }
    public void setNik(String nik) { this.nik = nik; }
    public void setTempatLahir(String tempatLahir) { this.tempatLahir = tempatLahir; }
    public void setTanggalLahir(String tanggalLahir) { this.tanggalLahir = tanggalLahir; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public void setIdBooking(int idBooking) { this.idBooking = idBooking; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public void setTotalHarga(int totalHarga) { this.totalHarga = totalHarga; }
    public void setJumlahOrang(int jumlahOrang) { this.jumlahOrang = jumlahOrang; }
    public void setUsername(String username) { this.username = username; }
    public void setFotoProfil(String fotoProfil) { this.fotoProfil = fotoProfil; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Peserta peserta = (Peserta) o;
        return idBooking == peserta.idBooking &&
                totalHarga == peserta.totalHarga &&
                jumlahOrang == peserta.jumlahOrang &&
                Objects.equals(id, peserta.id) &&
                Objects.equals(nama, peserta.nama) &&
                Objects.equals(email, peserta.email) &&
                Objects.equals(status, peserta.status) &&
                Objects.equals(avatarUrl, peserta.avatarUrl) &&
                Objects.equals(nomorWa, peserta.nomorWa) &&
                Objects.equals(nik, peserta.nik) &&
                Objects.equals(tempatLahir, peserta.tempatLahir) &&
                Objects.equals(tanggalLahir, peserta.tanggalLahir) &&
                Objects.equals(alamat, peserta.alamat) &&
                Objects.equals(bookingStatus, peserta.bookingStatus) &&
                Objects.equals(username, peserta.username) &&
                Objects.equals(fotoProfil, peserta.fotoProfil);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nama, email, status, avatarUrl, nomorWa, nik, tempatLahir,
                tanggalLahir, alamat, idBooking, bookingStatus, totalHarga, jumlahOrang,
                username, fotoProfil);
    }
}
