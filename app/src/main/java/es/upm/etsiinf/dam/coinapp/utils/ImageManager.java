package es.upm.etsiinf.dam.coinapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//ImageManager imageManager1 = ImageManager.getInstance("http://www.example.com/image1.jpg");
//Bitmap bitmap1 = imageManager1.getBitmapFromURL();
public class ImageManager {
    private static ImageManager instance;
    private String imageURL;

    private ImageManager (String imageURL) {
        this.imageURL = imageURL;
    }

    public ImageManager(){}

    public static ImageManager getInstance(String imageURL) {
        if (!instance.imageURL.equals(imageURL)) {
            instance = new ImageManager(imageURL);
        }

        return instance;
    }

    public Bitmap getBitmapFromURL() throws IOException {
        URL url = new URL(imageURL);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();

        InputStream input = connection.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(input);

        return bitmap;

    }

    public byte[] getBytesFromBitmap() throws IOException {
        Bitmap bitmap = getBitmapFromURL();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, stream);

        return stream.toByteArray();
    }

    public Bitmap getBitmapFromBLOB(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
