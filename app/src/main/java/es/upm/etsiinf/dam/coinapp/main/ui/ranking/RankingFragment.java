package es.upm.etsiinf.dam.coinapp.main.ui.ranking;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import es.upm.etsiinf.dam.coinapp.databinding.FragmentRankingBinding;
import es.upm.etsiinf.dam.coinapp.main.MainActivity;
import es.upm.etsiinf.dam.coinapp.main.ui.ranking.singleextended.DetailActivity;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;


public class RankingFragment extends Fragment {

    private FragmentRankingBinding binding;
    private RankingAdapter rankingAdapter;
    private Context context;


    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        context = requireActivity();
        RankingViewModel rankingViewModel =
                new ViewModelProvider(this, new RankingViewModelFactory(context)).get(RankingViewModel.class);

        binding = FragmentRankingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ProgressBar progressBar = binding.progressBarCoins;
        ListView listviewCoins = binding.listviewCoins;
        rankingAdapter = new RankingAdapter();
        listviewCoins.setAdapter(rankingAdapter);

        //Añadir el caracter "Infinite Scrolling"
        listviewCoins.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged (AbsListView absListView, int i) {

            }

            @Override
            public void onScroll (AbsListView absListView, int i, int i1, int i2) {
                if(i+i1 == i2 && i2 !=0){
                    if(!rankingViewModel.isLoading()){
                        rankingViewModel.loadMoreCoins();
                    }else{
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        listviewCoins.setOnItemClickListener((adapterView, view, i, l) -> {
            Coin selectedCoin = (Coin) ((RankingAdapter)(listviewCoins.getAdapter())).getItem(i);
            Intent intent = new Intent(requireActivity(), DetailActivity.class);
            intent.putExtra("coin_id",selectedCoin.getId());
            startActivity(intent);


        });
        rankingViewModel.getCoins().observe(getViewLifecycleOwner(), coins -> {
            rankingAdapter.clearCoins();
            progressBar.setVisibility(View.GONE);
            rankingAdapter.setCoins(coins);
        });

        SwipeRefreshLayout swipeRefreshLayout = binding.swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(() -> {
            rankingViewModel.refreshCoins();
        });

        rankingViewModel.getIsRefreshing().observe(getViewLifecycleOwner(), isRefreshing -> {
            swipeRefreshLayout.setRefreshing(isRefreshing);
        });

        rankingViewModel.getInternet().observe(getViewLifecycleOwner(), isInternet -> {
            if(isInternet.equalsIgnoreCase("NO")){
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, "No hay conexión a internet para actualizar los datos", Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        binding = null;
    }
}