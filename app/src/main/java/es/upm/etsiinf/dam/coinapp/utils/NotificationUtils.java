package es.upm.etsiinf.dam.coinapp.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;

public class NotificationUtils {
    public static final String CHANNEL_ID="coin_notifications";
    public static final String CHANNEL_ID_GROUP="coin_notifications_group";
    public static final CharSequence CHANNEL_NAME="Coin Price Ticker";
    public static final int NOTIFICATION_ID=1;
    public static final int NOTIFICATION_COINS_GROUP_ID=2;
    public static final String GROUP_NAME_COIN_NOTIFICATIONS="coin_group";

    public void createNotificationChannel(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel notificationChannel = null;
        NotificationChannel notificationChannel1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400,
                    500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);

            //GROUP SUMMARY
            notificationChannel1 = new NotificationChannel(CHANNEL_ID_GROUP, CHANNEL_NAME,
                    importance);
            notificationChannel1.enableLights(true);
            notificationChannel1.setLightColor(Color.RED);
            notificationChannel1.enableVibration(true);
            notificationChannel1.setVibrationPattern(new long[]{100, 200, 300, 400,
                    500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel1);
        }
    }

    public File shareImage(Coin coin, Context context){
        Bitmap originalImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.share_image);

        Bitmap mutableImage = originalImage.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(mutableImage);

        Paint strokePaint = new Paint();
        strokePaint.setColor(Color.BLACK);
        strokePaint.setTextSize(250);
        strokePaint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        strokePaint.setStrokeWidth(4);
        strokePaint.setStrokeCap(Paint.Cap.ROUND);
        strokePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        strokePaint.setAlpha(255);

        Paint fillPaint = new Paint();
        fillPaint.setColor(Color.WHITE);
        fillPaint.setTextSize(250);
        fillPaint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        fillPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        fillPaint.setAlpha(255);

        String currentPrice = "$"+String.format(Locale.US,"%,."+( DataManager.obtenerPrecisionFormato(coin.getCurrent_price()) )+"f",coin.getCurrent_price());
        float currentPriceWidth = fillPaint.measureText(currentPrice);
        canvas.drawText(coin.getName(), 260, 1100, strokePaint);
        canvas.drawText(coin.getName(), 260, 1100, fillPaint);
        canvas.drawText(currentPrice, 260, 1500, strokePaint);
        canvas.drawText(currentPrice, 260, 1500, fillPaint);

        File imageDir = new File(context.getFilesDir(), "images");
        if(!imageDir.exists()){
            imageDir.mkdirs();
        }

        File imagePath = new File(imageDir, coin.getName()+".jpg");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imagePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mutableImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            if(out != null) {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    public PendingIntent createPendingIntent (Context notificationService, String bodyNotification, Coin coin){
        File image = shareImage(coin,notificationService);
        Uri fileUri = FileProvider.getUriForFile(
                notificationService,"es.upm.etsiinf.dam.coinapp.fileprovider",image);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        sendIntent.putExtra(Intent.EXTRA_TEXT,bodyNotification);
        sendIntent.setType("image/*");
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        //Con esto se soluciona el IllegalArgumentException
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            Log.i("scheduleJob","entra en S");
            Log.i("scheduleJob","creatingPendingIntent...\n"+bodyNotification);
            return PendingIntent.getActivity(notificationService,coin.getMarket_cap_rank(), sendIntent, PendingIntent.FLAG_MUTABLE);
        }
        return PendingIntent.getActivity(notificationService,coin.getMarket_cap_rank(), sendIntent, 0);
    }

}
