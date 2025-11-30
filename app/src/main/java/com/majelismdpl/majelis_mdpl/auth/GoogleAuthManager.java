package com.majelismdpl.majelis_mdpl.auth;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.browser.customtabs.CustomTabsIntent;

import com.majelismdpl.majelis_mdpl.utils.ApiConfig;

public class GoogleAuthManager {

    private static final String TAG = "GoogleAuthManager";

    public static void startGoogleLogin(Context context) {
        try {
            // Ambil base URL dari ApiConfig
            String baseUrl = ApiConfig.getBaseUrl();

            // Buat URL OAuth untuk login
            // Format: https://your-ngrok-url.ngrok-free.app/backend/mobile/google-oauth-android.php?type=login
            String loginUrl = baseUrl + "/mobile/google-oauth-android.php?type=login";

            Log.d(TAG, "🔵 Opening Google OAuth URL: " + loginUrl);

            openCustomTab(context, loginUrl);
        } catch (Exception e) {
            Log.e(TAG, "❌ Error in startGoogleLogin: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void startGoogleSignup(Context context) {
        try {
            String baseUrl = ApiConfig.getBaseUrl();
            String signupUrl = baseUrl + "/mobile/google-oauth-android.php?type=signup";

            Log.d(TAG, "🔵 Opening Google OAuth URL (Signup): " + signupUrl);

            openCustomTab(context, signupUrl);
        } catch (Exception e) {
            Log.e(TAG, "❌ Error in startGoogleSignup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void openCustomTab(Context context, String url) {
        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

            // Optional: Customize toolbar color
            // builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));

            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(url));

            Log.d(TAG, "✅ Custom Tab launched successfully");
        } catch (Exception e) {
            Log.e(TAG, "❌ Error launching Custom Tab: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
