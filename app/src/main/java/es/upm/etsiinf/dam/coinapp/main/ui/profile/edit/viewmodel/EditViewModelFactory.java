package es.upm.etsiinf.dam.coinapp.main.ui.profile.edit.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import es.upm.etsiinf.dam.coinapp.main.ui.profile.ProfileViewModel;

public class EditViewModelFactory implements ViewModelProvider.Factory{
    private final String usuario;
    private final String email;
    private final Drawable imageProfile;
    private final SharedPreferences sharedPreferences;
    private final Context context;

    public EditViewModelFactory(Context context, SharedPreferences sharedPreferences, String usuario, String email, Drawable imageProfile) {
        this.context = context;
        this.usuario = usuario;
        this.email = email;
        this.imageProfile = imageProfile;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EditViewModel.class)) {
            return (T) new EditViewModel(context, sharedPreferences, usuario, email, imageProfile);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
