package es.upm.etsiinf.dam.coinapp.AsyncTask;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import es.upm.etsiinf.dam.coinapp.modelos.Coin;

public class CoinGeckoThread implements Runnable {

    private static final String API_URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=eur&per_page=100&";
    private int page;
    private Handler handler;
    private int maxPages = 20;

    public CoinGeckoThread (int page, Handler handler) {
        this.page = page;
        this.handler = handler;
    }

    @Override
    public void run () {
        try {
            if(page<=maxPages){
                URL url = new URL(API_URL + "?page=" + page);
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

                    List<Coin> coins = new ArrayList<>();

                    // Recorre la lista de criptomonedas en el objeto JSON y crea objetos Coin para cada una de ellas
                    for (int i = 0; i < coinsJson.length(); i++) {
                        JSONObject coinJson = coinsJson.getJSONObject(i);
                        String id = coinJson.getString("id");
                        String name = coinJson.getString("name");
                        String symbol = coinJson.getString("symbol");
                        String image = coinJson.getString("image");
                        double current_price = coinJson.getDouble("current_price");
                        double market_cap = coinJson.getDouble("market_cap");
                        int market_cap_rank = coinJson.getInt("market_cap_rank");
                        double fully_diluted_valuation = coinJson.getDouble("fully_diluted_valuation");
                        double total_volume = coinJson.getDouble("total_volume");
                        double high_24h = coinJson.getDouble("high_24h");
                        double low_24h = coinJson.getDouble("low_24h");
                        double price_change_24h = coinJson.getDouble("price_change_24h");
                        double price_change_percentage_24h = coinJson.getDouble("price_change_percentage_24h");
                        double market_cap_change_24h = coinJson.getDouble("market_cap_change_24h");
                        double market_cap_change_percentage_24h = coinJson.getDouble("market_cap_change_percentage_24h");
                        double circulating_supply = coinJson.getDouble("circulating_supply");
                        double total_supply = coinJson.getDouble("total_supply");
                        double max_supply = coinJson.getDouble("max_supply");
                        double ath = coinJson.getDouble("ath");
                        double ath_change_percentage = coinJson.getDouble("ath_change_percentage");
                        String ath_date = coinJson.getString("ath_date");
                        double atl = coinJson.getDouble("atl");
                        double atl_change_percentage = coinJson.getDouble("atl_change_percentage");
                        String atl_date = coinJson.getString("atl_date");
                        JSONObject roiJson = coinJson.getJSONObject("roi");
                        Coin.Roi roi = new Coin.Roi();
                        roi.setTimes(roiJson.getDouble("times"));
                        roi.setCurrency(roiJson.getString("currency"));
                        roi.setPercentage(roiJson.getDouble("percentage"));
                        String last_updated = coinJson.getString("last_updated");

                        Coin coin = new Coin();
                        coin.setId(id);
                        coin.setName(name);
                        coin.setSymbol(symbol);
                        coin.setImage(image);
                        coin.setCurrent_price(current_price);
                        coin.setMarket_cap(market_cap);
                        coin.setMarket_cap_rank(market_cap_rank);
                        coin.setFully_diluted_valuation(fully_diluted_valuation);
                        coin.setTotal_volume(total_volume);
                        coin.setHigh_24h(high_24h);
                        coin.setLow_24h(low_24h);
                        coin.setPrice_change_24h(price_change_24h);
                        coin.setPrice_change_percentage_24h(price_change_percentage_24h);
                        coin.setMarket_cap_change_24h(market_cap_change_24h);
                        coin.setMarket_cap_change_percentage_24h(market_cap_change_percentage_24h);
                        coin.setCirculating_supply(circulating_supply);
                        coin.setTotal_supply(total_supply);
                        coin.setMax_supply(max_supply);
                        coin.setAth(ath);
                        coin.setAth_change_percentage(ath_change_percentage);
                        coin.setAth_date(ath_date);
                        coin.setAtl(atl);
                        coin.setAtl_change_percentage(atl_change_percentage);
                        coin.setAtl_date(atl_date);
                        coin.setRoi(roi);
                        coin.setLast_updated(last_updated);

                        // Añade el objeto Coin a la lista
                        coins.add(coin);
                    }
                    handler.post(() -> {
                        Message message = handler.obtainMessage(0, coins);
                        message.sendToTarget();
                    });

                }else{
                    Message message = handler.obtainMessage(1, null);
                    message.sendToTarget();
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}

