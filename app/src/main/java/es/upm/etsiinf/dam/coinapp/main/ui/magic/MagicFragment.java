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
    private SharedPreferences sharedPreferences;
    private Context context;
    private MagicViewModel magicViewModel;

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireActivity();
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        Log.i("ViewBall","entra en createview;");

        binding = FragmentMagicBinding.inflate(inflater, container, false);
        contentBinding = binding.includeActivity;
        FrameLayout frameLayout = binding.getRoot();
        viewBall = binding.viewBall;
        viewBall.setVisibility(View.VISIBLE);

        MagicViewModelFactory factory = new MagicViewModelFactory(context, mSensorManager, mAccelerometer, viewBall);
        magicViewModel = factory.create(MagicViewModel.class);

        contentBinding.getRoot().setVisibility(View.INVISIBLE);


        if(viewBall.getVisibility() != View.INVISIBLE){
            magicViewModel.getNumVisibleBalls().observe(getViewLifecycleOwner(), integer -> {
                if(integer ==0){
                    Log.i("ViewBall","entra en ==;");
                    viewBall.setVisibility(View.INVISIBLE);
                    contentBinding.getRoot().setVisibility(View.VISIBLE);
                }else{
                    if(integer > 0){
                        Log.i("ViewBall","entra en !=;");
                        viewBall.setVisibility(View.VISIBLE);
                        contentBinding.getRoot().setVisibility(View.INVISIBLE);
                        /*
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("bolas",500);
                        editor.apply();*/
                    }
                }
            });
        }
        return frameLayout;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        binding = null;
        magicViewModel = null;
    }
}