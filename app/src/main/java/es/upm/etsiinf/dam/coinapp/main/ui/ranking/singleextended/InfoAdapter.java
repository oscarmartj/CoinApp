package es.upm.etsiinf.dam.coinapp.main.ui.ranking.singleextended;

import android.icu.math.BigDecimal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;
import es.upm.etsiinf.dam.coinapp.utils.DateManager;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    public List<HashMap<String,Object>> coinList;
    private final int VIEW_TYPE_1 = 1;
    private final int VIEW_TYPE_2 = 2;

    public InfoAdapter(List<HashMap<String,Object>> coinList){
        this.coinList = coinList;
    }

    @Override
    public int getItemViewType (int position) {
        if(position<9) {
            return VIEW_TYPE_1;
        }else{
            return VIEW_TYPE_2;
        }
    }

    @NonNull
    @Override
    public InfoAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_lv_1, parent, false);
            return new ViewHolder(view, VIEW_TYPE_1);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_lv_2, parent, false);
            return new ViewHolder(view, VIEW_TYPE_2);
        }
    }

    @Override
    public void onBindViewHolder (@NonNull InfoAdapter.ViewHolder holder, int position) {
        HashMap element = coinList.get(position);

        Log.i("position",position+"");
        Log.i("position",coinList.size()+" size");
        if(position<9){
            if((position==0 || position==2 || position==4 || position==8) && element != null){
                HashMap<String, Double> toInsert = (HashMap<String, Double>) element;
                String statistic = toInsert.keySet().iterator().next();
                holder.name.setText(statistic);

                //VALUE ---> MODIFICACIONES Y DEMAS
                String numero = String.valueOf(toInsert.get(statistic));
                if(numero.charAt(0)=='$'){
                    numero=numero.substring(1);
                }
                String format="";
                if(numero.contains("E-")){
                    Log.i("formateoOK",numero);
                    format = DataManager.scientificToNormalNotation(numero);
                    Log.i("formateoOK","2: "+format);
                }else if(numero.contains("E") && !numero.contains("E-")){
                    format = DataManager.formateoExponentePositivo(Double.valueOf(numero));
                }
                if(!format.equals("") && format.charAt(0)!='0'){
                    String formattedNumber = NumberFormat.getNumberInstance(Locale.US).format(Double.valueOf(format));
                    holder.value.setText("$"+formattedNumber);
                }else if(!format.equals("") && format.charAt(0)=='0' && format.charAt(1)=='.'){
                    holder.value.setText(DataManager.formatToCurrency(format));
                }else{
                    holder.value.setText("$"+toInsert.get(statistic));
                }
            }else if(position==1 && element != null){ //low 24h/high24h
                HashMap<String, Double> toInsert = (HashMap<String, Double>) element;


                //LOW24H
                String numero = String.valueOf(toInsert.get("24h Low"));
                String finalLow24h="";
                if(numero.charAt(0)=='$'){
                    finalLow24h=numero.substring(1);
                }else{
                    finalLow24h=numero;
                }
                String format="";
                if(finalLow24h.contains("E-")){
                    finalLow24h = DataManager.scientificToNormalNotation(finalLow24h);
                }else if(finalLow24h.contains("E") && !finalLow24h.contains("E-")){
                    finalLow24h = DataManager.formateoExponentePositivo(Double.valueOf(finalLow24h));
                }
                if(!finalLow24h.equals("") && finalLow24h.charAt(0)!='0'){
                    finalLow24h = NumberFormat.getNumberInstance(Locale.US).format(Double.valueOf(finalLow24h));
                }else if(!finalLow24h.equals("") && finalLow24h.charAt(0)=='0' && finalLow24h.charAt(1)=='.'){
                    finalLow24h = DataManager.formatToCurrency(finalLow24h);
                    finalLow24h = finalLow24h.substring(1);
                }else{
                    finalLow24h = numero;
                }

                //HIGH 24H

                String numeroH = String.valueOf(toInsert.get("24h High"));
                String finalHigh24h="";
                if(numeroH.charAt(0)=='$'){
                    finalHigh24h=numeroH.substring(1);
                }else{
                    finalHigh24h=numeroH;
                }
                if(finalHigh24h.contains("E-")){
                    finalHigh24h = DataManager.scientificToNormalNotation(finalHigh24h);
                }else if(finalHigh24h.contains("E") && !finalHigh24h.contains("E-")){
                    finalHigh24h = DataManager.formateoExponentePositivo(Double.valueOf(finalHigh24h));
                }
                if(!finalHigh24h.equals("") && finalHigh24h.charAt(0)!='0'){
                    finalHigh24h = NumberFormat.getNumberInstance(Locale.US).format(Double.valueOf(finalHigh24h));
                }else if(!finalHigh24h.equals("") && finalHigh24h.charAt(0)=='0' && finalHigh24h.charAt(1)=='.'){
                    finalHigh24h = DataManager.formatToCurrency(finalHigh24h);
                    finalHigh24h = finalHigh24h.substring(1);
                }else{
                    finalHigh24h = numeroH;
                }

                holder.name.setText("24h Low / 24h High");
                holder.value.setText("$"+finalLow24h+" / "+"$"+finalHigh24h);


            }else if(position==3 && element != null){
                HashMap<String, String> toInsert = (HashMap<String, String>) element;
                holder.name.setText("Market Cap Rank");
                holder.value.setText(toInsert.get("Market Cap Rank"));

            }else if((position==5 || position==6 || position==7) && element != null){
                HashMap<String, Double> toInsert = (HashMap<String, Double>) element;
                String statistic = toInsert.keySet().iterator().next();
                holder.name.setText(statistic);

                String numero = String.valueOf(toInsert.get(statistic));
                String format=numero;
                if(format.contains("E") && !format.contains("E-")){
                    format = DataManager.formateoExponentePositivo(Double.valueOf(numero));
                }
                holder.value.setText(""+NumberFormat.getNumberInstance(Locale.US).format(Double.valueOf(format)));
            }else{
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(0,0));
            }
            
        }else{
            Log.i("position",position+"");
            if(element!=null){
                if(position==9){
                    HashMap<String, Object> toInsert = (HashMap<String, Object>) element;

                    String numeroH = String.valueOf(toInsert.get("All-Time High"));
                    String finalHigh24h="";
                    if(numeroH.charAt(0)=='$'){
                        finalHigh24h=numeroH.substring(1);
                    }else{
                        finalHigh24h=numeroH;
                    }
                    if(finalHigh24h.contains("E-")){
                        finalHigh24h = DataManager.scientificToNormalNotation(finalHigh24h);
                    }else if(finalHigh24h.contains("E") && !finalHigh24h.contains("E-")){
                        finalHigh24h = DataManager.formateoExponentePositivo(Double.valueOf(finalHigh24h));
                    }
                    if(!finalHigh24h.equals("") && finalHigh24h.charAt(0)!='0'){
                        finalHigh24h = NumberFormat.getNumberInstance(Locale.US).format(Double.valueOf(finalHigh24h));
                    }else if(!finalHigh24h.equals("") && finalHigh24h.charAt(0)=='0' && finalHigh24h.charAt(1)=='.'){
                        finalHigh24h = DataManager.formatToCurrency(finalHigh24h);
                        finalHigh24h = finalHigh24h.substring(1);
                    }else{
                        finalHigh24h = numeroH;
                    }



                    double percentage_ath = (double) toInsert.get("percentage ath");
                    String date_ath = (String) toInsert.get("Date ath");
                    holder.name2.setText("All-Time High");
                    holder.value2.setText("$"+finalHigh24h+" "+new BigDecimal(percentage_ath).setScale(2, BigDecimal.ROUND_HALF_UP)+"%");
                    try {
                        holder.date2.setText(DateManager.changeFormatDate(date_ath));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }else{
                    HashMap<String, Object> toInsert = (HashMap<String, Object>) element;
                    double atl = (double) toInsert.get("All-Time Low");
                    double percentage_atl = (double) toInsert.get("percentage atl");
                    String date_atl = (String) toInsert.get("Date atl");
                    holder.name2.setText("All-Time Low");
                    holder.value2.setText("$"+atl+" "+percentage_atl+"%");
                    try {
                        holder.date2.setText(DateManager.changeFormatDate(date_atl));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }else{
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(0,0));
            }
            
        }
    }

    @Override
    public int getItemCount () {
        return coinList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView value;

        private TextView name2;
        private TextView value2;
        private TextView date2;

        public ViewHolder(View itemView, int layout) {
            super(itemView);
            if (layout == VIEW_TYPE_1){
                name = itemView.findViewById(R.id.name_statistic);
                value = itemView.findViewById(R.id.content_statistic);
            }else{
                name2 = itemView.findViewById(R.id.name_statistic_l2);
                value2 = itemView.findViewById(R.id.content_statistic_l2_1);
                date2 = itemView.findViewById(R.id.content_statistic_l2_2);
            }
        }
    }

}
