package es.upm.etsiinf.dam.coinapp.AsyncTask;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import es.upm.etsiinf.dam.coinapp.modelos.Coin;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;

public class NotificationCoinsThread implements Runnable {

    private final OnCoinsReceivedListener listener;
    private List<String> coins;
    private String API_URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=";

    public interface OnCoinsReceivedListener {
        void onCoinsReceived(List<Coin> coins);
    }
    public NotificationCoinsThread (List<String> coins, OnCoinsReceivedListener listener) {
        this.coins = coins;
        for (int i = 0; i < coins.size(); i++) {
            API_URL += (i == 0) ? coins.get(i) : ("," + coins.get(i));
        }
        this.listener = listener;
    }

    @Override
    public void run () {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONArray coinsJson = new JSONArray(response.toString());

                List<Coin> coins = DataManager.setCoins(coinsJson);

                listener.onCoinsReceived(coins);

            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
