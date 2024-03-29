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
import java.util.LinkedList;
import java.util.List;

import es.upm.etsiinf.dam.coinapp.modelos.Coin;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;

public class CoinGeckoThread implements Runnable {

    private String API_URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=20&";
    private int page;
    private Handler handler;
    private int maxPages = 50;
    private List<Coin> coins = new LinkedList<>();

    public CoinGeckoThread (int page, Handler handler) {
        this.page = page;
        this.handler = handler;
    }

    public CoinGeckoThread(List<Coin> coins, Handler handler){
        this.coins=coins;
        this.handler= handler;
    }

    @Override
    public void run () {
        try {
            URL url = coins.size()>0?
                    new URL("https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page="+coins.size()+"&page=1")
                    :
                    new URL(API_URL + "page=" + page);
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

                handler.post(() -> {
                    Message message = handler.obtainMessage(0, coins);
                    message.sendToTarget();
                });

            }else{
                Message message = handler.obtainMessage(1, null);
                message.sendToTarget();
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}

