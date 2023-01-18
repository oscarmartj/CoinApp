package es.upm.etsiinf.dam.coinapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import es.upm.etsiinf.dam.coinapp.R;

//Bitmap bitmap1 = imageManager1.getBitmapFromURL();
public class ImageManager {

    public ImageManager () {
    }

    public byte[] getBLOBFromResources(Context context, int resource){
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), resource);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        return stream.toByteArray();

    }

    public Bitmap getBitmapFromURL (String imageURL) throws IOException {
        URL website = new URL(imageURL);
        URLConnection connection = website.openConnection();
        Bitmap res = BitmapFactory.decodeStream(connection.getInputStream());

        return res;

    }

    public byte[] getBytesFromBitmap (Bitmap bitmap) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }

    public Bitmap getBitmapFromBLOB (byte[] image) {
        Log.wtf("bitmapblob", "aqui");
        InputStream inputStream = new ByteArrayInputStream(image);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;

    }

    //https://stackoverflow.com/posts/29091591/revisions
    public Bitmap getBitmapFromDrawable (Drawable drawable){
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        final int width = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().height() : drawable.getIntrinsicHeight();

        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width, height <= 0 ? 1 : height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public Drawable getDrawableFromByte (byte[] bytes){
        Bitmap bitmap = getBitmapFromBLOB(bytes);
        return new BitmapDrawable(bitmap);
    }




    public String getDominantColor2 (Bitmap bitmap) {
        HashMap<Integer, Integer> colorMap = new HashMap<>();
        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int color = bitmap.getPixel(x, y);

                int count = 1;
                if(colorMap.containsKey(color)) {
                    count = colorMap.get(color) + 1;
                }
                colorMap.put(color, count);
            }
        }

        int dominantColor = 0;
        int maxCount = 0;
        for (Map.Entry<Integer, Integer> entry : colorMap.entrySet()) {
            if(entry.getValue() > maxCount) {
                dominantColor = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        //Verificar si el color dominante es negro o muy cercano al negro
        if(Color.red(dominantColor) < 20 && Color.green(dominantColor) < 20 && Color.blue(dominantColor) < 20) {
            int secondDominantColor = 0;
            int secondMaxCount = 0;
            for (Map.Entry<Integer, Integer> entry : colorMap.entrySet()) {
                if(entry.getValue() > secondMaxCount && entry.getKey() != dominantColor) {
                    secondDominantColor = entry.getKey();
                    secondMaxCount = entry.getValue();
                }
            }
            if(maxCount - secondMaxCount > (int) (maxCount*0.2)) {
                return String.format("#%06X", (0xFFFFFF & secondDominantColor));
            } else {
                return String.format("#%06X", (0xFFFFFF & dominantColor));
            }
        }
        return String.format("#%06X", (0xFFFFFF & dominantColor));
    }
}




