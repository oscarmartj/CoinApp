package es.upm.etsiinf.dam.coinapp.register.data;

import android.content.Context;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.database.UserDatabaseHelper;
import es.upm.etsiinf.dam.coinapp.modelos.User;
import es.upm.etsiinf.dam.coinapp.register.data.model.LoggedInUser;
import es.upm.etsiinf.dam.coinapp.utils.ImageManager;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private Context context;

    public LoginDataSource(Context context){
        this.context = context;
    }

    public Result<LoggedInUser> login (String username, String email, String password) {

        User user = new User(username,password,email);
        UserDatabaseHelper userDBHelper = new UserDatabaseHelper(context);

        try {
            byte[] profileImage = new ImageManager().getBLOBFromResources(context, R.drawable.defaultprofile);
            boolean resultado = userDBHelper.insertUser(user.getUsername(),user.getPassword(),user.getEmail(),profileImage);
            if(resultado){
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                user.getUsername());
                return new Result.Success<>(fakeUser);
            }else{
                return new Result.Error(new IOException("Error sign up"));
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error sign up", e));
        }
    }

    public void logout () {
        // TODO: revoke authentication
    }
}