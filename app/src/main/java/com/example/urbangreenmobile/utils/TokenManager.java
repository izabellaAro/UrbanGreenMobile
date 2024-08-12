package com.example.urbangreenmobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREFS_NAME = "APP_PREFS";
    private static final String TOKEN_KEY = "JWT_TOKEN";
    private SharedPreferences preferences;

    public TokenManager(Context context) {
        this.preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    public String getToken() {
        return preferences.getString(TOKEN_KEY, null);
    }

    public void clearToken() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(TOKEN_KEY);
        editor.apply();
    }
}
