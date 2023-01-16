package es.upm.etsiinf.dam.coinapp.database.functions;

import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_ATH;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_ATH_CHANGE_PERCENTAGE;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_ATH_DATE;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_ATL;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_ATL_CHANGE_PERCENTAGE;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_ATL_DATE;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_CIRCULATING_SUPPLY;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_CURRENT_PRICE;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_FULLY_DILUTED_VALUATION;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_HIGH_24H;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_ID;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_IMAGE;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_IMAGE_BITMAP;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_LAST_UPDATED;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_LOW_24H;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_MARKET_CAP;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_MARKET_CAP_CHANGE_24H;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_MARKET_CAP_CHANGE_PERCENTAGE_24H;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_MARKET_CAP_RANK;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_MAX_SUPPLY;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_NAME;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_PRICE_CHANGE_24H;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_PRICE_CHANGE_PERCENTAGE_24H;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_ROI;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_SYMBOL;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_TOTAL_SUPPLY;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.COLUMN_TOTAL_VOLUME;
import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.TABLE_NAME;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;
import es.upm.etsiinf.dam.coinapp.utils.ImageManager;

public class CoinDB {
    private CoinDatabaseHelper dbHelper;

    public CoinDB (Context context) {
        dbHelper = new CoinDatabaseHelper(context);
    }

    public int getNumOfRecords(){
        int numRecords;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_MARKET_CAP_RANK);

