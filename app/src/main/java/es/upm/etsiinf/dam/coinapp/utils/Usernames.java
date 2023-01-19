package es.upm.etsiinf.dam.coinapp.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import es.upm.etsiinf.dam.coinapp.modelos.User;

public class Usernames {

    public static String firstToUppercase(String displayName){
        if (displayName.isEmpty()) {
            return "";
        }
        char firstLetter = displayName.charAt(0);
        if (Character.isLetter(firstLetter) && !Character.isUpperCase(firstLetter)) {
            firstLetter = Character.toUpperCase(firstLetter);
            return firstLetter + displayName.substring(1);
        }
        return displayName;
    }

    public static void updateUserPreferences(Context context, User user){
        SharedPreferences prefs = context.getSharedPreferences("login_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", user.getUsername());
        editor.putString("email", user.getEmail());
        editor.apply();


    }
}
