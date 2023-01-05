package es.upm.etsiinf.dam.coinapp.main.ui.magic;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.upm.etsiinf.dam.coinapp.databinding.FragmentMagicBinding;

public class MagicFragment extends Fragment {

    private FragmentMagicBinding binding;

    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        Context context = requireActivity();
        MagicViewModel magicViewModel =
                new ViewModelProvider(this, new MagicViewModelFactory(context)).get(MagicViewModel.class);

        binding = FragmentMagicBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        magicViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        binding = null;
    }
}