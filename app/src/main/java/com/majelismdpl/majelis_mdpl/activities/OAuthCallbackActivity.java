package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.majelismdpl.majelis_mdpl.models.LoginResponse;
import com.majelismdpl.majelis_mdpl.models.User;
import com.majelismdpl.majelis_mdpl.utils.SessionManager;

public class OAuthCallbackActivity extends AppCompatActivity {

    private static final String TAG = "OAuthCallback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri data = getIntent().getData();

        if (data != null) {
            Log.d(TAG, "✅ Received OAuth callback: " + data.toString());

            // Parse OAuth response dari URI
            String success = data.getQueryParameter("success");
            String idUserStr = data.getQueryParameter("id_user");  // ← TAMBAHAN BARU
            String username = data.getQueryParameter("username");
            String role = data.getQueryParameter("role");
            String message = data.getQueryParameter("message");

            Log.d(TAG, "OAuth Response - Success: " + success + ", ID: " + idUserStr + ", Username: " + username + ", Role: " + role);

            if ("1".equals(success) && username != null && role != null) {
                // Login berhasil
                Log.d(TAG, "✅ OAuth login successful for user: " + username);

                // ============ PERBAIKAN: Parse id_user dari callback ============
                int idUser = 0;
                try {
                    if (idUserStr != null && !idUserStr.isEmpty()) {
                        idUser = Integer.parseInt(idUserStr);
                    }
                } catch (NumberFormatException e) {
                    Log.e(TAG, "❌ Error parsing id_user: " + e.getMessage());
                }

                User user = new User();
                user.setIdUser(idUser);  // ← SET ID USER DARI BACKEND
                user.setUsername(username);
                user.setRole(role);

                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setSuccess(true);
                loginResponse.setRole(role);
                loginResponse.setUser(user);
                loginResponse.setMessage(message);

                // Simpan ke SessionManager
                SessionManager sessionManager = SessionManager.getInstance(this);
                sessionManager.saveLoginResponse(loginResponse);

                Log.d(TAG, "✅ OAuth data saved to SessionManager with ID: " + idUser);
                // =================================================================

                // Show success message
                String successMessage = message != null ? message : "Login Google berhasil!";
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();

                // Redirect ke MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            } else {
                // Login gagal
                String errorMsg = message != null ? message : "Login Google gagal!";
                Log.e(TAG, "❌ OAuth login failed: " + errorMsg);

                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();

                // Kembali ke LoginActivity
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } else {
            Log.e(TAG, "❌ No OAuth callback data received");
            Toast.makeText(this, "Tidak dapat memproses login Google", Toast.LENGTH_SHORT).show();

            // Kembali ke LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        finish();
    }
}
