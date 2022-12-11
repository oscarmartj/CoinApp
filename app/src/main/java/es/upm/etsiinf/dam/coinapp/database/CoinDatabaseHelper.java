package es.upm.etsiinf.dam.coinapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CoinDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CoinDatabase";

    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "coins";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SYMBOL = "symbol";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_CURRENT_PRICE = "current_price";
    public static final String COLUMN_MARKET_CAP = "market_cap";
    public static final String COLUMN_MARKET_CAP_RANK = "market_cap_rank";
    public static final String COLUMN_FULLY_DILUTED_VALUATION = "fully_diluted_valuation";
    public static final String COLUMN_TOTAL_VOLUME = "total_volume";
    public static final String COLUMN_HIGH_24H = "high_24h";
    public static final String COLUMN_LOW_24H = "low_24h";
    public static final String COLUMN_PRICE_CHANGE_24H = "price_change_24h";
    public static final String COLUMN_PRICE_CHANGE_PERCENTAGE_24H = "price_change_percentage_24h";
    public static final String COLUMN_MARKET_CAP_CHANGE_24H = "market_cap_change_24h";
    public static final String COLUMN_MARKET_CAP_CHANGE_PERCENTAGE_24H = "market_cap_change_percentage_24h";
    public static final String COLUMN_CIRCULATING_SUPPLY = "circulating_supply";
    public static final String COLUMN_TOTAL_SUPPLY = "total_supply";
    public static final String COLUMN_MAX_SUPPLY = "max_supply";
    public static final String COLUMN_ATH = "ath";
    public static final String COLUMN_ATH_CHANGE_PERCENTAGE = "ath_change_percentage";
    public static final String COLUMN_ATH_DATE = "ath_date";
    public static final String COLUMN_ATL = "atl";
    public static final String COLUMN_ATL_CHANGE_PERCENTAGE = "atl_change_percentage";
    public static final String COLUMN_ATL_DATE = "atl_date";
    public static final String COLUMN_ROI = "roi";
    public static final String COLUMN_LAST_UPDATED = "last_updated";

    private static final String SQL_CREATE_TABLE_COINS =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " TEXT PRIMARY KEY,"
                    + COLUMN_SYMBOL + " TEXT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_IMAGE + " BLOB,"
                    + COLUMN_CURRENT_PRICE + " REAL,"
                    + COLUMN_MARKET_CAP + " REAL,"
                    + COLUMN_MARKET_CAP_RANK + " INTEGER,"
                    + COLUMN_FULLY_DILUTED_VALUATION + " REAL,"
                    + COLUMN_TOTAL_VOLUME + " REAL,"
                    + COLUMN_HIGH_24H + " REAL,"
                    + COLUMN_LOW_24H + " REAL,"
                    + COLUMN_PRICE_CHANGE_24H + " REAL,"
                    + COLUMN_PRICE_CHANGE_PERCENTAGE_24H + " REAL,"
                    + COLUMN_MARKET_CAP_CHANGE_24H + " REAL,"
                    + COLUMN_MARKET_CAP_CHANGE_PERCENTAGE_24H + " REAL,"
                    + COLUMN_CIRCULATING_SUPPLY + " REAL,"
                    + COLUMN_TOTAL_SUPPLY + " REAL,"
                    + COLUMN_MAX_SUPPLY + " REAL,"
                    + COLUMN_ATH + " REAL,"
                    + COLUMN_ATH_CHANGE_PERCENTAGE + " REAL,"
                    + COLUMN_ATH_DATE + " TEXT,"
                    + COLUMN_ATL + " REAL,"
                    + COLUMN_ATL_CHANGE_PERCENTAGE + " REAL,"
                    + COLUMN_ATL_DATE + " TEXT,"
                    + COLUMN_ROI + " TEXT,"
                    + COLUMN_LAST_UPDATED + " TEXT)";

    public CoinDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_COINS);

    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
