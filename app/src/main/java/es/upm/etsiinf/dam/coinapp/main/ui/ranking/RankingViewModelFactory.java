package es.upm.etsiinf.dam.coinapp.main.ui.ranking;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.widget.ProgressBar;

public class RankingViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private ProgressBar progressBar;

    public RankingViewModelFactory (Context context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RankingViewModel.class)) {
            return (T) new RankingViewModel(context, progressBar);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
