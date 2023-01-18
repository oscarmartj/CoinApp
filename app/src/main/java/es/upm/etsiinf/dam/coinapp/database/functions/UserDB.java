package es.upm.etsiinf.dam.coinapp.database.functions;

import static es.upm.etsiinf.dam.coinapp.database.UserDatabaseHelper.COLUMN_EMAIL;
import static es.upm.etsiinf.dam.coinapp.database.UserDatabaseHelper.COLUMN_PASSWORD;
import static es.upm.etsiinf.dam.coinapp.database.UserDatabaseHelper.COLUMN_PROFILEIMAGE;
import static es.upm.etsiinf.dam.coinapp.database.UserDatabaseHelper.COLUMN_USERNAME;
import static es.upm.etsiinf.dam.coinapp.database.UserDatabaseHelper.TABLE_NAME_USERS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.security.NoSuchAlgorithmException;

import es.upm.etsiinf.dam.coinapp.database.UserDatabaseHelper;
import es.upm.etsiinf.dam.coinapp.modelos.User;
import es.upm.etsiinf.dam.coinapp.utils.Security;


public class UserDB {
    private UserDatabaseHelper dbHelper;
    private final Security security = new Security();

    public UserDB (Context context) {
        dbHelper = new UserDatabaseHelper(context);
    }


    public boolean insertUser (String username, String password, String email, byte[] profileImage) throws NoSuchAlgorithmException {
        // Obtiene la base de datos en modo escritura
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Crea un nuevo mapa de valores donde se almacenarán los valores
        ContentValues values = new ContentValues();

        // Cifra la contraseña utilizando el algoritmo SHA-256
        password = security.encryptPassword(password);

        // Asigna valores a las columnas
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PROFILEIMAGE,profileImage);

        // Inserta un nuevo registro en la tabla
        long rowId = db.insert(TABLE_NAME_USERS, null, values);
        Log.i("RegisterActivity", "rowId="+rowId);
        db.close();
        return rowId != -1;
    }

    public void updateProfileImage(String email, byte[] image) {
        // Obtiene la base de datos en modo escritura
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Crea un nuevo mapa de valores donde se almacenará la imagen
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROFILEIMAGE, image);

        // Actualiza el registro de la tabla de usuarios con el email especificado
        db.update(TABLE_NAME_USERS, values, COLUMN_EMAIL + "= ?", new String[] { email });
        db.close();
    }


    public User getUserByEmail(String email) {
        User user = null;

        String query = "SELECT * FROM " + TABLE_NAME_USERS + " WHERE " + COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        try (
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery(query, selectionArgs)
        ) {
            if (cursor.moveToFirst()) {
                String username = cursor.getString(0);
                String password = cursor.getString(1);
                byte[] profileImage = cursor.getBlob(3);
                user = new User(username, password, email, profileImage);
            }
        } catch (Exception e) {
            Log.e("UserDatabaseHelper", "Error al obtener usuario por email", e);
        }
        return user;
    }
}
