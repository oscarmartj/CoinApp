package es.upm.etsiinf.dam.coinapp.main.ui.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.SharedPreferences;

import es.upm.etsiinf.dam.coinapp.main.ui.ranking.RankingViewModel;

public class ProfileViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private final SharedPreferences sharedPreferences;

    public ProfileViewModelFactory(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            return (T) new ProfileViewModel(context, sharedPreferences);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

