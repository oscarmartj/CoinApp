package es.upm.etsiinf.dam.coinapp.main.ui.profile.edit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.databinding.ActivityEditBinding;

public class EditActivity extends AppCompatActivity {

    private ActivityEditBinding binding;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        binding = ActivityEditBinding.inflate(getLayoutInflater());

        MaterialToolbar toolbar = findViewById(R.id.topAppBar_editprofile);

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