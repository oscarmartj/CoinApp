package es.upm.etsiinf.dam.coinapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.math.BigDecimal;
import android.preference.PreferenceManager;

import java.math.RoundingMode;

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

    public static String roundNumber(double num) {
        BigDecimal bd = new BigDecimal(num);
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        String result = bd.toString();
        if (result.charAt(0) == '-') {
            result = result.substring(1);
        }
        return result;
    }
    public static double roundNumberWithSign(double num) {
        BigDecimal bd = new BigDecimal(num);
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    public static int whatPage(int records){
        return records/20;
    }

    public static int getInitialRange(int x) {
        if (x == 1) {
            return 1;
        }
        return (x-1) * 20;
    }
}

