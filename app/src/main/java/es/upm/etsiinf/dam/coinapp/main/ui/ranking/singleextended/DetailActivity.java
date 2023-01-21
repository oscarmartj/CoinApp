package es.upm.etsiinf.dam.coinapp.main.ui.ranking.singleextended;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.VectorDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.icu.math.BigDecimal;
import android.icu.text.Transliterator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.database.functions.CoinDB;
import es.upm.etsiinf.dam.coinapp.databinding.ActivityDetailBinding;
import es.upm.etsiinf.dam.coinapp.main.ui.ranking.RankingAdapter;
import es.upm.etsiinf.dam.coinapp.main.ui.ranking.RankingViewModel;
import es.upm.etsiinf.dam.coinapp.main.ui.ranking.RankingViewModelFactory;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;
import es.upm.etsiinf.dam.coinapp.utils.ImageManager;


public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private Coin coin;
    private ImageManager imageManager;
    private int colorTextToolbar;
    private InfoAdapter infoAdapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageManager = new ImageManager();

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

            sendIntent.putExtra(Intent.EXTRA_TEXT,DataManager.setIntentShareText(coin));
            sendIntent.setType("image/*");
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

        }));

        setTitleColor(toolBarLayout,ivShare.getDrawable(), Color.parseColor(color));



        FloatingActionButton fab = binding.fab;
        boolean isFavorite = DataManager.isFavorite(this,coin.getId());
        if(isFavorite){
            fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_staron));
            ColorStateList csl = ColorStateList.valueOf(Color.YELLOW);
            fab.setBackgroundTintList(csl);
        }else{
            fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_staroff));
            ColorStateList csl = ColorStateList.valueOf(Color.GRAY);
            fab.setBackgroundTintList(csl);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                boolean isFavorite = DataManager.isFavorite(DetailActivity.this,coin.getId());
                if(!isFavorite){
                    DataManager.setFavorite(DetailActivity.this,coin.getId());
                    fab.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this,R.drawable.ic_staron));

                    ColorStateList csl = ColorStateList.valueOf(Color.YELLOW);
                    fab.setBackgroundTintList(csl);

                    Toast.makeText(DetailActivity.this,"Añadida a favoritos, recibiras notificaciones de su precio.", Toast.LENGTH_LONG).show();
                }else{
                    DataManager.removeFavorite(DetailActivity.this,coin.getId());
                    fab.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this,R.drawable.ic_staroff));

                    ColorStateList csl = ColorStateList.valueOf(Color.GRAY);
                    fab.setBackgroundTintList(csl);

                    Toast.makeText(DetailActivity.this,"Eliminada de favoritos, dejaras de recibir notificaciones", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //ELEMENTOS DE LA CARDVIEW
        confShapeIcon(binding.includeContentScrolling.ivLogoContentDetail, binding.includeContentScrolling.clCardone);
        confHeaderText(binding.includeContentScrolling.nameCoinDetailInformation, binding.includeContentScrolling.marketcaprankCoinDetailInformation);


        RecyclerView lv_marketInformation = binding.includeContentScrolling.lwMarketInformation;
        infoAdapter = new InfoAdapter(coin.getListOfElementsForAdapter());
        lv_marketInformation.setAdapter(infoAdapter);
        lv_marketInformation.setLayoutManager(new LinearLayoutManager(this));

        Button btn_more_info = binding.includeContentScrolling.moreInfoBtn;

        btn_more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                String url = "https://www.coingecko.com/en/coins/"+coin.getId();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

    }


    public void setTitleColor(CollapsingToolbarLayout collapsingToolbarLayout, Drawable overflowIcon, int color) {

        if (colorEsClaro(color)) {
            collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);
            collapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);
            colorTextToolbar = Color.BLACK;
            overflowIcon.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        } else {
            collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
            collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
            colorTextToolbar = Color.WHITE;
            overflowIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public boolean colorEsClaro(int colorToolbar){
        return Color.luminance(colorToolbar) > 0.5;

    }

    public File modifyShareImage(){
        Bitmap originalImage = BitmapFactory.decodeResource(getResources(), R.drawable.share_image);

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

        String currentPrice = "$" + String.format(Locale.US, "%,." + (DataManager.obtenerPrecisionFormato(coin.getCurrent_price())) + "f", coin.getCurrent_price());
        float currentPriceWidth = fillPaint.measureText(currentPrice);
        canvas.drawText(this.coin.getName(), 260, 1100, strokePaint);
        canvas.drawText(this.coin.getName(), 260, 1100, fillPaint);
        canvas.drawText(currentPrice, 260, 1500, strokePaint);
        canvas.drawText(currentPrice, 260, 1500, fillPaint);

        File imageDir = new File(getFilesDir(), "images");
        if(!imageDir.exists()){
            imageDir.mkdirs();
        }

        File imagePath = new File(imageDir, "modified_share_image.jpg");
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

    private void confShapeIcon(ShapeableImageView shapeableImageView, ConstraintLayout layout){
        layout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(imageManager.getLighterShade(imageManager.getDominantColor2(imageManager.getBitmapFromBLOB(coin.getImageBytes()))))));
        //cuadrado blanco para fondo
        ShapeDrawable square = new ShapeDrawable(new RectShape());
        square.getPaint().setColor(Color.WHITE);
        int[] dimensionsPx = imageManager.dimensionsDpToPx(48,48);
        square.setBounds(0,0,dimensionsPx[0],dimensionsPx[1]);

        shapeableImageView.setBackground(square);
        shapeableImageView.setImageBitmap(imageManager.resizeImage(coin.getImageBytes(), 48,48));

    }

    private void confHeaderText (TextView nameCoinDetailInformation, TextView marketcaprankCoinDetailInformation) {
        if(coin.getName().length()<15){
            nameCoinDetailInformation.setText(coin.getName());
        }else{
            nameCoinDetailInformation.setText(coin.getSymbol().toUpperCase());
        }
        marketcaprankCoinDetailInformation.setText("#"+coin.getMarket_cap_rank());

        //color
        nameCoinDetailInformation.setTextColor(colorTextToolbar);
        marketcaprankCoinDetailInformation.setTextColor(colorTextToolbar);
        if(colorTextToolbar == Color.BLACK) {
            nameCoinDetailInformation.setTypeface(null, Typeface.NORMAL); //cuando el texto es negro, queda mal la negrita.
        }
    }
}