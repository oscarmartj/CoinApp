package es.upm.etsiinf.dam.coinapp.main.ui.magic;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MagicViewModel extends ViewModel implements SensorEventListener {

    private Context context;
    private final MutableLiveData<Integer> numVisibleBalls;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private BallView mBallView;

    public MagicViewModel (Context context, SensorManager mSensorManager, Sensor mAccelerometer, BallView view) {
        this.context = context;
        this.mSensorManager = mSensorManager;
        this.mAccelerometer = mAccelerometer;
        this.mBallView = view;
        mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        numVisibleBalls = new MutableLiveData<>();
        numVisibleBalls.setValue(500);

    }

    public LiveData<Integer> getNumVisibleBalls () {
        return numVisibleBalls;
    }
    public void setNumVisibleBalls (int value) {
        numVisibleBalls.setValue(500);
    }
    public void activarSensor(){
        mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged (SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        double gravity = 9.8;
        double magnitude = Math.sqrt(x*x + y*y + z*z); //vector aceleración
        double magnitudeMinus = magnitude-gravity;

        double magnitudeThreshold = 1.2;

        if((Math.abs(magnitudeMinus)) > Math.abs(magnitudeThreshold)) {
            float speedFactor = (float) (magnitudeMinus / magnitudeThreshold);
            mBallView.increaseSpeed(speedFactor);
        }

        mBallView.countVisibleBalls();
        Log.i("visibleBalls",mBallView.getVisibleBalls()+"");
        if(mBallView.getVisibleBalls()==0) {
            numVisibleBalls.setValue(0);
            Log.i("visibleBalls", "entra aqui");
            //mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged (Sensor sensor, int i) {

    }
}