        if (cursor.moveToFirst()) {
            numRecords = cursor.getCount();
        } else {
            numRecords = 0;
        }
        cursor.close();
        return numRecords;

    }

    public void updateCoins (List<Coin> coins){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<ContentValues> valuesList = new ArrayList<>();

        for(Coin coin : coins){
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, coin.getId());
            values.put(COLUMN_SYMBOL, coin.getSymbol());
            values.put(COLUMN_NAME, coin.getName());
            values.put(COLUMN_CURRENT_PRICE, coin.getCurrent_price());
            values.put(COLUMN_MARKET_CAP, coin.getMarket_cap());
            values.put(COLUMN_MARKET_CAP_RANK, coin.getMarket_cap_rank());
            values.put(COLUMN_FULLY_DILUTED_VALUATION, coin.getFully_diluted_valuation());
            values.put(COLUMN_TOTAL_VOLUME, coin.getTotal_volume());
            values.put(COLUMN_HIGH_24H, coin.getHigh_24h());
            values.put(COLUMN_LOW_24H, coin.getLow_24h());
            values.put(COLUMN_PRICE_CHANGE_24H, coin.getPrice_change_24h());
            values.put(COLUMN_PRICE_CHANGE_PERCENTAGE_24H, coin.getPrice_change_percentage_24h());
            values.put(COLUMN_MARKET_CAP_CHANGE_24H, coin.getMarket_cap_change_24h());
            values.put(COLUMN_MARKET_CAP_CHANGE_PERCENTAGE_24H, coin.getMarket_cap_change_percentage_24h());
            values.put(COLUMN_CIRCULATING_SUPPLY, coin.getCirculating_supply());
            values.put(COLUMN_TOTAL_SUPPLY, coin.getTotal_supply());
            values.put(COLUMN_MAX_SUPPLY, coin.getMax_supply());
            values.put(COLUMN_ATH, coin.getAth());
            values.put(COLUMN_ATH_CHANGE_PERCENTAGE, coin.getAth_change_percentage());
            values.put(COLUMN_ATH_DATE, coin.getAth_date());
            values.put(COLUMN_ATL, coin.getAtl());
            values.put(COLUMN_ATL_CHANGE_PERCENTAGE, coin.getAtl_change_percentage());
            values.put(COLUMN_ATL_DATE, coin.getAtl_date());
            if(coin.getRoi()==null ){
                values.put(COLUMN_ROI,"");
            }else{
                values.put(COLUMN_ROI, coin.getRoi().toJson());
            }
            values.put(COLUMN_LAST_UPDATED, coin.getLast_updated());
            valuesList.add(values);
        }

        try {
            String where = COLUMN_ID + " = ?";
            db.beginTransaction();
            for (int i = 0; i < valuesList.size(); i++) {
                ContentValues cv = valuesList.get(i);
                String[] whereArgs = {cv.getAsString(COLUMN_ID)};
                db.update(TABLE_NAME, valuesList.get(i),where,whereArgs);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

    }
    public void insertCoins (List<Coin> coins) throws IOException{

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<ContentValues> valuesList = new ArrayList<>();

        List<Coin> toUpdate = new LinkedList<>();

        for (Coin coin : coins) {
            if(countCoinById(coin.getId())==0){
                Log.i("updateWorker",coin.getId()+": "+countCoinById(coin.getId()));
                ContentValues values = new ContentValues();
                values.put(COLUMN_ID, coin.getId());
                values.put(COLUMN_SYMBOL, coin.getSymbol());
                values.put(COLUMN_NAME, coin.getName());
                values.put(COLUMN_IMAGE, coin.getImageBytes());
                values.put(COLUMN_CURRENT_PRICE, coin.getCurrent_price());
                values.put(COLUMN_MARKET_CAP, coin.getMarket_cap());
                values.put(COLUMN_MARKET_CAP_RANK, coin.getMarket_cap_rank());
                values.put(COLUMN_FULLY_DILUTED_VALUATION, coin.getFully_diluted_valuation());
                values.put(COLUMN_TOTAL_VOLUME, coin.getTotal_volume());
                values.put(COLUMN_HIGH_24H, coin.getHigh_24h());
                values.put(COLUMN_LOW_24H, coin.getLow_24h());
                values.put(COLUMN_PRICE_CHANGE_24H, coin.getPrice_change_24h());
                values.put(COLUMN_PRICE_CHANGE_PERCENTAGE_24H, coin.getPrice_change_percentage_24h());
                values.put(COLUMN_MARKET_CAP_CHANGE_24H, coin.getMarket_cap_change_24h());
                values.put(COLUMN_MARKET_CAP_CHANGE_PERCENTAGE_24H, coin.getMarket_cap_change_percentage_24h());
                values.put(COLUMN_CIRCULATING_SUPPLY, coin.getCirculating_supply());
                values.put(COLUMN_TOTAL_SUPPLY, coin.getTotal_supply());
                values.put(COLUMN_MAX_SUPPLY, coin.getMax_supply());
                values.put(COLUMN_ATH, coin.getAth());
                values.put(COLUMN_ATH_CHANGE_PERCENTAGE, coin.getAth_change_percentage());
                values.put(COLUMN_ATH_DATE, coin.getAth_date());
                values.put(COLUMN_ATL, coin.getAtl());
                values.put(COLUMN_ATL_CHANGE_PERCENTAGE, coin.getAtl_change_percentage());
                values.put(COLUMN_ATL_DATE, coin.getAtl_date());
                if(coin.getRoi()==null ){
                    values.put(COLUMN_ROI,"");
                }else{
                    values.put(COLUMN_ROI, coin.getRoi().toJson());
                }
                values.put(COLUMN_LAST_UPDATED, coin.getLast_updated());
                values.put(COLUMN_IMAGE_BITMAP, coin.getImageBitmap().toString());

                valuesList.add(values);
            }else{
                Log.i("updateWorker",coin.getId()+": toUpdate");
                toUpdate.add(coin);
            }

        }

        try {
            db.beginTransaction();
            for (int i = 0; i < valuesList.size(); i++) {
                db.insert(TABLE_NAME, null, valuesList.get(i));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        //Actualizar monedas en caso de haberlas
        if(toUpdate.size()>0) updateCoins(toUpdate);

    }

    public int countCoinById(String id) {
        int count = 0;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{"COUNT(*)"},
                COLUMN_ID + "=?",
                new String[] {id},
                null,
                null,
                COLUMN_MARKET_CAP_RANK
        );
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    @SuppressLint("Range")
    public Coin searchCoinById (String id) throws JSONException {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[] {id});
        Coin result = new Coin();
        if(cursor.moveToFirst()){
            id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
            String symbol = cursor.getString(cursor.getColumnIndex(COLUMN_SYMBOL));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            byte[] image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
            Bitmap imagebitmap = new ImageManager().getBitmapFromBLOB(image);
            double currentPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_CURRENT_PRICE));
            double marketCap = cursor.getDouble(cursor.getColumnIndex(COLUMN_MARKET_CAP));
            int marketCapRank = cursor.getInt(cursor.getColumnIndex(COLUMN_MARKET_CAP_RANK));
            double fullyDilutedValuation = cursor.getDouble(cursor.getColumnIndex(COLUMN_FULLY_DILUTED_VALUATION));
            double totalVolume = cursor.getDouble(cursor.getColumnIndex(COLUMN_TOTAL_VOLUME));
            double high24h = cursor.getDouble(cursor.getColumnIndex(COLUMN_HIGH_24H));
            double low24h = cursor.getDouble(cursor.getColumnIndex(COLUMN_LOW_24H));
            double priceChange24h = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE_CHANGE_24H));
            double priceChangePercentage24h = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE_CHANGE_PERCENTAGE_24H));
            double marketCapChange24h = cursor.getDouble(cursor.getColumnIndex(COLUMN_MARKET_CAP_CHANGE_24H));
            double marketCapChangePercentage24h = cursor.getDouble(cursor.getColumnIndex(COLUMN_MARKET_CAP_CHANGE_PERCENTAGE_24H));
            double circulatingSupply = cursor.getDouble(cursor.getColumnIndex(COLUMN_CIRCULATING_SUPPLY));
            double totalSupply = cursor.getDouble(cursor.getColumnIndex(COLUMN_TOTAL_SUPPLY));
            double maxSupply = cursor.getDouble(cursor.getColumnIndex(COLUMN_MAX_SUPPLY));
            double ath = cursor.getDouble(cursor.getColumnIndex(COLUMN_ATH));
            double athChangePercentage = cursor.getDouble(cursor.getColumnIndex(COLUMN_ATH_CHANGE_PERCENTAGE));
            String athDate = cursor.getString(cursor.getColumnIndex(COLUMN_ATH_DATE));
            double atl = cursor.getDouble(cursor.getColumnIndex(COLUMN_ATL));
            double atlChangePercentage = cursor.getDouble(cursor.getColumnIndex(COLUMN_ATL_CHANGE_PERCENTAGE));
            String atlDate = cursor.getString(cursor.getColumnIndex(COLUMN_ATL_DATE));
            String roiString = cursor.getString(cursor.getColumnIndex(COLUMN_ROI));

            Coin.Roi roi = new Coin.Roi();
            if(!roiString.isEmpty()){
                JSONObject jsonObject = new JSONObject(roiString);
                double times = jsonObject.getDouble("times");
                String currency = jsonObject.getString("currency");
                double percentage = jsonObject.getDouble("percentage");

                roi.setCurrency(currency);
                roi.setTimes(times);
                roi.setPercentage(percentage);
            }
            String lastUpdated = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_UPDATED));

            //Creo el objeto
            Coin coin = new Coin();
            coin.setId(id);
            coin.setSymbol(symbol);
            coin.setName(name);
            coin.setImageBytes(image);
            coin.setImageBitmap(imagebitmap);
            coin.setCurrent_price(currentPrice);
            coin.setMarket_cap_rank(marketCapRank);
            coin.setMarket_cap(marketCap);
            coin.setFully_diluted_valuation(fullyDilutedValuation);
            coin.setTotal_volume(totalVolume);
            coin.setHigh_24h(high24h);
            coin.setLow_24h(low24h);
            coin.setPrice_change_24h(priceChange24h);
            coin.setPrice_change_percentage_24h(priceChangePercentage24h);
            coin.setMarket_cap_change_24h(marketCapChange24h);
            coin.setMarket_cap_change_percentage_24h(marketCapChangePercentage24h);
            coin.setCirculating_supply(circulatingSupply);
            coin.setTotal_supply(totalSupply);
            coin.setMax_supply(maxSupply);
            coin.setAth_change_percentage(athChangePercentage);
            coin.setAth(ath);
            coin.setAth_date(athDate);
            coin.setAtl(atl);
            coin.setAtl_change_percentage(atlChangePercentage);
            coin.setAtl_date(atlDate);
            coin.setRoi(roi);
            coin.setLast_updated(lastUpdated);
            result = coin;
            cursor.close();
        }


        Log.i("ResultCoin",result.toString());
        return result;
    }

    public List<Coin> getCoinsMarketCapRange (int min, int max) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_SYMBOL,
                COLUMN_NAME,
                COLUMN_IMAGE,
                COLUMN_CURRENT_PRICE,
                COLUMN_MARKET_CAP,
                COLUMN_MARKET_CAP_RANK,
                COLUMN_PRICE_CHANGE_PERCENTAGE_24H
                // Otros campos omitidos
        };

        String selection = COLUMN_MARKET_CAP_RANK + " BETWEEN ? AND ?";
        String[] selectionArgs = {String.valueOf(min), String.valueOf(max)};

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                COLUMN_MARKET_CAP_RANK);

        List<Coin> coins = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String symbol = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SYMBOL));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
            double currentPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_PRICE));
            double marketCap = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MARKET_CAP));
            int marketCapRank = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MARKET_CAP_RANK));
            double percentage = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE_CHANGE_PERCENTAGE_24H));

            Coin coin = new Coin();
            coin.setId(id);
            coin.setSymbol(symbol);
            coin.setName(name);
            coin.setImageBytes(image);
            coin.setCurrent_price(currentPrice);
            coin.setMarket_cap(marketCap);
            coin.setMarket_cap_rank(marketCapRank);
            coin.setPrice_change_percentage_24h(percentage);
            coins.add(coin);
        }
        return coins;
    }

    public void deleteAllCoins() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

}
