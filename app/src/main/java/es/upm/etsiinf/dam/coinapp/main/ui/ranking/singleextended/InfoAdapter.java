package es.upm.etsiinf.dam.coinapp.main.ui.ranking.singleextended;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import es.upm.etsiinf.dam.coinapp.R;

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
        if(position<9){
            if((position==0 || position==2 || position==4 || position==8) && element != null){
                HashMap<String, Double> toInsert = (HashMap<String, Double>) element;
                String statistic = toInsert.keySet().iterator().next();
                holder.name.setText(statistic);
                holder.value.setText("$"+toInsert.get(statistic));
            }else if(position==1 && element != null){ //low 24h/high24h
                HashMap<String, Double> toInsert = (HashMap<String, Double>) element;


                double low24h = toInsert.get("24h Low");
                double high24h = toInsert.get("24h High");

                holder.name.setText("24h Low / 24h High");
                holder.value.setText("$"+low24h+" / "+"$"+high24h);


            }else if(position==3 && element != null){
                HashMap<String, String> toInsert = (HashMap<String, String>) element;
                holder.name.setText("Market Cap Rank");
                holder.value.setText(toInsert.get("Market Cap Rank"));

            }else if((position==5 || position==6 || position==7) && element != null){
                HashMap<String, Double> toInsert = (HashMap<String, Double>) element;
                String statistic = toInsert.keySet().iterator().next();
                holder.name.setText(statistic);
                holder.value.setText(""+toInsert.get(statistic));
            }else{
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(0,0));
            }
            
        }else{
            if(element!=null){
                if(position==9){
                    HashMap<String, Object> toInsert = (HashMap<String, Object>) element;
                    double ath = (double) toInsert.get("All-Time High");
                    double percentage_ath = (double) toInsert.get("percentage ath");
                    String date_ath = (String) toInsert.get("Date ath");
                    holder.name2.setText("All-Time High");
                    holder.value2.setText("$"+ath+" "+percentage_ath+"%");
                    holder.date2.setText(date_ath);

                }else{
                    HashMap<String, Object> toInsert = (HashMap<String, Object>) element;
                    double atl = (double) toInsert.get("All-Time Low");
                    double percentage_atl = (double) toInsert.get("percentage atl");
                    String date_atl = (String) toInsert.get("Date atl");
                    holder.name2.setText("All-Time Low");
                    holder.value2.setText("$"+atl+" "+percentage_atl+"%");
                    holder.date2.setText(date_atl);
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
