package es.upm.etsiinf.dam.coinapp.register.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.register.data.LoginRepository;
import es.upm.etsiinf.dam.coinapp.register.data.Result;
import es.upm.etsiinf.dam.coinapp.register.data.model.LoggedInUser;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel (LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState () {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult () {
        return loginResult;
    }

    public void login (String username, String email, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, email, password);

        if(result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.register_failed));
        }
    }

    public void loginDataChanged (String username, String email, String password) {
        if(!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username_2, null ,null));
        } else if(!isEmailValid(email)){
            loginFormState.setValue(new LoginFormState(null,R.string.invalid_email,null));
        } else if(!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isEmailValid (String email) {
        if(email == null) {
            return false;
        }
        if(email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return false;
        }
    }

    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        boolean matches = username.matches("^[a-zA-Z0-9._-]{3,}$");
        return matches;
    }


    // A placeholder password validation check
    private boolean isPasswordValid (String password) {
        return password != null && password.trim().length() > 5;
    }
}