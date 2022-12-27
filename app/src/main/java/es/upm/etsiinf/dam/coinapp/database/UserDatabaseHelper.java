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
    private static final String TABLE_NAME_USERS = "users";

    // Columnas de la tabla de usuarios
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";

    // Sentencia SQL para crear la tabla de usuarios
    private static final String SQL_CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_NAME_USERS + "(" +
                    COLUMN_USERNAME + " TEXT PRIMARY KEY," +
                    COLUMN_PASSWORD + " TEXT," +
                    COLUMN_EMAIL + " TEXT UNIQUE" + ")";


    private final Security security = new Security();

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

    public boolean insertUser (String username, String password, String email) throws NoSuchAlgorithmException {
        // Obtiene la base de datos en modo escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // Crea un nuevo mapa de valores donde se almacenarán los valores
        ContentValues values = new ContentValues();

        // Cifra la contraseña utilizando el algoritmo SHA-256
        password = security.encryptPassword(password);

        // Asigna valores a las columnas
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);

        // Inserta un nuevo registro en la tabla
        long rowId = db.insert(TABLE_NAME_USERS, null, values);
        Log.i("RegisterActivity", "rowId="+rowId);
        db.close();
        return rowId != -1;
    }

    public User getUserByEmail(String email) {
        User user = null;

        String query = "SELECT * FROM " + TABLE_NAME_USERS + " WHERE " + COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        try (
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.rawQuery(query, selectionArgs)
        ) {
            if (cursor.moveToFirst()) {
                String username = cursor.getString(0);
                String password = cursor.getString(1);
                user = new User(username, password, email);
            }
        } catch (Exception e) {
            Log.e("UserDatabaseHelper", "Error al obtener usuario por email", e);
        }
        return user;
    }



}
