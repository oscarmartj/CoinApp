package es.upm.etsiinf.dam.coinapp.main.ui.ranking.singleextended;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.database.functions.CoinDB;
import es.upm.etsiinf.dam.coinapp.databinding.ActivityDetailBinding;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;
import es.upm.etsiinf.dam.coinapp.utils.ImageManager;


public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private Coin coin;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String idCoin = getIntent().getStringExtra("coin_id");
        this.coin = new Coin();
        try {
            this.coin = new CoinDB(this).searchCoinById(idCoin);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String color = new ImageManager().getDominantColor2(coin.getImageBitmap());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(coin.getName());
        toolBarLayout.setBackgroundColor(Color.parseColor(color));
        toolBarLayout.setContentScrimColor(Color.parseColor(color));

        ImageButton ivShare = binding.shareButton;
        ivShare.setOnClickListener((view -> {
            File modifiedImageFile = modifyShareImage();
            Uri fileUri = FileProvider.getUriForFile(
                    this,"es.upm.etsiinf.dam.coinapp.fileprovider",modifiedImageFile);

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            sendIntent.setType("image/*");
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

        }));


        setTitleColor(toolBarLayout,ivShare.getDrawable(), Color.parseColor(color));



        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }



    public void setTitleColor(CollapsingToolbarLayout collapsingToolbarLayout, Drawable overflowIcon, int color) {

        // Si el color es claro, cambiamos el color de las letras a negro
        if (colorEsClaro(color)) {
            collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);
            collapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);
            overflowIcon.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        } else {
            // Si el color es oscuro, dejamos el color de las letras en blanco
            collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
            collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
            overflowIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public boolean colorEsClaro(int colorToolbar){
        return Color.luminance(colorToolbar) > 0.5;

    }

    public File modifyShareImage(){
        // Cargar la imagen en un Bitmap
        Bitmap originalImage = BitmapFactory.decodeResource(getResources(), R.drawable.share_image);

        // Crear un nuevo Canvas a partir del Bitmap
        Bitmap mutableImage = originalImage.copy(Bitmap.Config.ARGB_8888,true);

        Canvas canvas = new Canvas(mutableImage);

        // Crear un objeto Paint para el trazo
        Paint strokePaint = new Paint();
        strokePaint.setColor(Color.BLACK);
        strokePaint.setTextSize(250);
        strokePaint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        strokePaint.setStrokeWidth(4);
        strokePaint.setStrokeCap(Paint.Cap.ROUND);
        strokePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        strokePaint.setAlpha(255);

// Crear un objeto Paint para el relleno
        Paint fillPaint = new Paint();
        fillPaint.setColor(Color.WHITE);
        fillPaint.setTextSize(250);
        fillPaint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        fillPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        fillPaint.setAlpha(255);

// Dibujar el texto con el trazo negro y el relleno blanco
        canvas.drawText(this.coin.getName(), 260, 1100, strokePaint);
        canvas.drawText(this.coin.getName(), 260, 1100, fillPaint);
        canvas.drawText("$"+String.format(Locale.US,"%,.2f",coin.getCurrent_price()), 260, 1500, strokePaint);
        canvas.drawText("$"+String.format(Locale.US,"%,.2f",coin.getCurrent_price()), 260, 1500, fillPaint);


        // Crear un subdirectorio "modified_images" en el directorio privado de archivos de la aplicación
        File imageDir = new File(getFilesDir(), "images");
        if(!imageDir.exists()){
            imageDir.mkdirs();
        }

        // Crear un archivo en el directorio "modified_images"
        File imagePath = new File(imageDir, "modified_share_image.jpg");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imagePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Comprimir el bitmap en JPEG y escribirlo en el archivo
        mutableImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            if(out != null) {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Devolver el path del archivo
        return imagePath;//.getAbsolutePath();
    }

}