package es.upm.etsiinf.dam.coinapp.AsyncTask;

import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import es.upm.etsiinf.dam.coinapp.modelos.Coin;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;

public class UpdateServiceThread implements Runnable{

    private String API_URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=";

    OnCoinsServiceReceivedListener listener;
    private boolean flag;

    public interface OnCoinsServiceReceivedListener {
        void onCoinsServiceReceived(List<Coin> coins);
    }

    public UpdateServiceThread (boolean flag, OnCoinsServiceReceivedListener listener) {
        this.listener = listener;
        this.flag = flag;
    }

    @Override
    public void run () {
        List<Coin> coins = new ArrayList<>();
        try{
            for(int i = 0; (flag ?i<1:i<10); i++){
                URL url = new URL(API_URL+i);
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

                    List<Coin> resultado = DataManager.setCoins(coinsJson);
                    coins.addAll(resultado);
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            if(coins.size()>0){
                listener.onCoinsServiceReceived(coins);
            }else{
                listener.onCoinsServiceReceived(null);
            }
        }
    }
}
