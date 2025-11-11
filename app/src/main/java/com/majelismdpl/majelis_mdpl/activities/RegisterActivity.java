package com.majelismdpl.majelis_mdpl.activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.majelismdpl.majelis_mdpl.R; // Pastikan import R ini benar!

public class RegisterActivity extends AppCompatActivity {

    // Deklarasi variabel untuk komponen UI
    private TextInputLayout usernameLayout, passwordLayout, confirmPasswordLayout;
    private TextInputEditText usernameInput, passwordInput, confirmPasswordInput;
    private MaterialButton registerButton, googleSignInButton;
    private TextView loginLinkText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sesuaikan nama file layout XML Anda jika berbeda
        setContentView(R.layout.activity_register);

        // Panggil metode untuk inisialisasi view
        initViews();

        // Panggil metode untuk mengatur click listener
        setClickListeners();
    }

    /**
     * Metode untuk inisialisasi semua komponen UI dari layout.
     */
    private void initViews() {
        // Layouts
        usernameLayout = findViewById(R.id.usernameLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);

        // Input Fields
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

        // Buttons
        registerButton = findViewById(R.id.registerButton);
        googleSignInButton = findViewById(R.id.googleSignInButton);

        // TextView Link
        loginLinkText2 = findViewById(R.id.loginLinkText2);
    }

    /**
     * Metode untuk mengatur semua listener tombol dan link.
     */
    private void setClickListeners() {

        // Listener untuk Tombol DAFTAR
        registerButton.setOnClickListener(v -> {
            // Panggil metode untuk memproses registrasi
            handleRegistration();
        });

        // Listener untuk Tombol Google Sign-In
        googleSignInButton.setOnClickListener(v -> {
            // Panggil metode untuk sign-in dengan Google
            signInWithGoogle();
        });

        // Listener untuk link "Masuk"
        loginLinkText2.setOnClickListener(v -> {
            // Panggil metode untuk pindah ke halaman Login
            goToLogin();
        });
    }

    /**
     * Menangani logika saat tombol "DAFTAR" ditekan.
     */
    private void handleRegistration() {
        // Ambil input dari pengguna dan hapus spasi di awal/akhir
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Validasi input
        if (!validateInput(username, password, confirmPassword)) {
            return; // Hentikan proses jika validasi gagal
        }

        // TODO: Implementasikan logika registrasi Anda di sini
        // (Contoh: Panggil API, simpan ke Firebase, dll.)

        // Tampilkan pesan sukses (contoh)
        Toast.makeText(this, "Registrasi berhasil untuk: " + username, Toast.LENGTH_SHORT).show();

        // Setelah berhasil, arahkan pengguna ke halaman Login
        goToLogin();
    }

    /**
     * Validasi semua field input.
     * @return true jika semua valid, false jika ada yang salah.
     */
    private boolean validateInput(String username, String password, String confirmPassword) {
        // Hapus error sebelumnya
        usernameLayout.setError(null);
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);

        // Cek username
        if (username.isEmpty()) {
            usernameLayout.setError("Username tidak boleh kosong");
            usernameInput.requestFocus();
            return false;
        }

        // Cek password
        if (password.isEmpty()) {
            passwordLayout.setError("Password tidak boleh kosong");
            passwordInput.requestFocus();
            return false;
        }

        // Cek panjang password (contoh: minimal 6 karakter)
        if (password.length() < 6) {
            passwordLayout.setError("Password minimal 6 karakter");
            passwordInput.requestFocus();
            return false;
        }

        // Cek konfirmasi password
        if (confirmPassword.isEmpty()) {
            confirmPasswordLayout.setError("Konfirmasi password tidak boleh kosong");
            confirmPasswordInput.requestFocus();
            return false;
        }

        // Cek apakah password dan konfirmasi password cocok
        if (!password.equals(confirmPassword)) {
            confirmPasswordLayout.setError("Password tidak cocok");
            confirmPasswordInput.requestFocus();
            return false;
        }

        // Jika semua lolos, kembalikan true
        return true;
    }

    /**
     * Menangani logika saat tombol "Masuk dengan Google" ditekan.
     */
    private void signInWithGoogle() {
        // TODO: Implementasikan logika Google Sign-In Anda di sini.
        // Ini biasanya melibatkan GoogleSignInClient dan startActivityForResult.

        Toast.makeText(this, "Fitur Google Sign-In belum diimplementasi", Toast.LENGTH_SHORT).show();
    }

    /**
     * Berpindah ke LoginActivity.
     */
    private void goToLogin() {
        // Ganti LoginActivity.class dengan nama Activity Login Anda
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

        // Flag ini akan menghapus RegisterActivity dari tumpukan (back stack)
        // sehingga pengguna tidak bisa kembali ke halaman ini setelah login.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish(); // Tutup RegisterActivity
    }
}