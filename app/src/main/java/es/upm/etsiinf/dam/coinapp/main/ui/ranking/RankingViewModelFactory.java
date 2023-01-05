package es.upm.etsiinf.dam.coinapp.main.ui.ranking;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;

public class RankingViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public RankingViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RankingViewModel.class)) {
            return (T) new RankingViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
