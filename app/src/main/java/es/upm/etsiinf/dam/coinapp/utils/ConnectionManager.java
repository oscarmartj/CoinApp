package es.upm.etsiinf.dam.coinapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;


public class ConnectionManager {

    public interface OnNetworkStatusCallback{
        void onNetworkStatusCallback();
    }

    private Context context;
    private boolean isConnected;

    public ConnectionManager(Context context){
        this.context=context;
        this.isConnected=isConnected(context);
    }
    public void connectionCheck(OnNetworkStatusCallback onNetworkStatusCallback){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                if(!isConnected()){
                    onNetworkStatusCallback.onNetworkStatusCallback();
                    setConnected(true);
                }
            }

            @Override
            public void onUnavailable () {
                setConnected(false);
            }

            @Override
            public void onLost (@NonNull Network network) {
                setConnected(false);
                onNetworkStatusCallback.onNetworkStatusCallback();

            }
        };

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    private boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public boolean isConnected () {
        return isConnected;
    }

    public void setConnected (boolean connected) {
        isConnected = connected;
    }



}
