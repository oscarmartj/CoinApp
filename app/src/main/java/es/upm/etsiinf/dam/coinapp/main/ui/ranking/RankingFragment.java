package es.upm.etsiinf.dam.coinapp.main.ui.ranking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import es.upm.etsiinf.dam.coinapp.databinding.FragmentRankingBinding;



public class RankingFragment extends Fragment {

    private FragmentRankingBinding binding;
    private RankingAdapter rankingAdapter;
    private Context context;

    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        RankingViewModel rankingViewModel =
                new ViewModelProvider(this).get(RankingViewModel.class);

        binding = FragmentRankingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = requireActivity();
        rankingViewModel.setConnected(isConnected());

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
                        rankingViewModel.loadMoreCoins(context,isConnected());
                    }else{
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
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

        return root;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        binding = null;
    }

    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}