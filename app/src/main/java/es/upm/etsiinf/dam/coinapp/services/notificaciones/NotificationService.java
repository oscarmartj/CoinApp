package es.upm.etsiinf.dam.coinapp.services.notificaciones;

import static es.upm.etsiinf.dam.coinapp.utils.NotificationUtils.CHANNEL_ID;
import static es.upm.etsiinf.dam.coinapp.utils.NotificationUtils.CHANNEL_ID_GROUP;
import static es.upm.etsiinf.dam.coinapp.utils.NotificationUtils.GROUP_NAME_COIN_NOTIFICATIONS;
import static es.upm.etsiinf.dam.coinapp.utils.NotificationUtils.NOTIFICATION_COINS_GROUP_ID;
import static es.upm.etsiinf.dam.coinapp.utils.NotificationUtils.NOTIFICATION_ID;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import es.upm.etsiinf.dam.coinapp.AsyncTask.NotificationCoinsThread;
import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.main.MainActivity;
import es.upm.etsiinf.dam.coinapp.main.ui.ranking.singleextended.DetailActivity;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;
import es.upm.etsiinf.dam.coinapp.utils.ImageManager;
import es.upm.etsiinf.dam.coinapp.utils.NotificationUtils;

public class NotificationService extends JobService {
    @Override
    public boolean onStartJob (JobParameters jobParameters) {
        Context context = getApplicationContext();

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            Log.i("scheduleJob","entra aqui donde las notis");
            SharedPreferences sharedPreferences = getSharedPreferences("favorites", MODE_PRIVATE);
            List<String> coins = DataManager.getFavorites(sharedPreferences);
            Log.i("scheduleJob",coins.toString());

            NotificationCoinsThread thread = new NotificationCoinsThread(coins, new NotificationCoinsThread.OnCoinsReceivedListener() {
                @Override
                public void onCoinsReceived (List<Coin> coins) throws IOException{
                    coins.sort(Comparator.comparingInt(Coin::getMarket_cap_rank));

                    StringBuilder ids= new StringBuilder();
                    Log.i("scheduleJob","final coins"+coins.toString());
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    for(Coin coin: coins){
                        NotificationUtils nu = new NotificationUtils();
                        DataManager dm = new DataManager();
                        Bitmap bitmap = new ImageManager().getBitmapFromURL(coin.getImage());
                        String currentPrice = "$"+String.format(Locale.US,"%,."+( DataManager.obtenerPrecisionFormato(coin.getCurrent_price()) )+"f",coin.getCurrent_price());

                        //SHARE
                        String bodyNotification = dm.setPendingShareText(coin);
                        PendingIntent sharePendingIntent = nu.createPendingIntent(NotificationService.this, bodyNotification, coin);

                        //To MainActivity
                        Intent intent = new Intent(NotificationService.this, DetailActivity.class);
                        intent.putExtra("coin_id",coin.getId());

                        PendingIntent pendingIntent;
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                            pendingIntent = PendingIntent.getActivity(
                                    context,
                                    Integer.parseInt(Integer.toString(coin.getMarket_cap_rank())
                                            + Integer.toString(0)),
                                    intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE );
                        }else{
                            pendingIntent = PendingIntent.getActivity(
                                    context,
                                    Integer.parseInt(Integer.toString(coin.getMarket_cap_rank())
                                            + Integer.toString(0)),
                                    intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT );
                        }

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationService.this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_staron)
                                .setLargeIcon(bitmap)
                                .setContentTitle(coin.getName())
                                .setContentText("El precio actual de "+coin.getSymbol().toUpperCase()+ " es de "+currentPrice)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(bodyNotification))
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setGroup(GROUP_NAME_COIN_NOTIFICATIONS)
                                .setContentIntent(pendingIntent)
                                .addAction(R.drawable.ic_share_black_24dp,"Share",sharePendingIntent);

                        int notificationID_resultado = Integer.parseInt(Integer.toString(NOTIFICATION_ID) +Integer.toString(coin.getMarket_cap_rank()));
                        ids.append(coin.getMarket_cap_rank());
                        notificationManager.notify(notificationID_resultado, builder.build());
                    }

                    NotificationCompat.Builder summaryBuilder = new NotificationCompat.Builder(NotificationService.this, CHANNEL_ID_GROUP)
                            .setSmallIcon(R.drawable.ic_staroff)
                            .setTicker("Notificaciones de monedas")
                            .setGroup(GROUP_NAME_COIN_NOTIFICATIONS)
                            .setGroupSummary(true)
                            .setPriority(NotificationCompat.PRIORITY_HIGH);

                    notificationManager.notify(Integer.parseInt(NOTIFICATION_COINS_GROUP_ID+ ids.toString()),summaryBuilder.build());
                }
            });

            new Thread(thread).start();
        }
        return false;
    }

    @Override
    public boolean onStopJob (JobParameters jobParameters) {
        Log.i("scheduleJob","entra en onStopJob");
        return true;
    }

}
