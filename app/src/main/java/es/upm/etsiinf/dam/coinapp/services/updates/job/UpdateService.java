package es.upm.etsiinf.dam.coinapp.services.updates.job;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import java.io.IOException;

import es.upm.etsiinf.dam.coinapp.AsyncTask.UpdateServiceThread;
import es.upm.etsiinf.dam.coinapp.database.functions.CoinDB;

@SuppressLint("SpecifyJobSchedulerIdRange")
public class UpdateService extends JobService {
    @Override
    public boolean onStartJob (JobParameters jobParameters) {
        /*
        CoinDB db = new CoinDB(getApplicationContext());
        UpdateServiceThread ust = new UpdateServiceThread(false, coins -> {
            if(coins.size() > 0) {
                Log.e("ListenerUpdate", "OK en Updateworker");
                try {
                    db.insertCoins(coins);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("UpdateWorker", "Error en Updateworker");
            }
        });
        Thread thread = new Thread(ust);
        thread.start();*/

        return false;
    }

    @Override
    public boolean onStopJob (JobParameters jobParameters) {
        return false;
    }
}
