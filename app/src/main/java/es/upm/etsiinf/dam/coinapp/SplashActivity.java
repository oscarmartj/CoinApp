package es.upm.etsiinf.dam.coinapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import es.upm.etsiinf.dam.coinapp.AsyncTask.CoinGeckoThread;
import es.upm.etsiinf.dam.coinapp.database.CoinDatabaseHelper;
import es.upm.etsiinf.dam.coinapp.database.functions.CoinDB;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import es.upm.etsiinf.dam.coinapp.ui.login.LoginActivity;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;
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

        ImageView logoImageView = findViewById(R.id.logo);
        TextView noInternetTW = findViewById(R.id.textview_centrado);

        //Si no tiene conexión a internet y no tiene datos de las monedas guardados en la base de datos
        if(!isConnected() && !hasData()){
            logoImageView.setVisibility(View.INVISIBLE);
            noInternetTW.setVisibility(View.VISIBLE);
            connectionCheck();
        }else if(!isConnected() && hasData()){

        }else {

            //comprobar si el usuario está registrado y conectado
            if(userIsLoggedIn()) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Si el usuario esta registrado y no conectado, iniciar la pantalla de inicio de sesión despues de la splash


                MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.heartbeatsound);
                // Iniciar la animación del logo
                logoImageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.heartbeat));
                mediaPlayer.start();

                final Handler handler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage (Message msg) {
                        switch (msg.what) {
                            case 0:
                                Log.i("handleMessage", "Entra en el case 0 del handler");
                                // Almacenar los datos de las criptomonedas recibidas
                                coins = (List<Coin>) msg.obj;
                                // Iniciar la actividad principal
                                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                DataManager.setCoinsInIntent(coins, intent);
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mediaPlayer.stop();
                                startActivity(intent);
                                break;
                            case 1:
                                Log.i("handleMessage", "Entra en el case 1 del handler");
                                Toast toast = Toast.makeText(getApplicationContext(), "No tiene acceso a internet.", Toast.LENGTH_SHORT);
                                toast.show();
                                break;
                        }
                    }
                };

                Log.i("Main", "Antes de crear el thread");
                Thread thread = new Thread(new CoinGeckoThread(1, handler));
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean userIsLoggedIn() {
        return TokenManager.userIsLoggedIn(TokenManager.getAccessTokenFromLocalStorage(preferences));
    }

    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private boolean hasData(){

        CoinDB coinDB = new CoinDB(this);
        int tamanyo_db = coinDB.getNumOfRecords();

        return tamanyo_db > 0;
    }

    private void connectionCheck(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(intent);
            }
        };
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);

    }
}

