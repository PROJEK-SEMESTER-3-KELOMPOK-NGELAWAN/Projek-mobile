package com.majelismdpl.majelis_mdpl.models;

/**
 * ============================================
 * Model Trip
 * Menyimpan data trip user dari API dan untuk History
 * ============================================
 */
public class Trip {
    // Data dari API (database)
    private int idBooking;
    private int idTrip;
    private String namaGunung;
    private String jenisTrip;
    private String tanggalTrip;
    private String tanggalDisplay;
    private String durasi;
    private String viaGunung;
    private String gambarUrl;
    private int jumlahOrang;
    private int totalHarga;
    private String statusBooking;
    private String statusPembayaran;
    private String namaLokasi;
    private String waktuKumpul;
    private String linkMap;

    // Data untuk History (dummy/custom)
    private String id;
    private String mountainName;
    private String date;
    private int participants;
    private String status;
    private double rating;
    private String imageUrl;

    // Constructor kosong
    public Trip() {
    }

    /**
     * Constructor untuk History Fragment (data dummy)
     */
    public Trip(String id, String mountainName, String date, int participants,
                String status, double rating, String imageUrl) {
        this.id = id;
        this.mountainName = mountainName;
        this.date = date;
        this.participants = participants;
        this.status = status;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    // ========== Getters untuk History ==========
    public String getId() {
        return id;
    }

    public String getMountainName() {
        return mountainName;
    }

    public String getDate() {
        return date;
    }

    public int getParticipants() {
        return participants;
    }

    public String getStatus() {
        return status;
    }

    public double getRating() {
        return rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // ========== Getters untuk API Data ==========
    public int getIdBooking() {
        return idBooking;
    }

    public int getIdTrip() {
        return idTrip;
    }

    public String getNamaGunung() {
        return namaGunung;
    }

    public String getJenisTrip() {
        return jenisTrip;
    }

    public String getTanggalTrip() {
        return tanggalTrip;
    }

    public String getTanggalDisplay() {
        return tanggalDisplay;
    }

    public String getDurasi() {
        return durasi;
    }

    public String getViaGunung() {
        return viaGunung;
    }

    public String getGambarUrl() {
        return gambarUrl;
    }

    public int getJumlahOrang() {
        return jumlahOrang;
    }

    public int getTotalHarga() {
        return totalHarga;
    }

    public String getStatusBooking() {
        return statusBooking;
    }

    public String getStatusPembayaran() {
        return statusPembayaran;
    }

    public String getNamaLokasi() {
        return namaLokasi;
    }

    public String getWaktuKumpul() {
        return waktuKumpul;
    }

    public String getLinkMap() {
        return linkMap;
    }

    // ========== Setters untuk History ==========
    public void setId(String id) {
        this.id = id;
    }

    public void setMountainName(String mountainName) {
        this.mountainName = mountainName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // ========== Setters untuk API Data ==========
    public void setIdBooking(int idBooking) {
        this.idBooking = idBooking;
    }

    public void setIdTrip(int idTrip) {
        this.idTrip = idTrip;
    }

    public void setNamaGunung(String namaGunung) {
        this.namaGunung = namaGunung;
    }

    public void setJenisTrip(String jenisTrip) {
        this.jenisTrip = jenisTrip;
    }

    public void setTanggalTrip(String tanggalTrip) {
        this.tanggalTrip = tanggalTrip;
    }

    public void setTanggalDisplay(String tanggalDisplay) {
        this.tanggalDisplay = tanggalDisplay;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }

    public void setViaGunung(String viaGunung) {
        this.viaGunung = viaGunung;
    }

    public void setGambarUrl(String gambarUrl) {
        this.gambarUrl = gambarUrl;
    }

    public void setJumlahOrang(int jumlahOrang) {
        this.jumlahOrang = jumlahOrang;
    }

    public void setTotalHarga(int totalHarga) {
        this.totalHarga = totalHarga;
    }

    public void setStatusBooking(String statusBooking) {
        this.statusBooking = statusBooking;
    }

    public void setStatusPembayaran(String statusPembayaran) {
        this.statusPembayaran = statusPembayaran;
    }

    public void setNamaLokasi(String namaLokasi) {
        this.namaLokasi = namaLokasi;
    }

    public void setWaktuKumpul(String waktuKumpul) {
        this.waktuKumpul = waktuKumpul;
    }

    public void setLinkMap(String linkMap) {
        this.linkMap = linkMap;
    }
}
