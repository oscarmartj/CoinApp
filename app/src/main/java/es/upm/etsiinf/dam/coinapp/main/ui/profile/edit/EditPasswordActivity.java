package es.upm.etsiinf.dam.coinapp.main.ui.profile.edit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.NoSuchAlgorithmException;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.database.functions.UserDB;
import es.upm.etsiinf.dam.coinapp.databinding.ActivityEditBinding;
import es.upm.etsiinf.dam.coinapp.databinding.ActivityEditpasswordBinding;
import es.upm.etsiinf.dam.coinapp.modelos.User;
import es.upm.etsiinf.dam.coinapp.utils.Security;

public class EditPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpassword);

        UserDB userDB = new UserDB(this);
        Security security = new Security();

        TextInputEditText old_password = findViewById(R.id.tiet_old_password_edit);
        TextInputEditText new_password = findViewById(R.id.tiet_new_password_edit);
        TextInputEditText new_password_confirmation = findViewById(R.id.tiet_confirm_new_password_edit);

        TextInputLayout old_password_layout = findViewById(R.id.til_old_password_edit);
        TextInputLayout new_password_layout = findViewById(R.id.til_new_password_edit);
        TextInputLayout new_password_confirmation_layout = findViewById(R.id.til_confirm_new_password_edit);

        String email = getIntent().getStringExtra("email");
        User user = userDB.getUserByEmail(email);


        /// LISTENER TEXTFIELDS ///
        old_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged (Editable editable) {
                if(old_password_layout.getError()!=null){
                    String oldPassword = old_password.getText().toString();
                    if(old_password.getText().toString().length()<5){
                        old_password_layout.setError("Incorrect password.");
                    }else{
                        boolean samePassword = false;
                        try {
                            samePassword = security.checkPassword(oldPassword,user.getPassword());
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        if(!samePassword){
                            old_password_layout.setError("Incorrect password.");
                        }else{
                            old_password_layout.setError(null);
                        }
                    }
                }else{
                    if(new_password_confirmation.getText().toString().length()>0 || new_password.getText().toString().length()>0){
                        if(old_password.getText().toString().length()<5){
                            old_password_layout.setError("Incorrect password.");
                        }else{
                            boolean samePassword = false;
                            try {
                                samePassword = security.checkPassword(old_password.getText().toString(),user.getPassword());
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            if(!samePassword){
                                old_password_layout.setError("Incorrect password.");
                            }else{
                                old_password_layout.setError(null);
                            }
                        }
                    }
                }

            }
        });

        new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged (Editable editable) {
                //COMPROBAR PRIMERO OLDPASSWORD
                String oldPassword = old_password.getText().toString();
                if(old_password.getText().toString().length()<5){
                    old_password_layout.setError("Incorrect password.");
                }else{
                    boolean samePassword = false;
                    try {
                        samePassword = security.checkPassword(oldPassword,user.getPassword());
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    if(!samePassword){
                        old_password_layout.setError("Incorrect password.");
                    }else{
                        old_password_layout.setError(null);
                    }
                }

                if(new_password_confirmation_layout.getError() != null){
                    String newPasswordConfirmation = new_password_confirmation.getText().toString();
                    //Si la contraseña es inferior a 5 caracters
                    if(newPasswordConfirmation.length()<5){
                        new_password_confirmation_layout.setError("Minimum 5 characters.");

                        //Si la contraseña es mayor o igual a 5 caracters pero no coincide con la new password
                    }else if(new_password.getText().toString().length()>20){
                        new_password_confirmation_layout.setError("Max. 20 characters.");
                    }else if(new_password.getText() !=null
                            && new_password.getText().length()>=5
                            && !new_password_confirmation.getText().toString().equals(new_password.getText().toString())
                            && newPasswordConfirmation.length()>=5
                    ){
                        new_password_confirmation_layout.setError("It does not match with the new password.");
                    }else{
                        new_password_confirmation_layout.setError(null);
                    }
                }

                String newPassword = new_password.getText().toString();
                //Si la contraseña es inferior a 5 caracters
                if(newPassword.length()<5){
                    new_password_layout.setError("Min. 5 characters.");

                //Si la contraseña es mayor o igual a 5 caracters pero no coincide con la confirmation
                }else if(newPassword.length()>20){
                    new_password_layout.setError("Max. 20 characters.");
                }else if(new_password_confirmation.getText() !=null
                        && new_password_confirmation.getText().length()>=5
                        && !new_password_confirmation.getText().toString().equals(newPassword)
                        && newPassword.length()>=5
                ){
                    new_password_layout.setError("It does not match with the password confirmation.");
                }else{
                    new_password_layout.setError(null);
                }

            }
        });

        new_password_confirmation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged (Editable editable) {

                if(new_password_layout.getError() != null){
                    if(new_password.getText().toString().length()<5){
                        new_password_layout.setError("Min. 5 characters.");

                        //Si la contraseña es mayor o igual a 5 caracters pero no coincide con la confirmation
                    }else if(new_password.getText().toString().length()>20){
                        new_password_layout.setError("Max. 20 characters.");
                    }else if(new_password_confirmation.getText() !=null
                            && new_password_confirmation.getText().length()>=5
                            && !new_password_confirmation.getText().toString().equals(new_password.getText().toString())
                            && new_password.getText().toString().length()>=5
                    ){
                        new_password_layout.setError("It does not match with the password confirmation.");
                    }else{
                        new_password_layout.setError(null);
                    }
                }

                String newPasswordConfirmation = new_password_confirmation.getText().toString();
                //Si la contraseña es inferior a 5 caracters
                if(newPasswordConfirmation.length()<5){
                    new_password_confirmation_layout.setError("Minimum 5 characters.");

                    //Si la contraseña es mayor o igual a 5 caracters pero no coincide con la new password
                }else if(new_password.getText().toString().length()>20){
                    new_password_confirmation_layout.setError("Max. 20 characters.");
                }else if(new_password.getText() !=null
                        && new_password.getText().length()>=5
                        && !new_password_confirmation.getText().toString().equals(new_password.getText().toString())
                        && newPasswordConfirmation.length()>=5
                ){
                    new_password_confirmation_layout.setError("It does not match with the new password.");
                }else{
                    new_password_confirmation_layout.setError(null);
                }

            }
        });


        MaterialToolbar toolbar = findViewById(R.id.topAppBar_editpassword);

        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        toolbar.setOnMenuItemClickListener(menuItem -> {
            if(menuItem.getItemId() == R.id.edit_menu_option) {
                if(old_password_layout.getError() != null || new_password_layout.getError() !=null || new_password_confirmation_layout.getError() != null){
                    Toast.makeText(this, "Changes could not be saved.", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    try {
                        String finalNewPassword = security.encryptPassword(new_password_confirmation.getText().toString());
                        user.setPassword(finalNewPassword);
                        userDB.updatePassword(user);
                        Toast.makeText(this, "Password changed successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
            return false;
        });
    }
}
