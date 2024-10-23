package com.example.inventorymanagementapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsApp {
    private static final String PREF_TEXT_SIZE = "text_size";

    // Method to get the saved text size preference
    public static String getTextSizePreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PREF_TEXT_SIZE, "medium");
    }

    // Method to set the text size preference
    public static void setTextSizePreference(Context context, String textSize) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_TEXT_SIZE, textSize);
        editor.apply();
    }
}
