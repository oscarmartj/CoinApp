package es.upm.etsiinf.dam.coinapp.main.ui.profile.edit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.databinding.ActivityDetailBinding;
import es.upm.etsiinf.dam.coinapp.databinding.ActivityEditBinding;
import es.upm.etsiinf.dam.coinapp.main.ui.profile.ProfileViewModel;
import es.upm.etsiinf.dam.coinapp.main.ui.profile.ProfileViewModelFactory;
import es.upm.etsiinf.dam.coinapp.main.ui.profile.edit.viewmodel.EditViewModel;
import es.upm.etsiinf.dam.coinapp.main.ui.profile.edit.viewmodel.EditViewModelFactory;
import es.upm.etsiinf.dam.coinapp.register.ui.login.LoginViewModel;
import es.upm.etsiinf.dam.coinapp.register.ui.login.LoginViewModelFactory;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;
import es.upm.etsiinf.dam.coinapp.utils.ImageManager;

public class EditActivity extends AppCompatActivity {

    private ActivityEditBinding binding;
    private EditViewModel editViewModel;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ImageManager imageManager = new ImageManager();


        /*
            private final String usuario;
    private final String email;
    private final Drawable imageProfile;
    private final SharedPreferences sharedPreferences;
    private final Context context;
         */
        TextInputEditText user = findViewById(R.id.tiet_user_edit);
        TextInputEditText email = findViewById(R.id.tiet_email_edit);
        ShapeableImageView ip_profile = findViewById(R.id.iv_circle_profile_edit);

        String userText = getIntent().getStringExtra("username");
        String emailText = getIntent().getStringExtra("email");
        Drawable imageProfile = imageManager.getDrawableFromByte(getIntent().getByteArrayExtra("imageProfile"));

        editViewModel = new EditViewModelFactory(this,
                getSharedPreferences("login_preferences",MODE_PRIVATE),
                userText, emailText, imageProfile
        ).create(EditViewModel.class);

        user.setText(userText);
        email.setText(emailText);
        ip_profile.setImageDrawable(imageProfile);

        user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged (Editable editable) {
                editViewModel.changeUsername(user.getText().toString());
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
                    editViewModel.changeEmail(email.getText().toString());
                }else{
                    String hola="hola";
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