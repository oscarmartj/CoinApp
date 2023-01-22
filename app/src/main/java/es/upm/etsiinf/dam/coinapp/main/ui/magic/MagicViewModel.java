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

import org.json.JSONException;

import java.security.SecureRandom;

import es.upm.etsiinf.dam.coinapp.database.functions.CoinDB;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;

public class MagicViewModel extends ViewModel implements SensorEventListener {

    private Context context;
    private final MutableLiveData<Integer> numVisibleBalls;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private BallView mBallView;
    private final MutableLiveData<Coin> randomCoin;
    private CoinDB coinDB;
    private boolean reset=false;

    public MagicViewModel (Context context, SensorManager mSensorManager, Sensor mAccelerometer, BallView view) {
        this.context = context;
        this.coinDB = new CoinDB(context);
        this.mSensorManager = mSensorManager;
        this.mAccelerometer = mAccelerometer;
        mBallView = view;
        mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        numVisibleBalls = new MutableLiveData<>();
        numVisibleBalls.setValue(500);

        randomCoin = new MutableLiveData<>();

        randomCoin.setValue(getRandomCoin(coinDB));

    }

    public LiveData<Coin> getRandomCoin(){return randomCoin;}

    private Coin getRandomCoin (CoinDB coinDB) {
        int numCoinsInDB = coinDB.getNumOfRecords();

        SecureRandom secureRandom = new SecureRandom(); //he querido utilizar securerandom en vez de la clase random, es mas seguro por lo visto.
        int randomNumber = secureRandom.nextInt(numCoinsInDB)+1; //entre 1....numCoinsInDB
        Coin coin = new Coin();
        try {
            coin = coinDB.getCoinByMarketCap(randomNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return coin;
    }

    public LiveData<Integer> getNumVisibleBalls () {
        return numVisibleBalls;
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
        if(mBallView.getVisibleBalls()==0) {
            numVisibleBalls.setValue(0);
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged (Sensor sensor, int i) {

    }

    @Override
    protected void onCleared () {
        super.onCleared();
    }
}