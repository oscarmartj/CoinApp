package es.upm.etsiinf.dam.coinapp.services.updates;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import es.upm.etsiinf.dam.coinapp.AsyncTask.CoinGeckoThread;

public class UpdateWorker extends Worker {

    private Context context;
    public UpdateWorker (@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }

    @NonNull
    @Override
    public Result doWork () {

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage (@NonNull Message message) {
                if(message.what==0){

                }else{

                }
                return true;
            }
        });
        CoinGeckoThread coinGeckoThread = new CoinGeckoThread(1,handler);
        Thread thread = new Thread(coinGeckoThread);
        thread.start();

        return Result.success();
    }
}
