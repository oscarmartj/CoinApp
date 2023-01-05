package es.upm.etsiinf.dam.coinapp.main.ui.magic;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import es.upm.etsiinf.dam.coinapp.main.ui.ranking.RankingViewModel;

public class MagicViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public MagicViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RankingViewModel.class)) {
            return (T) new MagicViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}