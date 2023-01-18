package es.upm.etsiinf.dam.coinapp.main.ui.profile.edit.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditViewModel extends ViewModel {

    private final MutableLiveData<String> usuario;
    private final MutableLiveData<String> email;
    private final MutableLiveData<Drawable> imageProfile;

    public EditViewModel (Context context, SharedPreferences sharedPreferences,
                          String username, String emailstring, Drawable imageProfiled) {
        usuario = new MutableLiveData<>();
        usuario.setValue(username);

        email = new MutableLiveData<>();
        email.setValue(emailstring);

        imageProfile = new MutableLiveData<>();
        imageProfile.setValue(imageProfiled);
    }

    public LiveData<String> getUsuario () {
        return usuario;
    }

    public LiveData<String> getEmail () {
        return email;
    }

    public LiveData<Drawable> getImageProfile () {
        return imageProfile;
    }

    public void changeUsername(String username){
        usuario.setValue(username);
    }

    public void changeEmail(String emailstring){
        email.setValue(emailstring);
    }

    public void changeDrawable(Drawable newDrawable){
        imageProfile.setValue(newDrawable);
    }
}
