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
        boolean isValid = true;
        int errorUsername = 0;
        int errorEmail = 0;
        int errorPassword = 0;

        if(!username.isEmpty() && !isUserNameValid(username)) {
            Log.i("Flujo","if user");
            isValid = false;
            errorUsername = R.string.invalid_username_2;
        }
        if(!email.isEmpty() && !isEmailValid(email)){
            Log.i("Flujo","if email");
            isValid = false;
            errorEmail = R.string.invalid_email;
        }
        if(!password.isEmpty() && !isPasswordValid(password)) {
            Log.i("Flujo","if password");
            isValid = false;
            errorPassword = R.string.invalid_password;
        }
        if(isValid) {
            Log.i("Flujo","if valid");
            loginFormState.setValue(new LoginFormState(true));
        } else {
            Log.i("Flujo","if erorr");
            loginFormState.setValue(new LoginFormState(errorUsername==0?null:errorUsername,errorEmail==0?null:errorEmail,errorPassword==0?null:errorPassword));
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