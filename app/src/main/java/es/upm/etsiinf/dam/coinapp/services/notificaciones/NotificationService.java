package es.upm.etsiinf.dam.coinapp.services.notificaciones;

import static es.upm.etsiinf.dam.coinapp.utils.NotificationUtils.CHANNEL_ID;
import static es.upm.etsiinf.dam.coinapp.utils.NotificationUtils.GROUP_NAME_COIN_NOTIFICATIONS;
import static es.upm.etsiinf.dam.coinapp.utils.NotificationUtils.NOTIFICATION_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.List;

import es.upm.etsiinf.dam.coinapp.AsyncTask.NotificationCoinsThread;
import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;

public class NotificationService extends JobService {
    @Override
    public boolean onStartJob (JobParameters jobParameters) {
        Log.i("scheduleJob","entra aqui donde las notis");
        SharedPreferences sharedPreferences = getSharedPreferences("favorites", MODE_PRIVATE);
        List<String> coins = DataManager.getFavorites(sharedPreferences);
        Log.i("scheduleJob",coins.toString());

        NotificationCoinsThread thread = new NotificationCoinsThread(coins, new NotificationCoinsThread.OnCoinsReceivedListener() {
            @Override
            public void onCoinsReceived (List<Coin> coins) {
                Log.i("scheduleJob","final coins"+coins.toString());
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                for(Coin coin: coins){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationService.this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_staron)
                            .setContentTitle("Notificación periódica")
                            .setContentText("Este es un ejemplo de notificación periódica")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setGroup(GROUP_NAME_COIN_NOTIFICATIONS);


                    notificationManager.notify(NOTIFICATION_ID, builder.build());
                }
                // Crea una notificación de resumen del grupo de notificaciones
                NotificationCompat.Builder summaryBuilder = new NotificationCompat.Builder(NotificationService.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_staroff)
                        .setContentTitle("Notificaciones de monedas")
                        .setContentText("Tienes " + coins.size() + " monedas en tu lista de favoritos")
                        .setGroup(GROUP_NAME_COIN_NOTIFICATIONS) // Establece el mismo valor de grupo para la notificación de resumen
                        .setGroupSummary(true) // Indica que esta es una notificación de resumen del grupo
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                notificationManager.notify(NOTIFICATION_ID, summaryBuilder.build());
            }
        });

        new Thread(thread).start();


        // Indica al sistema que el trabajo ya se ha realizado y puede ser eliminado

        return false;
    }

    @Override
    public boolean onStopJob (JobParameters jobParameters) {
        Log.i("scheduleJob","entra en onStopJob");
        return true;
    }
}
