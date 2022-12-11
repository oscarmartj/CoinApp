package es.upm.etsiinf.dam.coinapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import es.upm.etsiinf.dam.coinapp.AsyncTask.CoinGeckoThread;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import es.upm.etsiinf.dam.coinapp.ui.login.LoginActivity;
import es.upm.etsiinf.dam.coinapp.utils.TokenManager;



@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private List<Coin> coins;
    private int splashDuration;
    private SharedPreferences preferences;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);


        //comprobar si el usuario está registrado y conectado
        if (userIsLoggedIn()) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Si el usuario no está registrado y conectado, iniciar la pantalla de inicio de sesión despues de la splash


        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.heartbeatsound);
        // Iniciar la animación del logo
        ImageView logoImageView = findViewById(R.id.logo);
        logoImageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.heartbeat));
        mediaPlayer.start();

        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        // Almacenar los datos de las criptomonedas recibidas
                        coins = (List<Coin>) msg.obj;
                        // Iniciar la actividad principal
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        Parcelable[] coinsArray = coins.toArray(new Coin[coins.size()]);
                        intent.putExtra("coins",coinsArray);
                        mediaPlayer.stop();
                        startActivityForResult(intent, Activity.RESULT_OK);
                        break;
                    case 1:
                        Toast toast = Toast.makeText(getApplicationContext(), "No tiene acceso a internet.", Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                }
            }
        };

        Thread thread = new Thread(new CoinGeckoThread(1, handler));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        }
    }

    private boolean userIsLoggedIn() {
        return TokenManager.userIsLoggedIn(TokenManager.getAccessTokenFromLocalStorage(preferences));
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Activity.RESULT_OK && resultCode==Activity.RESULT_OK){
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

