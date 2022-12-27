package es.upm.etsiinf.dam.coinapp.utils;

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
}
