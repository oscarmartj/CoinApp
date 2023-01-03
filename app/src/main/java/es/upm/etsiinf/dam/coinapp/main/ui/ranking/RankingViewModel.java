package es.upm.etsiinf.dam.coinapp.main.ui.ranking;

import android.content.Intent;
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
import es.upm.etsiinf.dam.coinapp.modelos.Coin;

public class RankingViewModel extends ViewModel {
    
    private MutableLiveData<List<Coin>> coins;
    private int page = 1;
    private boolean isLoading = false;
    private Handler handler;

    public RankingViewModel () {
        coins = new MutableLiveData<>();
        coins.setValue(new LinkedList<>());
        loadMoreCoins();
    }

    public LiveData<List<Coin>> getCoins() {
        return coins;
    }

    public void loadMoreCoins() {
        if(isLoading) {
            return;
        } else {

            this.handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage (Message msg) {
                    switch (msg.what) {
                        case 0: //OK
                            List<Coin> newCoins = (List<Coin>)msg.obj;
                            Log.i("NewCoins",newCoins.toString());
                            if(newCoins != null){
                                List<Coin> currentCoins = coins.getValue();
                                Log.i("NewCoins","CurrentCoins ->"+currentCoins.toString());
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
            Log.i("PageThread",page+"");
            CoinGeckoThread coinGeckoThread = new CoinGeckoThread(this.page, handler);
            new Thread(coinGeckoThread).start();
        }
    }


    public boolean isLoading () {
        return isLoading;
    }
}