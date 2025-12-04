package com.majelismdpl.majelis_mdpl.models;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private UserData data;

    public ProfileResponse() {}

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public UserData getData() { return data; }

    public static class UserData {
        @SerializedName("id_user")
        private int idUser;

        @SerializedName("username")
        private String username;

        @SerializedName("email")
        private String email;

        @SerializedName("whatsapp")
        private String whatsapp;

        @SerializedName("alamat")
        private String alamat;

        // path relatif: img/profile/xxx.jpg
        @SerializedName("foto_profil")
        private String fotoProfil;

        // URL lengkap: https://majelismdpl.my.id/img/profile/xxx.jpg
        @SerializedName("foto_url")
        private String fotoUrl;

        @SerializedName("role")
        private String role;

        @SerializedName("email_verified")
        private boolean emailVerified;

        // ===== GETTERS =====
        public int getIdUser() { return idUser; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getWhatsapp() { return whatsapp; }
        public String getAlamat() { return alamat; }
        public String getFotoProfil() { return fotoProfil; }
        public String getFotoUrl() { return fotoUrl; }
        public String getRole() { return role; }
        public boolean isEmailVerified() { return emailVerified; }

        /**
         * Helper method untuk mendapatkan URL foto yang valid
         * Prioritas: foto_url > foto_profil
         */
        public String getValidPhotoUrl() {
            if (fotoUrl != null && !fotoUrl.isEmpty()) {
                return fotoUrl;
            }
            return fotoProfil;
        }

        // ===== SETTERS =====
        public void setIdUser(int idUser) { this.idUser = idUser; }
        public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
        public void setFotoProfil(String fotoProfil) { this.fotoProfil = fotoProfil; }
        public void setUsername(String username) { this.username = username; }
        public void setEmail(String email) { this.email = email; }
        public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }
        public void setAlamat(String alamat) { this.alamat = alamat; }
        public void setRole(String role) { this.role = role; }
        public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }
    }
}
