package es.upm.etsiinf.dam.coinapp.database.functions;

import static es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper.TABLE_NAME;

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
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            numRecords = cursor.getCount();
        } else {
            numRecords = 0;
        }
        cursor.close();
        return numRecords;

    }
    public void insertCoins (List<Coin> coins) throws IOException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(getNumOfRecords()>0){
            int version = db.getVersion();
            db.setVersion(version+1);
        }
        List<ContentValues> valuesList = new ArrayList<>();

        for (Coin coin : coins) {

            ContentValues values = new ContentValues();
            values.put(CoinDatabaseHelper.COLUMN_ID, coin.getId());
            values.put(CoinDatabaseHelper.COLUMN_SYMBOL, coin.getSymbol());
            values.put(CoinDatabaseHelper.COLUMN_NAME, coin.getName());
            values.put(CoinDatabaseHelper.COLUMN_IMAGE, coin.getImageBytes());
            values.put(CoinDatabaseHelper.COLUMN_CURRENT_PRICE, coin.getCurrent_price());
            values.put(CoinDatabaseHelper.COLUMN_MARKET_CAP, coin.getMarket_cap());
            values.put(CoinDatabaseHelper.COLUMN_MARKET_CAP_RANK, coin.getMarket_cap_rank());
            values.put(CoinDatabaseHelper.COLUMN_FULLY_DILUTED_VALUATION, coin.getFully_diluted_valuation());
            values.put(CoinDatabaseHelper.COLUMN_TOTAL_VOLUME, coin.getTotal_volume());
            values.put(CoinDatabaseHelper.COLUMN_HIGH_24H, coin.getHigh_24h());
            values.put(CoinDatabaseHelper.COLUMN_LOW_24H, coin.getLow_24h());
            values.put(CoinDatabaseHelper.COLUMN_PRICE_CHANGE_24H, coin.getPrice_change_24h());
            values.put(CoinDatabaseHelper.COLUMN_PRICE_CHANGE_PERCENTAGE_24H, coin.getPrice_change_percentage_24h());
            values.put(CoinDatabaseHelper.COLUMN_MARKET_CAP_CHANGE_24H, coin.getMarket_cap_change_24h());
            values.put(CoinDatabaseHelper.COLUMN_MARKET_CAP_CHANGE_PERCENTAGE_24H, coin.getMarket_cap_change_percentage_24h());
            values.put(CoinDatabaseHelper.COLUMN_CIRCULATING_SUPPLY, coin.getCirculating_supply());
            values.put(CoinDatabaseHelper.COLUMN_TOTAL_SUPPLY, coin.getTotal_supply());
            values.put(CoinDatabaseHelper.COLUMN_MAX_SUPPLY, coin.getMax_supply());
            values.put(CoinDatabaseHelper.COLUMN_ATH, coin.getAth());
            values.put(CoinDatabaseHelper.COLUMN_ATH_CHANGE_PERCENTAGE, coin.getAth_change_percentage());
            values.put(CoinDatabaseHelper.COLUMN_ATH_DATE, coin.getAth_date());
            values.put(CoinDatabaseHelper.COLUMN_ATL, coin.getAtl());
            values.put(CoinDatabaseHelper.COLUMN_ATL_CHANGE_PERCENTAGE, coin.getAtl_change_percentage());
            values.put(CoinDatabaseHelper.COLUMN_ATL_DATE, coin.getAtl_date());
            if(coin.getRoi()==null ){
                values.put(CoinDatabaseHelper.COLUMN_ROI,"");
            }else{
                values.put(CoinDatabaseHelper.COLUMN_ROI, coin.getRoi().toJson());
            }
            values.put(CoinDatabaseHelper.COLUMN_LAST_UPDATED, coin.getLast_updated());
            values.put(CoinDatabaseHelper.COLUMN_IMAGE_BITMAP, coin.getImageBitmap().toString());

            valuesList.add(values);
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

    }

    public Coin searchCoinById (String id) throws JSONException {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + CoinDatabaseHelper.COLUMN_ID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() == 0) return null;

        cursor.moveToFirst();
        // y así sucesivamente para todos los demás campos de la tabla
        id = cursor.getString(0);
        String symbol = cursor.getString(1);
        String name = cursor.getString(2);
        byte[] image = cursor.getBlob(3);
        Bitmap imagebitmap = new ImageManager().getBitmapFromBLOB(image);

        double current_price = cursor.getDouble(4);
        double market_cap = cursor.getDouble(5);
        int market_cap_rank = cursor.getInt(6);
        double fully_diluted_valuation = cursor.getDouble(7);
        double total_volume = cursor.getDouble(8);
        double high_24h = cursor.getDouble(9);
        double low_24h = cursor.getDouble(10);
        double price_change_24h = cursor.getDouble(11);
        double price_change_percentage_24h = cursor.getDouble(12);
        double market_cap_change_24h = cursor.getDouble(13);
        double market_cap_change_percentage_24h = cursor.getDouble(14);
        double circulating_supply = cursor.getDouble(15);
        double total_supply = cursor.getDouble(16);
        double max_supply = cursor.getDouble(17);
        double ath = cursor.getDouble(18);
        double ath_change_percentage = cursor.getDouble(19);
        String ath_date = cursor.getString(20);
        double atl = cursor.getDouble(21);
        double atl_change_percentage = cursor.getDouble(22);
        String atl_date = cursor.getString(23);

        Coin.Roi roi = null;
        if(!cursor.isNull(24)) {
            JSONObject jsonObject = new JSONObject(cursor.getString(24));
            double times = jsonObject.getDouble("times");
            String currency = jsonObject.getString("currency");
            double percentage = jsonObject.getDouble("percentage");

            roi = new Coin.Roi();
            roi.setCurrency(currency);
            roi.setTimes(times);
            roi.setPercentage(percentage);

        }
        String last_updated = cursor.getString(25);

        //Creo el objeto
        Coin coin = new Coin();
        coin.setId(id);
        coin.setSymbol(symbol);
        coin.setName(name);
        coin.setImageBitmap(imagebitmap);
        coin.setCurrent_price(current_price);
        coin.setMarket_cap_rank(market_cap_rank);
        coin.setMarket_cap(market_cap);
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
        coin.setAth_change_percentage(ath_change_percentage);
        coin.setAth(ath);
        coin.setAth_date(ath_date);
        coin.setAtl(atl);
        coin.setAtl_change_percentage(atl_change_percentage);
        coin.setAtl_date(atl_date);
        coin.setRoi(roi);
        coin.setLast_updated(last_updated);

        cursor.close();
        return coin;
    }

    public List<Coin> getCoinsMarketCapRange (int min, int max) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                CoinDatabaseHelper.COLUMN_ID,
                CoinDatabaseHelper.COLUMN_SYMBOL,
                CoinDatabaseHelper.COLUMN_NAME,
                CoinDatabaseHelper.COLUMN_IMAGE,
                CoinDatabaseHelper.COLUMN_CURRENT_PRICE,
                CoinDatabaseHelper.COLUMN_MARKET_CAP,
                CoinDatabaseHelper.COLUMN_MARKET_CAP_RANK,
                // Otros campos omitidos
        };

        String selection = CoinDatabaseHelper.COLUMN_MARKET_CAP_RANK + " BETWEEN ? AND ?";
        String[] selectionArgs = {String.valueOf(min), String.valueOf(max)};

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        List<Coin> coins = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(CoinDatabaseHelper.COLUMN_ID));
            String symbol = cursor.getString(cursor.getColumnIndexOrThrow(CoinDatabaseHelper.COLUMN_SYMBOL));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(CoinDatabaseHelper.COLUMN_NAME));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(CoinDatabaseHelper.COLUMN_IMAGE));
            double currentPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(CoinDatabaseHelper.COLUMN_CURRENT_PRICE));
            double marketCap = cursor.getDouble(cursor.getColumnIndexOrThrow(CoinDatabaseHelper.COLUMN_MARKET_CAP));
            int marketCapRank = cursor.getInt(cursor.getColumnIndexOrThrow(CoinDatabaseHelper.COLUMN_MARKET_CAP_RANK));

            Coin coin = new Coin();
            coin.setId(id);
            coin.setSymbol(symbol);
            coin.setName(name);
            coin.setImageBytes(image);
            coin.setCurrent_price(currentPrice);
            coin.setMarket_cap(marketCap);
            coin.setMarket_cap_rank(marketCapRank);
            coins.add(coin);
        }
        return coins;
    }
}
