package es.upm.etsiinf.dam.coinapp.main.ui.profile.edit;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.database.functions.UserDB;
import es.upm.etsiinf.dam.coinapp.databinding.ActivityEditBinding;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;
import es.upm.etsiinf.dam.coinapp.utils.ImageManager;

public class EditActivity extends AppCompatActivity {

    private ActivityEditBinding binding;
    private UserDB userDB;

    private String usuarioFinal;
    private String emailFinal;
    private Drawable imageFinal;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ImageManager imageManager = new ImageManager();
        userDB = new UserDB(this);

        TextInputEditText user = findViewById(R.id.tiet_user_edit);
        TextInputEditText email = findViewById(R.id.tiet_email_edit);
        TextInputLayout user_layout = findViewById(R.id.til_user_edit);
        TextInputLayout email_layout = findViewById(R.id.til_email_edit);

        ShapeableImageView ip_profile = findViewById(R.id.iv_circle_profile_edit);

        String userText = getIntent().getStringExtra("username");
        usuarioFinal = userText;
        String emailText = getIntent().getStringExtra("email");
        emailFinal = emailText;
        Drawable imageProfile = imageManager.getDrawableFromByte(getIntent().getByteArrayExtra("imageProfile"));
        imageFinal = imageProfile;

        user.setText(usuarioFinal);
        email.setText(emailFinal);
        ip_profile.setImageDrawable(imageFinal);


        user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged (Editable editable) {
                if(DataManager.isUserNameValid(user.getText().toString())){
                    usuarioFinal = user.getText().toString();
                }else{
                    if(user.getText().toString().length()<3){
                        user_layout.setError("Minimum 3 characters.");
                    }else{
                        user_layout.setError("Invalid username format (a-z, A-Z,0-9,._-).");
                    }
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged (Editable editable) {
                if(DataManager.isEmailValid(email.getText().toString())){
                    if(userDB.countUsersByEmail(email.getText().toString())>0){
                        email_layout.setError("Already exist user with this email");
                    }else{
                        emailFinal=email.getText().toString();
                    }
                }else{
                    email_layout.setError("Invalid email format.");
                }

            }
        });



        MaterialToolbar toolbar = findViewById(R.id.topAppBar_editprofile);

        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        toolbar.setOnMenuItemClickListener(menuItem -> {
            if(menuItem.getItemId() == R.id.editprofile_menu_option) {
                Toast.makeText(this, " Icon Clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }
}