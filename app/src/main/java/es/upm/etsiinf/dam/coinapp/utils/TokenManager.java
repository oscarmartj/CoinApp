package es.upm.etsiinf.dam.coinapp.utils;

import android.content.SharedPreferences;

public class TokenManager {
    private static final String TOKEN_KEY = "access_token";

    public static String getAccessTokenFromLocalStorage(SharedPreferences preferences) {
        return preferences.getString(TOKEN_KEY, "");
    }

    public static boolean userIsLoggedIn(String accessToken){
        return !accessToken.isEmpty();
    }
}

