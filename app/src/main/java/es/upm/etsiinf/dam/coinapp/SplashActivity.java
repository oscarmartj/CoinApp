package es.upm.etsiinf.dam.coinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import java.lang.reflect.Type;
import java.util.List;

import es.upm.etsiinf.dam.coinapp.interfaces.CoinGeckoApi;
import es.upm.etsiinf.dam.coinapp.modelos.CoinMarketData;

import android.os.Handler;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;



@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private List<CoinMarketData> coinMarketDataList;
    private CoinGeckoApi coinGeckoApi;
    private int splashDuration;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.heartbeatsound);
        // Iniciar la animación del logo
        ImageView logoImageView = findViewById(R.id.logo);
        logoImageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.heartbeat));
        mediaPlayer.start();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.coingecko.com/api/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        coinGeckoApi = retrofit.create(CoinGeckoApi.class);

        Call<List<CoinMarketData>> call = coinGeckoApi.getTopCoins("usd", "market_cap_desc", 100, 1);
        call.enqueue(new Callback<List<CoinMarketData>>() {
            @Override
            public void onResponse (Call<List<CoinMarketData>> call, Response<List<CoinMarketData>> response) {
                if(response.isSuccessful()) {
                    coinMarketDataList = response.body();
                    splashDuration = coinMarketDataList.size() * 20;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run () {
                            // Obtener el Type de una lista de objetos CoinMarketData
                            Type coinMarketDataType = new TypeToken<List<CoinMarketData>>(){}.getType();

                            // Crear una instancia de Gson que se puede usar para convertir una lista de objetos CoinMarketData en un String
                            Gson gson = new GsonBuilder().create();

                            // Convertir la lista de objetos CoinMarketData en un String usando la instancia de Gson
                            String coinMarketDataString = gson.toJson(coinMarketDataList, coinMarketDataType);

                            // Crear un Intent que se puede usar para iniciar la MainActivity y pasarle el String de datos de CoinMarketData como extra
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.putExtra("coin_data", coinMarketDataString);

                            // Iniciar la MainActivity y finalizar la SplashActivity
                            mediaPlayer.stop();
                            startActivity(intent);
                            finish();


                        }
                    }, splashDuration);
                } else {
                    Log.e("SplashActivity", "Error en la llamada a la API: " + response.errorBody());
                }
            }

            @Override
            public void onFailure (Call<List<CoinMarketData>> call, Throwable t) {
                Log.e("SplashActivity", "Error en la llamada a la API: " + t.getMessage());
            }
        });
    }

}

