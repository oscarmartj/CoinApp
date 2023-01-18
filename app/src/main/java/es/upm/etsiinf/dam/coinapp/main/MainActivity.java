package es.upm.etsiinf.dam.coinapp.main;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;

import java.util.List;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.SplashActivity;
import es.upm.etsiinf.dam.coinapp.databinding.ActivityMainBinding;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;
import es.upm.etsiinf.dam.coinapp.services.notificaciones.NotificationScheduleJob;
import es.upm.etsiinf.dam.coinapp.services.updates.job.UpdateScheduleJob;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;
import es.upm.etsiinf.dam.coinapp.utils.KeyboardUtil;
import es.upm.etsiinf.dam.coinapp.utils.NotificationUtils;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        JobScheduler jobScheduler = getSystemService(JobScheduler.class);
        JobInfo jobInfo = jobScheduler.getPendingJob(999);
        if(jobInfo == null){ //que un nuevo trabajo no reemplace el que ya existe.
            NotificationUtils nutils = new NotificationUtils();
            nutils.createNotificationChannel(this); //crear canal de notificaciones
            NotificationScheduleJob job = new NotificationScheduleJob();
            job.scheduleJob(this);
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_ranking, R.id.navigation_magic, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


    }

}