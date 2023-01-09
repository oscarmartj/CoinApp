package es.upm.etsiinf.dam.coinapp.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.math.BigDecimal;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.upm.etsiinf.dam.coinapp.database.functions.CoinDB;
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
        String currentPrice = dollarHashtagSymbol+String.format(Locale.US,"%,."+( DataManager.obtenerPrecisionFormato(coin.getCurrent_price()) )+"f",coin.getCurrent_price());
        double percentage = coin.getPrice_change_percentage_24h();
        String low24hWhatEmoji = (coin.getLow_24h()<coin.getHigh_24h()?redCircle:greenCircle);
        String high24WhatEmoji = (coin.getHigh_24h()<coin.getLow_24h()?redCircle:greenCircle);

        String titulo="¿Como ha cambiado "+coin.getName()+" "+dollarHashtagSymbol+coin.getSymbol().toUpperCase()+"?"+"\n";
        String cambio24h = "1 " + symbolCoin + " = " + currentPrice + " " + (percentage>0.0?"+"+percentage+"%":percentage+"%") + " " + (percentage < 0.0 ? redCircle + "\n" : greenCircle + "\n");
        String detalles="Detalles:\n";
        Log.i("precisionCambio",DataManager.obtenerPrecisionFormato(coin.getPrice_change_24h())+" precio: "+coin.getPrice_change_24h());
        String cambio = "Cambio: "+(new BigDecimal(coin.getPrice_change_24h()).toString().charAt(0)=='-'?redCircle:greenCircle+"+")+String.format(Locale.US,"%,."+( DataManager.obtenerPrecisionFormato(coin.getPrice_change_24h()) )+"f",coin.getPrice_change_24h())+dollarHashtagSymbol+"\n";
        String low24h = "Precio más bajo (24h) = "+dollarHashtagSymbol+String.format(Locale.US,"%,."+( DataManager.obtenerPrecisionFormato(coin.getLow_24h()) )+"f",coin.getLow_24h())+low24hWhatEmoji+"\n";
        String high24h = "Precio más alto (24h) = "+dollarHashtagSymbol+String.format(Locale.US,"%,."+( DataManager.obtenerPrecisionFormato(coin.getHigh_24h()) )+"f",coin.getHigh_24h())+high24WhatEmoji+"\n";
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

    public static List<String> getFavorites(SharedPreferences sharedPreferences) {

        Map<String, ?> allEntries = sharedPreferences.getAll();
        List<String> trueCurrencies = new ArrayList<>();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Boolean && (Boolean) value) {
                trueCurrencies.add(entry.getKey());
            }
        }
        return trueCurrencies;
    }

    public static List<Coin> setCoins(JSONArray coinsJson) throws JSONException, IOException {
        List<Coin> coins = new LinkedList<>();
        // Recorre la lista de criptomonedas en el objeto JSON y crea objetos Coin para cada una de ellas
        for (int i = 0; i < coinsJson.length(); i++) {
            JSONObject coinJson = coinsJson.getJSONObject(i);
            String id = coinJson.optString("id");
            String name = coinJson.optString("name");
            String symbol = coinJson.optString("symbol");
            String image = coinJson.optString("image");
            double current_price = coinJson.optDouble("current_price");
            double market_cap = coinJson.optDouble("market_cap");
            int market_cap_rank = coinJson.optInt("market_cap_rank");
            double fully_diluted_valuation = coinJson.optDouble("fully_diluted_valuation");
            double total_volume = coinJson.optDouble("total_volume");
            double high_24h = coinJson.optDouble("high_24h");
            double low_24h = coinJson.optDouble("low_24h");
            double price_change_24h = coinJson.optDouble("price_change_24h");
            double price_change_percentage_24h = coinJson.optDouble("price_change_percentage_24h");
            double market_cap_change_24h = coinJson.optDouble("market_cap_change_24h");
            double market_cap_change_percentage_24h = coinJson.optDouble("market_cap_change_percentage_24h");
            double circulating_supply = coinJson.optDouble("circulating_supply");
            double total_supply = coinJson.optDouble("total_supply");
            double max_supply = coinJson.optDouble("max_supply");
            double ath = coinJson.optDouble("ath");
            double ath_change_percentage = coinJson.optDouble("ath_change_percentage");
            String ath_date = coinJson.optString("ath_date");
            double atl = coinJson.optDouble("atl");
            double atl_change_percentage = coinJson.optDouble("atl_change_percentage");
            String atl_date = coinJson.optString("atl_date");
            JSONObject roiJson = coinJson.optJSONObject("roi");
            Coin.Roi roi = new Coin.Roi();
            if(roiJson == null ){
                roi=null;
            }else{
                roi.setTimes(roiJson.optDouble("times"));
                roi.setCurrency(roiJson.optString("currency"));
                roi.setPercentage(roiJson.optDouble("percentage"));
            }
            String last_updated = coinJson.optString("last_updated");

            Coin coin = new Coin();
            coin.setId(id);
            coin.setName(name);
            coin.setSymbol(symbol);
            coin.setImage(image);
            if(!image.isEmpty()) {
                ImageManager imageManager = new ImageManager();
                Log.wtf("ImageBytes", imageManager.toString());
                Log.wtf("ImageBytes", image);
                coin.setImageBitmap(imageManager.getBitmapFromURL(image));
                coin.setImageBytes(imageManager.getBytesFromBitmap(coin.getImageBitmap()));
            }
            coin.setCurrent_price(current_price);
            coin.setMarket_cap(market_cap);
            coin.setMarket_cap_rank(market_cap_rank);
            coin.setFully_diluted_valuation(fully_diluted_valuation);
            coin.setTotal_volume(total_volume);
            coin.setHigh_24h(high_24h);
            coin.setLow_24h(low_24h);
            coin.setPrice_change_24h(price_change_24h);
            coin.setPrice_change_percentage_24h(price_change_percentage_24h);
            coin.setMarket_cap_change_24h(market_cap_change_24h);
            coin.setMarket_cap_change_percentage_24h(market_cap_change_percentage_24h);
            coin.setCirculating_supply(circulating_supply);
            coin.setTotal_supply(total_supply);
            coin.setMax_supply(max_supply);
            coin.setAth(ath);
            coin.setAth_change_percentage(ath_change_percentage);
            coin.setAth_date(ath_date);
            coin.setAtl(atl);
            coin.setAtl_change_percentage(atl_change_percentage);
            coin.setAtl_date(atl_date);
            coin.setRoi(roi);
            coin.setLast_updated(last_updated);

            // Añade el objeto Coin a la lista
            coins.add(coin);
        }
        return coins;
    }

    public String setPendingShareText(Coin coin){
        //emoticonos y utiles
        String redCircle="\uD83D\uDD34";
        String greenCircle="\uD83D\uDFE2";
        String symbolCoin = coin.getSymbol().toUpperCase();
        String dollarHashtagSymbol = "$";

        String currentPrice = dollarHashtagSymbol+String.format(Locale.US,"%,."+( DataManager.obtenerPrecisionFormato(coin.getCurrent_price()) )+"f",coin.getCurrent_price());
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

    public static int obtenerPrecisionFormato(double valor) {
        int firstPositionWithout0 = DataManager.obtenerPrimeraPosicionDecimal(valor);
        Log.i("precisionCambio",firstPositionWithout0+ " fp");
        if (firstPositionWithout0 > 2) {
            return firstPositionWithout0 + 1;
        } else if (firstPositionWithout0 == 2 && String.valueOf(valor).length() > 2) {
            return firstPositionWithout0 + 1;
        } else {
            return 2;
        }
    }


    public static int obtenerPrimeraPosicionDecimal(Double numero) {
        String str = Double.toString(numero);
        String formateo;
        if(str.contains("E")){
            BigDecimal bd = new BigDecimal(numero);
            String exponente = str.charAt(str.length()-2) +""+str.charAt(str.length()-1);
            double exp = Double.parseDouble(exponente);
            if(exp < -6.0){
                int valorAbsoluto = (int) Math.abs(exp);
                bd = bd.setScale(valorAbsoluto+1, BigDecimal.ROUND_HALF_UP);
            }
            String formattedNumber = bd.toString();
            formateo = formattedNumber.length() > 8 ? formattedNumber.substring(0, formattedNumber.length()>=10?10:8) : formattedNumber;
        }else{
            formateo=str;
        }

        String parteEnteraString = formateo.substring(0,formateo.indexOf("."));
        int parteEntera = Integer.parseInt(parteEnteraString);
        if (parteEntera>0) {
            return -1;
        }
        int posicionPuntoDecimal = formateo.indexOf(".");
        for (int i = posicionPuntoDecimal + 1; i < formateo.length(); i++) {
            if (formateo.charAt(i) != '0') {
                return i - posicionPuntoDecimal;
            }
        }
        return -1;
    }

}

