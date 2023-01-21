package es.upm.etsiinf.dam.coinapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import es.upm.etsiinf.dam.coinapp.SplashActivity;

public class ConnectionManager {
    public static void connectionCheck (Context context, OnConnectionCallback onConnectionCallback){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                /*
                Intent intent = new Intent(context.getApplicationContext(), activity);
                context.startActivity(intent);*/
                onConnectionCallback.onConnectionCallback();
            }
        };
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    public interface OnConnectionCallback{
        void onConnectionCallback();
    }
}
