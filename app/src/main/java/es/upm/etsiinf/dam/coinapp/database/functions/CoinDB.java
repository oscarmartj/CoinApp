package es.upm.etsiinf.dam.coinapp.database.functions;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;
import es.upm.etsiinf.dam.coinapp.utils.ImageManager;

public class CoinDB {
    private CoinDatabaseHelper dbHelper;

    public CoinDB(Context context){
        dbHelper = new CoinDatabaseHelper(context);
    }

    public void insertCoins(List<Coin> coins) throws IOException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<ContentValues> valuesList = new ArrayList<>();

        for (Coin coin : coins) {

            ImageManager imageManager = ImageManager.getInstance(coin.getImage());
            byte[] byteImage = imageManager.getBytesFromBitmap();

            ContentValues values = new ContentValues();
            values.put(CoinDatabaseHelper.COLUMN_ID, coin.getId());
            values.put(CoinDatabaseHelper.COLUMN_SYMBOL, coin.getSymbol());
            values.put(CoinDatabaseHelper.COLUMN_NAME, coin.getName());
            values.put(CoinDatabaseHelper.COLUMN_IMAGE, byteImage);
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
            values.put(CoinDatabaseHelper.COLUMN_ROI, coin.getRoi().toJson());
            values.put(CoinDatabaseHelper.COLUMN_LAST_UPDATED, coin.getLast_updated());

            valuesList.add(values);
        }
        try{
            db.beginTransaction();
            for(int i=0; i<valuesList.size(); i++){
                db.insert(CoinDatabaseHelper.TABLE_NAME, null, valuesList.get(i));
            }
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
        }

    }
}
