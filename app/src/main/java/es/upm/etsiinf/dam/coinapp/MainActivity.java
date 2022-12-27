package es.upm.etsiinf.dam.coinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import es.upm.etsiinf.dam.coinapp.database.functions.CoinDB;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;
import es.upm.etsiinf.dam.coinapp.utils.ImageManager;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tw = findViewById(R.id.textview_cripto);
        ImageView iw = findViewById(R.id.imageView);
        ImageView iw2 = findViewById(R.id.imageView2);
        ImageView iw3 = findViewById(R.id.imageView3);

        CoinDB db = new CoinDB(this);
        List<Coin> coins = db.getCoinsMarketCapRange(1,3);

        Bitmap bitmap = new ImageManager().getBitmapFromBLOB(coins.get(0).getImageBytes());
        iw.setImageBitmap(bitmap);

        Bitmap bitmap2 = new ImageManager().getBitmapFromBLOB(coins.get(1).getImageBytes());
        iw2.setImageBitmap(bitmap2);
        Bitmap bitmap3 = new ImageManager().getBitmapFromBLOB(coins.get(2).getImageBytes());
        iw3.setImageBitmap(bitmap3);


        tw.setText((int) coins.get(0).getCurrent_price()+"");
    }


}
