package com.majelismdpl.majelis_mdpl.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.majelismdpl.majelis_mdpl.utils.SharedPrefManager;

public class OAuthCallbackActivity extends AppCompatActivity {

    private static final String TAG = "OAuthCallback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri data = getIntent().getData();

        if (data != null) {
            Log.d(TAG, "Received OAuth callback: " + data.toString());

            // Parse OAuth response dari URI
            String success = data.getQueryParameter("success");
            String username = data.getQueryParameter("username");
            String role = data.getQueryParameter("role");
            String message = data.getQueryParameter("message");

            Log.d(TAG, "OAuth Response - Success: " + success + ", Username: " + username + ", Role: " + role);

            if ("1".equals(success) && username != null && role != null) {
                // Login berhasil
                Log.d(TAG, "OAuth login successful for user: " + username);

                // Simpan data login
                SharedPrefManager.getInstance(this).saveLoginData(username, role);

                // Show success message
                Toast.makeText(this,
                        message != null ? message : "Login Google berhasil!",
                        Toast.LENGTH_SHORT).show();

                // Redirect ke MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("USERNAME", username);
                intent.putExtra("ROLE", role);
                intent.putExtra("IS_OAUTH_LOGIN", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                // Login gagal
                String errorMsg = message != null ? message : "Login Google gagal!";
                Log.e(TAG, "OAuth login failed: " + errorMsg);

                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();

                // Kembali ke LoginActivity
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } else {
            Log.e(TAG, "No OAuth callback data received");
            Toast.makeText(this, "Tidak dapat memproses login Google", Toast.LENGTH_SHORT).show();

            // Kembali ke LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        finish();
    }
}
