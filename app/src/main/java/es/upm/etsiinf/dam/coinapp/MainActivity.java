package es.upm.etsiinf.dam.coinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import es.upm.etsiinf.dam.coinapp.modelos.Coin;

public class MainActivity extends AppCompatActivity {

    private List<Coin> coins;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Coin[] coinsArray = (Coin[]) getIntent().getParcelableArrayExtra("coins");
        if(coinsArray.length<=0){

        }else{
            coins= Arrays.asList(coinsArray);
        }
        TextView tw = findViewById(R.id.textview_cripto);

        tw.setText(coins.get(0).getId());
    }


}
