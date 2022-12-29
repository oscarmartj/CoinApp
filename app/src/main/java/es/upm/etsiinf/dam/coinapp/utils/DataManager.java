package es.upm.etsiinf.dam.coinapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DataManager {
    public static void saveDatabaseVersion(int version, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("DATABASE_VERSION", version);
        editor.apply();
    }

    public static int getSavedDatabaseVersion (Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt("DATABASE_VERSION", 1);
    }


}

