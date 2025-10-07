package com.majelismdpl.majelis_mdpl.auth;

import android.net.Uri;

import com.majelismdpl.majelis_mdpl.models.GoogleAuthResponse;

public class OAuthHelper {

    public static GoogleAuthResponse parseOAuthResponse(Uri data) {
        if (data == null) return null;

        String success = data.getQueryParameter("success");
        String username = data.getQueryParameter("username");
        String role = data.getQueryParameter("role");
        String message = data.getQueryParameter("message");

        boolean isSuccess = "1".equals(success);

        return new GoogleAuthResponse(isSuccess, message, username, role);
    }
}
