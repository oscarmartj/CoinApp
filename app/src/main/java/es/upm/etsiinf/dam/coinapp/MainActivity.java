package es.upm.etsiinf.dam.coinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import es.upm.etsiinf.dam.coinapp.modelos.CoinMarketData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener el Intent que se utilizó para iniciar la MainActivity
        Intent intent = getIntent();

        // Obtener el String de datos de CoinMarketData del Intent
        String coinMarketDataString = intent.getStringExtra("coin_data");

        // Obtener el Type de una lista de objetos CoinMarketData
        Type coinMarketDataType = new TypeToken<List<CoinMarketData>>() {
        }.getType();

        // Crear una instancia de Gson que se puede usar para convertir una lista de objetos CoinMarketData en un String
        Gson gson = new GsonBuilder().create();

        // Convertir el String de datos de CoinMarketData en una lista de objetos CoinMarketData usando la instancia de Gson
        List<CoinMarketData> coinMarketDataList = gson.fromJson(coinMarketDataString, coinMarketDataType);

        TextView tw = findViewById(R.id.textview_cripto);
        tw.setText("Hola Alicia!");
        //tw.setText(coinMarketDataList.get(0).getName());
    }


}
