package es.upm.etsiinf.dam.coinapp.main.ui.magic;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.upm.etsiinf.dam.coinapp.databinding.FragmentMagicBinding;

public class MagicFragment extends Fragment {

    private FragmentMagicBinding binding;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private View view;
    private boolean flag=false;

    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        Context context = requireActivity();
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(!flag) {
            view = new BallView(requireActivity());
        }else{
            view = binding.getRoot();
        }
        MagicViewModel magicViewModel =
                new ViewModelProvider(this, new MagicViewModelFactory(context,mSensorManager,mAccelerometer, (BallView) view)).get(MagicViewModel.class);

        binding = FragmentMagicBinding.inflate(inflater, container, false);

        final TextView textView = binding.textDashboard;
        magicViewModel.getNumVisibleBalls().observe(getViewLifecycleOwner(), integer -> {
            if(integer ==0){
                flag=true;
            }
        });
        return view;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume () {
        super.onResume();
    }
}