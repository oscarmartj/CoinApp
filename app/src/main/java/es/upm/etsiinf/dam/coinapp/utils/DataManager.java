package es.upm.etsiinf.dam.coinapp.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.math.BigDecimal;
import android.preference.PreferenceManager;

import java.math.RoundingMode;
import java.util.Locale;

import es.upm.etsiinf.dam.coinapp.modelos.Coin;

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

    public static String setIntentShareText(Coin coin){
        //emoticonos y utiles
        String redCircle="\uD83D\uDD34";
        String greenCircle="\uD83D\uDFE2";
        String symbolCoin = coin.getSymbol().toUpperCase();
        String dollarHashtagSymbol = "$";
        String currentPrice = dollarHashtagSymbol+String.format(Locale.US,"%,.2f",coin.getCurrent_price());
        double percentage = coin.getPrice_change_percentage_24h();
        String low24hWhatEmoji = (coin.getLow_24h()<coin.getHigh_24h()?redCircle:greenCircle);
        String high24WhatEmoji = (coin.getHigh_24h()<coin.getLow_24h()?redCircle:greenCircle);

        String titulo="¿Como ha cambiado "+coin.getName()+" "+dollarHashtagSymbol+coin.getSymbol().toUpperCase()+"?"+"\n";
        String cambio24h = "1 " + symbolCoin + " = " + currentPrice + " " + (percentage>0.0?"+"+percentage+"%":percentage+"%") + " " + (percentage < 0.0 ? redCircle + "\n" : greenCircle + "\n");
        String detalles="Detalles:\n";
        String cambio = "Cambio: "+(coin.getPrice_change_24h()<0.0?redCircle:greenCircle+"+")+coin.getPrice_change_24h()+dollarHashtagSymbol+"\n";
        String low24h = "Precio más bajo (24h) = "+dollarHashtagSymbol+coin.getLow_24h()+low24hWhatEmoji+"\n";
        String high24h = "Precio más alto (24h) = "+dollarHashtagSymbol+coin.getHigh_24h()+high24WhatEmoji+"\n";
        String salto = "\n";
        String hashtag = "#"+symbolCoin+"\n";

        return titulo+salto+cambio24h+detalles+cambio+low24h+high24h+salto+hashtag;
    }

    public static boolean isFavorite(Context context, String coinId){
        SharedPreferences sharedPreferences = context.getSharedPreferences("favorites", MODE_PRIVATE);
        return sharedPreferences.getBoolean(coinId,false);
    }
    public static void setFavorite(Context context, String coinId){
        SharedPreferences sharedPreferences = context.getSharedPreferences("favorites",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(coinId,true);
        editor.apply();
    }
    public static void removeFavorite(Context context, String coinId){
        SharedPreferences sharedPreferences = context.getSharedPreferences("favorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(coinId);
        editor.apply();
    }
}

