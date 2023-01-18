package es.upm.etsiinf.dam.coinapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import es.upm.etsiinf.dam.coinapp.data.model.LoggedInUser;
import es.upm.etsiinf.dam.coinapp.database.functions.UserDB;
import es.upm.etsiinf.dam.coinapp.modelos.User;
import es.upm.etsiinf.dam.coinapp.database.UserDatabaseHelper;
import es.upm.etsiinf.dam.coinapp.utils.Security;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private Context context;

    public LoginDataSource(Context context){
        this.context = context;
    }
    public Result<LoggedInUser> login (String username, String password) {
        try {
            //Comprobamos si los datos son correctos
            User user = new UserDB(context).getUserByEmail(username);
            if(user!=null){
                if(new Security().checkPassword(password,user.getPassword())){
                    //Guardamos username y email por si fuera necesario utilizarlos en algun punto de la aplicación
                    SharedPreferences sharedPreferences = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
                    sharedPreferences.edit()
                            .putString("username",user.getUsername())
                            .putString("email", user.getEmail())
                            .apply();

                    return new Result.Success<>(new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username
                    ));
                }else{
                    return new Result.Error(new IOException("Contraseña incorrecta"));
                }
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("El usuario es incorrecto", e));
        }
        return new Result.Error(new IOException("El usuario es incorrecto"));
    }

    public void logout () {
        // TODO: revoke authentication
    }
}