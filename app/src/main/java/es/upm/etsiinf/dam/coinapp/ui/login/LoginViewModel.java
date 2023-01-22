package es.upm.etsiinf.dam.coinapp.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.regex.Pattern;

import es.upm.etsiinf.dam.coinapp.data.LoginRepository;
import es.upm.etsiinf.dam.coinapp.data.Result;
import es.upm.etsiinf.dam.coinapp.data.model.LoggedInUser;
import es.upm.etsiinf.dam.coinapp.R;

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

    public void login (String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if(result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged (String username, String password) {
        if(!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if(!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid (String username) {
        if(username == null) {
            return false;
        }
        if(username.contains("@")) {
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+(\\.[a-zA-Z]+)?$");
            return pattern.matcher(username).matches();
        } else {
            return false;
        }
    }


    // A placeholder password validation check
    private boolean isPasswordValid (String password) {
        return password != null && password.trim().length() > 5;
    }
}