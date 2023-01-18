package es.upm.etsiinf.dam.coinapp.main.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.upm.etsiinf.dam.coinapp.databinding.FragmentProfileBinding;
import es.upm.etsiinf.dam.coinapp.main.ui.profile.edit.EditActivity;
import es.upm.etsiinf.dam.coinapp.utils.KeyboardUtil;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        Context context = requireActivity();
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this, new ProfileViewModelFactory(context)).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        new KeyboardUtil(getActivity(),root);

        Button btn = binding.includeContentProfile.btnEditProfile;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(context, EditActivity.class);
                startActivity(intent);
            }
        });



        /*
        final TextView textView = binding.textNotifications;
        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return root;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        binding = null;
    }
}