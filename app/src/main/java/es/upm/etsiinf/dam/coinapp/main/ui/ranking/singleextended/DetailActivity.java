package es.upm.etsiinf.dam.coinapp.main.ui.ranking.singleextended;

import android.graphics.Color;
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
        setTitleColor(toolBarLayout,Color.parseColor(color));

        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_share_black_24dp));


        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // Handle the settings action
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setTitleColor(CollapsingToolbarLayout collapsingToolbarLayout, int color) {
        double luminance = Color.luminance(color);

        // Si el color es claro, cambiamos el color de las letras a negro
        if (luminance > 0.5) {
            collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);
            collapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);
        } else {
            // Si el color es oscuro, dejamos el color de las letras en blanco
            collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
            collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        }
    }
}