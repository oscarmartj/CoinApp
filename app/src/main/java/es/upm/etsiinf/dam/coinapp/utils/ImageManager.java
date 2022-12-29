package es.upm.etsiinf.dam.coinapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

//Bitmap bitmap1 = imageManager1.getBitmapFromURL();
public class ImageManager {

    public ImageManager(){}


    public Bitmap getBitmapFromURL(String imageURL) throws IOException {
        URL website = new URL(imageURL);
        URLConnection connection = website.openConnection();
        Bitmap res = BitmapFactory.decodeStream(connection.getInputStream());

        return res;

    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, stream);

        return stream.toByteArray();
    }

    public Bitmap getBitmapFromBLOB(byte[] image){
        Log.wtf("bitmapblob","aqui");
        InputStream inputStream = new ByteArrayInputStream(image);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;

    }

}
