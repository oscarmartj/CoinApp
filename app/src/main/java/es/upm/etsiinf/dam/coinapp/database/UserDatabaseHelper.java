package es.upm.etsiinf.dam.coinapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.NoSuchAlgorithmException;

import es.upm.etsiinf.dam.coinapp.modelos.User;
import es.upm.etsiinf.dam.coinapp.utils.Security;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    // Nombre de la base de datos
    private static final String DATABASE_NAME = "user_database";

    // Versión de la base de datos
    private static final int DATABASE_VERSION = 1;

    // Nombre de la tabla que almacena los usuarios
    public static final String TABLE_NAME_USERS = "users";

    // Columnas de la tabla de usuarios
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PROFILEIMAGE = "profileimage";

    // Sentencia SQL para crear la tabla de usuarios
    private static final String SQL_CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_NAME_USERS + "(" +
                    COLUMN_USERNAME + " TEXT PRIMARY KEY," +
                    COLUMN_PASSWORD + " TEXT," +
                    COLUMN_EMAIL + " TEXT UNIQUE," +
                    COLUMN_PROFILEIMAGE + " BLOB" + ")";



    public UserDatabaseHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        // Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(SQL_CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        // En este ejemplo, se elimina la tabla anterior y se crea una nueva
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS);
        onCreate(db);
    }

}
