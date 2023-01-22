package es.upm.etsiinf.dam.coinapp.main.ui.ranking;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;
import es.upm.etsiinf.dam.coinapp.utils.ImageManager;

public class RankingAdapter extends BaseAdapter {

    List<Coin> coins = new LinkedList<>();
    ImageManager im = new ImageManager();
    @Override
    public int getCount () {
        return coins.size();
    }

    @Override
    public Object getItem (int i) {
        return coins.get(i);
    }

    @Override
    public long getItemId (int i) {
        return coins.get(i).getMarket_cap_rank();
    }

    @Override
    public View getView (int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) viewGroup
                .getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Coin coin = coins.get(i);

        if(view == null){
            view = inflater.inflate(R.layout.item_ranking, viewGroup, false);
        }

        TextView textViewMarketCapRank = view.findViewById(R.id.textViewMarketcapRank_coin);
        ShapeableImageView imageView = view.findViewById(R.id.imageView_coin);
        TextView textViewName = view.findViewById(R.id.textViewName_coin);
        TextView textViewPrice = view.findViewById(R.id.textViewPrice_coin);
        TextView textViewPercentage = view.findViewById(R.id.textViewPercentage_coin);
        ImageView imageViewArrow = view.findViewById(R.id.imageViewArrow_coin);

        textViewMarketCapRank.setText(coin.getMarket_cap_rank()+"");

        //margenes, modificalo segun el numero sea mayor o menor
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) textViewMarketCapRank.getLayoutParams();
        if(coin.getMarket_cap_rank()>=10){
            layoutParams.setMarginEnd(0);
            textViewMarketCapRank.setLayoutParams(layoutParams);
        }else{
            layoutParams.setMarginEnd(15);
            textViewMarketCapRank.setLayoutParams(layoutParams);
        }

        ShapeDrawable square = new ShapeDrawable(new RectShape());
        square.getPaint().setColor(Color.WHITE);
        int[] dimensionsPx = im.dimensionsDpToPx(48,48);
        square.setBounds(0,0,dimensionsPx[0],dimensionsPx[1]);

        imageView.setBackground(square);

        if(isConnected(view.getContext())){
            imageView.setImageBitmap(im.getBitmapFromBLOB(coin.getImageBytes()));
        }else{
            imageView.setImageBitmap(new ImageManager().getBitmapFromBLOB(coin.getImageBytes()));
        }
        textViewName.setText(coin.getSymbol().toUpperCase());

        int firstPositionWithout0 = DataManager.obtenerPrimeraPosicionDecimal(coin.getCurrent_price());
        if(firstPositionWithout0>2){
            Log.i("firstPosition",coin.getId()+" entra aqui");
            textViewPrice.setText("$"+String.format(Locale.US,"%,."+(firstPositionWithout0+1)+"f",coin.getCurrent_price()));
        }else if(firstPositionWithout0==2 && String.valueOf(coin.getCurrent_price()).length()>2){
            textViewPrice.setText("$"+String.format(Locale.US,"%,."+(firstPositionWithout0+1)+"f",coin.getCurrent_price()));
        }else{
            textViewPrice.setText("$"+String.format(Locale.US,"%,.2f",coin.getCurrent_price()));
        }


        double percentage = coin.getPrice_change_percentage_24h();

        if(!Double.isNaN(percentage)){
            String percentageString = DataManager.roundNumber(percentage);
            double percentageRound = DataManager.roundNumberWithSign(percentage);
            if(percentageRound>0.0){
                textViewPercentage.setText(String.format("%s%%", percentageString));
                imageViewArrow.setImageResource(R.drawable.ic_arrowup_green_24dp);
            }else if(percentageRound<0.0){
                textViewPercentage.setText(String.format("%s%%", percentageString));
                imageViewArrow.setImageResource(R.drawable.ic_arrowdown_red_24dp);
            }else{
                textViewPercentage.setText(String.format("%s%%", "0.0"));
                imageViewArrow.setImageResource(R.drawable.ic_arrowright_grey_24dp);
            }
        }else{
            textViewPercentage.setText(String.format("%s%%", DataManager.roundNumber(0.0)));
            imageViewArrow.setImageResource(R.drawable.ic_arrowright_grey_24dp);
        }
        return view;
    }

    public void setCoins(List<Coin> coinsList){
        this.coins.addAll(coinsList);
        super.notifyDataSetChanged();
    }

    public void clearCoins(){
        this.coins.clear();
        super.notifyDataSetChanged();
    }

    private boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
