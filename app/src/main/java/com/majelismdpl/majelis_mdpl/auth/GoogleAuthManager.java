package com.majelismdpl.majelis_mdpl.auth;

import android.content.Context;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;

import com.example.majelis_mdpl.utils.ApiConfig;

public class GoogleAuthManager {

    public static void startGoogleLogin(Context context) {
        String loginUrl = ApiConfig.getOAuthUrl() + "?type=login";
        openCustomTab(context, loginUrl);
    }

    public static void startGoogleSignup(Context context) {
        String signupUrl = ApiConfig.getOAuthUrl() + "?type=signup";
        openCustomTab(context, signupUrl);
    }

    private static void openCustomTab(Context context, String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
