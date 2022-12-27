package es.upm.etsiinf.dam.coinapp.utils;

import android.content.Intent;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.upm.etsiinf.dam.coinapp.modelos.Coin;

public class DataManager {
    public static List<Coin> getCoinsFromIntent(Intent intent) {
        List<Coin> coins = intent.getParcelableArrayListExtra("coins");
        if(coins.size()<=0){
            return new ArrayList<>();
        } else {
            return coins;
        }
    }

    public static void setCoinsInIntent(List<Coin> coins, Intent intent) {
        intent.putParcelableArrayListExtra("coins", new ArrayList<>(coins));
    }



}
