package es.upm.etsiinf.dam.coinapp.main.ui.magic;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.databinding.ContentMagicBinding;
import es.upm.etsiinf.dam.coinapp.databinding.FragmentMagicBinding;

public class MagicFragment extends Fragment {

    private FragmentMagicBinding binding;
    private ContentMagicBinding contentBinding;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private BallView viewBall;
    private MagicViewModel magicViewModel;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        Log.i("ViewBall","entra en createview;");
        Context context = requireActivity();
        sharedPreferences = context.getSharedPreferences("balls",Context.MODE_PRIVATE);
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        binding = FragmentMagicBinding.inflate(inflater, container, false);
        contentBinding = binding.includeActivity;
        FrameLayout frameLayout = binding.getRoot();
        viewBall = binding.viewBall;

        magicViewModel =
                new ViewModelProvider(this, new MagicViewModelFactory(context,mSensorManager,mAccelerometer, viewBall)).get(MagicViewModel.class);
        contentBinding.getRoot().setVisibility(View.INVISIBLE);

        if(sharedPreferences.getInt("bolas",1)==0){
            magicViewModel.setNumVisibleBalls(500);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("bolas",500);
            editor.apply();
        }

        if(viewBall.getVisibility() != View.INVISIBLE){
            magicViewModel.getNumVisibleBalls().observe(getViewLifecycleOwner(), integer -> {
                if(integer ==0){
                    Log.i("ViewBall","entra en ==;");
                    viewBall.setVisibility(View.INVISIBLE);
                    contentBinding.getRoot().setVisibility(View.VISIBLE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("bolas",integer);
                    editor.apply();

                }else{
                    Log.i("ViewBall","entra en !=;");
                    viewBall.setVisibility(View.VISIBLE);
                    contentBinding.getRoot().setVisibility(View.INVISIBLE);
                }
            });
        }
        return frameLayout;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        /*
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.i("ViewBall","entra en destroy");
        editor.remove("bolas");
        editor.apply();*/
        binding = null;
    }

    @Override
    public void onStart () {
        super.onStart();
        Log.i("ViewBall","onStart");
        viewBall.setVisibility(View.VISIBLE);
    }
}