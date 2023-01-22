package es.upm.etsiinf.dam.coinapp.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.SplashActivity;
import es.upm.etsiinf.dam.coinapp.databinding.ActivityMainBinding;
import es.upm.etsiinf.dam.coinapp.services.notificaciones.NotificationScheduleJob;
import es.upm.etsiinf.dam.coinapp.utils.ConnectionManager;
import es.upm.etsiinf.dam.coinapp.utils.NotificationUtils;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ConnectionManager connectionManager;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = MainActivity.this;
        connectionManager = new ConnectionManager(MainActivity.this);

        //ACTUALIZAR CONEXIÓN ENTRE SI O NO, ESTA PENDIENTE
        connectionManager.connectionCheck(()-> runOnUiThread(() -> {

            Intent intent = new Intent(context, SplashActivity.class);
            startActivity(intent);
            finish();
        }));

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NotificationUtils nutils = new NotificationUtils();
        nutils.createNotificationChannel(this); //crear canal de notificaciones
        NotificationScheduleJob job = new NotificationScheduleJob();
        job.scheduleJob(this);

        BottomNavigationView navView = findViewById(R.id.nav_view);


        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_ranking, R.id.navigation_magic, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);



    }
}