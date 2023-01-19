package es.upm.etsiinf.dam.coinapp.main.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.security.NoSuchAlgorithmException;

import es.upm.etsiinf.dam.coinapp.database.functions.UserDB;
import es.upm.etsiinf.dam.coinapp.modelos.User;
import es.upm.etsiinf.dam.coinapp.utils.ImageManager;
import es.upm.etsiinf.dam.coinapp.utils.Security;

public class ProfileViewModel extends ViewModel {


    private final MutableLiveData<String> usuario;
    private final MutableLiveData<String> email;
    private final MutableLiveData<Drawable> imageProfile;
    private UserDB userDB;
    private User user;
    private ImageManager manager;

    public ProfileViewModel (Context context, SharedPreferences sharedPreferences) {
        manager = new ImageManager();
        userDB = new UserDB(context);
        user = userDB.getUserByEmail(sharedPreferences.getString("email",""));

        usuario = new MutableLiveData<>();
        usuario.setValue(user.getUsername());

        email = new MutableLiveData<>();
        email.setValue(user.getEmail());

        imageProfile = new MutableLiveData<>();
        imageProfile.setValue(manager.getDrawableFromByte(user.getProfileImage()));
    }

    public LiveData<String> getUsuario () {
        return usuario;
    }

    public LiveData<String> getEmail () {
        return email;
    }

    public LiveData<Drawable> getImageProfile(){
        return imageProfile;
    }

}