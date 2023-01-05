package es.upm.etsiinf.dam.coinapp.main.ui.ranking.singleextended;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.json.JSONException;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.database.functions.CoinDB;
import es.upm.etsiinf.dam.coinapp.databinding.ActivityDetailBinding;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;
import es.upm.etsiinf.dam.coinapp.utils.ImageManager;


public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String idCoin = getIntent().getStringExtra("coin_id");
        Coin coin = new Coin();
        try {
            coin = new CoinDB(this).searchCoinById(idCoin);
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
        ivShare.setOnClickListener(this::onShareButtonClick);


        /*
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_share_black_24dp));
        Drawable overflowIcon = toolbar.getOverflowIcon();*/
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

    public void onShareButtonClick(View view) {
        String data = "Datos a compartir";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, data);

        startActivity(Intent.createChooser(shareIntent, "Compartir con"));
    }
}