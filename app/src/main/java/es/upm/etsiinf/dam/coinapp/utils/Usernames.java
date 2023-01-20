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

    public static void logout(Context context){
        //1. Eliminar access token si lo hubiera (checkbox)
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_preferences",MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token","");

        //se ha marcado checkbox, por lo tanto borro el token.
        if(accessToken.length()>0){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("access_token");
            editor.apply();
        }

        //2. Eliminar datos de login (username/email)
        SharedPreferences prefs = context.getSharedPreferences("login_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("username");
        editor.remove("email");
        editor.apply();


    }
}
