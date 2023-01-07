package es.upm.etsiinf.dam.coinapp.services.notificaciones;

import static es.upm.etsiinf.dam.coinapp.utils.NotificationUtils.CHANNEL_ID;
import static es.upm.etsiinf.dam.coinapp.utils.NotificationUtils.NOTIFICATION_ID;

import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import es.upm.etsiinf.dam.coinapp.R;

public class NotificationService extends JobService {
    @Override
    public boolean onStartJob (JobParameters jobParameters) {
        Log.i("scheduleJob","entra aqui donde las notis");
        SharedPreferences sharedPreferences = getSharedPreferences("favorites", MODE_PRIVATE);

        // Aquí debes poner el código que envía la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_staron)
                .setContentTitle("Notificación periódica")
                .setContentText("Este es un ejemplo de notificación periódica")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Obtiene una instancia de NotificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Envía la notificación
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        // Indica al sistema que el trabajo ya se ha realizado y puede ser eliminado

        return false;
    }

    @Override
    public boolean onStopJob (JobParameters jobParameters) {
        Log.i("scheduleJob","entra en onStopJob");
        return true;
    }
}
