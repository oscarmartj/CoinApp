package es.upm.etsiinf.dam.coinapp.main.ui.ranking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.upm.etsiinf.dam.coinapp.databinding.FragmentRankingBinding;

public class RankingFragment extends Fragment {

    private FragmentRankingBinding binding;

    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        RankingViewModel rankingViewModel =
                new ViewModelProvider(this).get(RankingViewModel.class);

        binding = FragmentRankingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView listviewCoins = binding.listviewCoins;
        //rankingViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        //ListAdapter adapter = new ListAdapter(getContext(),rankingViewModel.get)
        return root;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        binding = null;
    }
}