package es.upm.etsiinf.dam.coinapp.main.ui.ranking;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.List;

import es.upm.etsiinf.dam.coinapp.AsyncTask.CoinGeckoThread;
import es.upm.etsiinf.dam.coinapp.database.functions.CoinDB;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;

public class RankingViewModel extends ViewModel {
    
    private MutableLiveData<List<Coin>> coins;
    private MutableLiveData<Boolean> isRefreshing;
    private int page = 1;
    private boolean isLoading = false;
    private Handler handler;
    private Handler handlerRefreshing;

    public RankingViewModel () {
        coins = new MutableLiveData<>();
        coins.setValue(new LinkedList<>());
        isRefreshing = new MutableLiveData<>();
        isRefreshing.setValue(false);
        loadMoreCoins();
    }

    public LiveData<List<Coin>> getCoins() {
        return coins;
    }
    public LiveData<Boolean> getIsRefreshing() {
        return isRefreshing;
    }

    public void loadMoreCoins (Context context, boolean connected) {
        if(isLoading) {
            return;
        } else {
            this.handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage (Message msg) {
                    switch (msg.what) {
                        case 0: //OK
                            List<Coin> newCoins = (List<Coin>)msg.obj;
                            if(newCoins != null){
                                List<Coin> currentCoins = coins.getValue();
                                if(currentCoins == null){
                                    currentCoins = new LinkedList<>();
                                }
                                int currentSize = currentCoins.size();
                                currentCoins.addAll(newCoins);
                                coins.setValue(currentCoins);
                                Log.i("NewCoins","Coins final ->"+currentCoins.toString());
                            }
                            isLoading = false;
                            page++;
                            break;
                        case 1: //ERROR
                            isLoading=false;
                            break;
                    }
                }
            };
            isLoading = true;
            if(connected){
                CoinGeckoThread coinGeckoThread = new CoinGeckoThread(this.page, handler);
                new Thread(coinGeckoThread).start();
            }else{
                CoinDB coinDB = new CoinDB(context);
                int initialcoin = DataManager.getInitialRange(this.page);
                List<Coin> coinsFromDB = coinDB.getCoinsMarketCapRange(initialcoin,initialcoin+19);
                handler.sendMessage(handler.obtainMessage(0, coinsFromDB));
            }
        }
    }
    public void refreshCoins(){
        isRefreshing.setValue(true);

        this.handlerRefreshing = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage (Message msg) {
                switch (msg.what) {
                    case 0: //OK
                        List<Coin> newCoins = (List<Coin>)msg.obj;
                        Log.i("updateCoins",newCoins.toString());
                        coins.postValue(newCoins);
                        isRefreshing.postValue(false);
                        break;
                    case 1: //ERROR
                        isRefreshing.setValue(false);
                        break;
                }
            }
        };
        new Thread(new CoinGeckoThread(this.coins.getValue(),handlerRefreshing)).start();

    }
    public boolean isLoading () {
        return isLoading;
    }

    public void setConnected (boolean connected) {
        this.isConnected = connected;
    }

}