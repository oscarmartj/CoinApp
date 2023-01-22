package es.upm.etsiinf.dam.coinapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.graphics.PointF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;


import androidx.core.content.ContextCompat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import es.upm.etsiinf.dam.coinapp.R;

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

    public String getLighterShade(String colorHex) {
        int color = Color.parseColor(colorHex);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = (int) Math.min(red * 1.35, 255);
        green = (int) Math.min(green * 1.35, 255);
        blue = (int) Math.min(blue * 1.35, 255);
        int lighterColor = Color.rgb(red, green, blue);
        return String.format("#%06X", (0xFFFFFF & lighterColor));
    }



    public File profileImageWithFilter(Context context, Bitmap bitmap, String username){

        Bitmap mutableImage = bitmap.copy(Bitmap.Config.ARGB_8888,true);

        Canvas canvas = new Canvas(mutableImage);

        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        cm.setScale(0.5f, 0.5f, 0.5f, 1);

        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);

        Paint paint = new Paint();
        paint.setColorFilter(f);
        canvas.drawBitmap(mutableImage, 0, 0, paint);

        Drawable iconD = ContextCompat.getDrawable(context,R.drawable.ic_camera_white_24dp);
        Bitmap icon = getBitmapFromDrawable(iconD);
        canvas.drawBitmap(icon, (mutableImage.getWidth() - icon.getWidth()) / 2, (mutableImage.getHeight() - icon.getHeight()) / 2, null);

        File imageDir = new File(context.getFilesDir(), "images");
        if(!imageDir.exists()){
            imageDir.mkdirs();
        }

        File imagePath = new File(imageDir, username+".jpg");
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

    public Bitmap compressImageFromGallery(Bitmap bitmap){
        bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] byteArray = byteArrayOutputStream.toByteArray();
        if(byteArray.length > 500000){
            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
        }
        /*
        while (byteArray.length > 500000) { //500KB
            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
        }*/

        return getBitmapFromBLOB(byteArray);
    }

    public Bitmap zoomFace(Bitmap originalBitmap) {
        originalBitmap = compressImageFromGallery(originalBitmap);
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        int maxFaces = 5;//valor arbitrario

        FaceDetector detector = new FaceDetector(width, height, maxFaces);
        FaceDetector.Face[] faces = new Face[maxFaces];
        int facesDetected = detector.findFaces(originalBitmap, faces);

        PointF midPoint = new PointF();
        if (facesDetected > 0) {
            // Obtener la cara principal
            Face biggestFace = null;
            float biggestFaceSize = 0;
            for (int i = 0; i < facesDetected; i++) {
                Face face = faces[i];
                float eyeDistance = face.eyesDistance();
                float faceSize = (int) (eyeDistance * 4) * (int) (eyeDistance * 4);
                if (biggestFace == null || faceSize > biggestFaceSize) {
                    biggestFace = face;
                    biggestFaceSize = faceSize;
                }
            }
            float eyeDistance = biggestFace.eyesDistance();

            // Obtener la posición y tamaño de la cara
            biggestFace.getMidPoint(midPoint);
            int faceX = (int) (midPoint.x - (eyeDistance * 2));
            int faceY = (int) (midPoint.y - (eyeDistance * 2));
            int faceWidth = (int) (eyeDistance * 4);
            int faceHeight = (int) (eyeDistance * 4);

            // Hacer zoom sobre la cara
            Bitmap faceBitmap = Bitmap.createBitmap(originalBitmap, faceX, faceY, faceWidth, faceHeight);

            // Escalar la imagen
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(faceBitmap, 300, 300, false);
            return scaledBitmap;
        } else {
            // Escalar la imagen
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 300, 300, false);
            return scaledBitmap;
        }
    }

    public Bitmap resizeImage(byte[] imageData, int widthDp, int heightDp) {
        // Convertimos las dimensiones en pixels
        float density = Resources.getSystem().getDisplayMetrics().density;
        int widthPx = (int) (widthDp * density);
        int heightPx = (int) (heightDp * density);

        // Crea un Bitmap a partir de los datos de la imagen
        Bitmap originalBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

        // Redimensiona el Bitmap a las dimensiones deseadas
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, widthPx, heightPx, false);

        // Libera la memoria del bitmap original
        originalBitmap.recycle();

        return resizedBitmap;
    }

    public int[] dimensionsDpToPx(int widthDp, int heightDp){
        float density = Resources.getSystem().getDisplayMetrics().density;
        int widthPx = (int) (widthDp * density);
        int heightPx = (int) (heightDp * density);

        return new int[]{widthPx,heightPx};
    }






}




