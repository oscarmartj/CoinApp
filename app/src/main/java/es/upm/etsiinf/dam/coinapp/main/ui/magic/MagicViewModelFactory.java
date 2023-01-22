package es.upm.etsiinf.dam.coinapp.main.ui.magic;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MagicViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private BallView view;

    public MagicViewModelFactory(Context context, SensorManager mSensorManager, Sensor mAccelerometer, View view) {
        this.context = context;
        this.mSensorManager = mSensorManager;
        this.mAccelerometer = mAccelerometer;
        this.view = (BallView) view;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MagicViewModel.class)) {
            return (T) new MagicViewModel(context,mSensorManager,mAccelerometer, view);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}