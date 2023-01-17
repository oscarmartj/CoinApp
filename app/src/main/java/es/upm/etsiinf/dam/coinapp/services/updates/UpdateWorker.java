package es.upm.etsiinf.dam.coinapp.services.updates;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.util.List;

import es.upm.etsiinf.dam.coinapp.AsyncTask.CoinGeckoThread;
import es.upm.etsiinf.dam.coinapp.AsyncTask.UpdateServiceThread;
import es.upm.etsiinf.dam.coinapp.database.functions.CoinDB;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;

public class UpdateWorker extends Worker {

    private Context context;
    public UpdateWorker (@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }

    @NonNull
    @Override
    public Result doWork () {
        CoinDB db = new CoinDB(context);

        UpdateServiceThread ust = new UpdateServiceThread(coins -> {
            if(coins.size() > 0) {
                Log.e("ListenerUpdate", "OK en Updateworker "+ coins.get(0));
                try {
                    db.insertCoins(coins);
                    DataManager.setSuccesfullTime(context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("UpdateWorker", "Error en Updateworker");
            }
        });
        Thread thread = new Thread(ust);
        thread.start();
        return Result.success();
    }
}
