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
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public UserData getData() { return data; }
    public void setData(UserData data) { this.data = data; }

    // Inner class untuk data user
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

        @SerializedName("foto_profil")
        private String fotoProfil;

        @SerializedName("role")
        private String role;

        @SerializedName("email_verified")
        private boolean emailVerified;

        public UserData() {}

        public int getIdUser() { return idUser; }
        public void setIdUser(int idUser) { this.idUser = idUser; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getWhatsapp() { return whatsapp; }
        public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }

        public String getAlamat() { return alamat; }
        public void setAlamat(String alamat) { this.alamat = alamat; }

        public String getFotoProfil() { return fotoProfil; }
        public void setFotoProfil(String fotoProfil) { this.fotoProfil = fotoProfil; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public boolean isEmailVerified() { return emailVerified; }
        public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }
    }
}
