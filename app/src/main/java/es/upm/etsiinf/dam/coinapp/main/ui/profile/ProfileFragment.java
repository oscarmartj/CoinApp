package es.upm.etsiinf.dam.coinapp.main.ui.profile;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import static es.upm.etsiinf.dam.coinapp.utils.DataManager.CAMERA_PHOTO_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.database.functions.UserDB;
import es.upm.etsiinf.dam.coinapp.databinding.FragmentProfileBinding;
import es.upm.etsiinf.dam.coinapp.main.MainActivity;
import es.upm.etsiinf.dam.coinapp.main.ui.profile.edit.EditActivity;
import es.upm.etsiinf.dam.coinapp.main.ui.profile.edit.EditPasswordActivity;
import es.upm.etsiinf.dam.coinapp.modelos.User;
import es.upm.etsiinf.dam.coinapp.ui.login.LoginActivity;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;
import es.upm.etsiinf.dam.coinapp.utils.ImageManager;
import es.upm.etsiinf.dam.coinapp.utils.Usernames;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private byte[] imageProfile;

    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        Context context = requireActivity();
        imageProfile = new byte[]{};
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        SharedPreferences sp = context.getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
        ProfileViewModel profileViewModel = new ViewModelProvider(requireActivity(),new ProfileViewModelFactory(context,sp)).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        TextView changePassword = binding.includeContentProfile.twChangepassword;
        TextInputEditText user = binding.includeContentProfile.tietUser;
        TextInputEditText email = binding.includeContentProfile.tietEmail;
        ShapeableImageView shapeableImageView = binding.includeContentProfile.ivCircleMagicCoin;

        ImageManager manager = new ImageManager();

        profileViewModel.getUsuario().observe(getViewLifecycleOwner(), user::setText);
        profileViewModel.getEmail().observe(getViewLifecycleOwner(), email::setText);
        profileViewModel.getImageProfile().observe(getViewLifecycleOwner(), image -> {
            shapeableImageView.setImageDrawable(image);
            try {
                imageProfile = manager.getBytesFromBitmap(manager.getBitmapFromDrawable(image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(context, EditPasswordActivity.class);
                intent.putExtra("email",email.getText().toString());
                startActivity(intent);
            }
        });

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == RESULT_OK && result.getData() != null) {

                User userToUpdate;
                if(DataManager.getFlagNoEdit(context)){
                    userToUpdate = new User(
                            result.getData().getStringExtra("username"),
                            result.getData().getStringExtra("email"),
                            result.getData().getByteArrayExtra("imageProfile")
                    );
                }else{
                    userToUpdate = new User(
                            result.getData().getStringExtra("username"),
                            result.getData().getStringExtra("email"),
                            result.getData().getByteArrayExtra("imageProfileNoEdit"));
                }
                profileViewModel.setChanges(userToUpdate);

            }else if(result.getResultCode() == RESULT_CANCELED && result.getData() != null){
                byte[] imageProfileNoEdit = result.getData().getByteArrayExtra("imageProfileNoEdit");
                profileViewModel.changeImage(imageProfileNoEdit);

            }
        });

        Button btn = binding.includeContentProfile.btnEditProfile;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("username",user.getText().toString());
                intent.putExtra("email",email.getText().toString());
                intent.putExtra("imageProfile",imageProfile);
                launcher.launch(intent);
            }
        });

        Button btn_logout = binding.includeContentProfile.btnLogoutProfile;
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Usernames.logout(context);
                Intent backToLogin = new Intent(context, LoginActivity.class);
                startActivity(backToLogin);
                Toast.makeText(context, "Bye "+Usernames.firstToUppercase(user.getText().toString())+" !", Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        binding = null;
    }
}