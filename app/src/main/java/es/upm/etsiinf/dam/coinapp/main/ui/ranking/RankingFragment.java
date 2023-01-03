package es.upm.etsiinf.dam.coinapp.main.ui.ranking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.upm.etsiinf.dam.coinapp.databinding.FragmentRankingBinding;

public class RankingFragment extends Fragment {

    private FragmentRankingBinding binding;
    private RankingAdapter rankingAdapter;

    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        RankingViewModel rankingViewModel =
                new ViewModelProvider(this).get(RankingViewModel.class);

        binding = FragmentRankingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
                    }
                }
            }
        });
        rankingViewModel.getCoins().observe(getViewLifecycleOwner(), coins -> {
            rankingAdapter.clearCoins();
            rankingAdapter.setCoins(coins);
        });

        return root;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        binding = null;
    }
}