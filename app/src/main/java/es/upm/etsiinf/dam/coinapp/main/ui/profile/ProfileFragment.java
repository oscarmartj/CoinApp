package es.upm.etsiinf.dam.coinapp.main.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import es.upm.etsiinf.dam.coinapp.databinding.FragmentProfileBinding;
import es.upm.etsiinf.dam.coinapp.main.ui.profile.edit.EditActivity;
import es.upm.etsiinf.dam.coinapp.main.ui.profile.edit.EditPasswordActivity;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        Context context = requireActivity();
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        SharedPreferences sp = context.getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this, new ProfileViewModelFactory(context,sp)).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button btn = binding.includeContentProfile.btnEditProfile;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(context, EditActivity.class);
                startActivity(intent);
            }
        });

        TextView changePassword = binding.includeContentProfile.twChangepassword;
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(context, EditPasswordActivity.class);
                startActivity(intent);
            }
        });
        TextInputEditText user = binding.includeContentProfile.tietUser;
        TextInputEditText email = binding.includeContentProfile.tietEmail;

        profileViewModel.getUsuario().observe(getViewLifecycleOwner(), user::setText);
        profileViewModel.getEmail().observe(getViewLifecycleOwner(), email::setText);


        return root;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        binding = null;
    }
}