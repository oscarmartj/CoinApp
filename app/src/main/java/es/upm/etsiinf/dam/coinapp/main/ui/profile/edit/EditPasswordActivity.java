package es.upm.etsiinf.dam.coinapp.main.ui.profile.edit;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.databinding.ActivityEditBinding;
import es.upm.etsiinf.dam.coinapp.databinding.ActivityEditpasswordBinding;

public class EditPasswordActivity extends AppCompatActivity {

    private ActivityEditpasswordBinding binding;
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpassword);

        binding = ActivityEditpasswordBinding.inflate(getLayoutInflater());

        MaterialToolbar toolbar = findViewById(R.id.topAppBar_editpassword);

        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        toolbar.setOnMenuItemClickListener(menuItem -> {
            if(menuItem.getItemId() == R.id.edit_menu_option) {
                Toast.makeText(this, " Icon Clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }
}
