package es.upm.etsiinf.dam.coinapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;


import java.util.ArrayList;
import java.util.Random;

public class MagicActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private BallView mBallView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mBallView = new BallView(this);
        setContentView(mBallView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBallView.countVisibleBalls();
        if (mBallView.getVisibleBalls() > 0) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        double gravity = 9.8;
        double magnitude = Math.sqrt(x*x + y*y + z*z); //vector aceleración
        double magnitudeMinus = magnitude-gravity;

        double magnitudeThreshold = 1.5;

        if((Math.abs(magnitudeMinus)) > Math.abs(magnitudeThreshold)) {
            float speedFactor = (float) (magnitudeMinus / magnitudeThreshold);
            mBallView.increaseSpeed(speedFactor);
        }

        mBallView.countVisibleBalls();
        if(mBallView.getVisibleBalls()==0) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class BallView extends View {
        private class Ball {
            float x;
            float y;
            float xSpeed;
            float ySpeed;
            int radius;
            int color;
        }
        private ArrayList<Ball> mBalls;
        private Paint mPaint;
        private Random rnd = new Random();
        private int width;
        private int height;

        private int visibleBalls = 0;
        private int notVisibleBalls = 0;

        public BallView(Context context) {
            super(context);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            this.width = displayMetrics.widthPixels;
            this.height = displayMetrics.heightPixels;
            mBalls = new ArrayList<>();
            for(int i = 0; i<500; i++){
                Ball ball = new Ball();
                ball.x = rnd.nextInt(width);
                ball.y = rnd.nextInt(height);
                ball.xSpeed = rnd.nextFloat()*10-5;
                ball.ySpeed = rnd.nextFloat()*10-5;
                ball.radius = 50;
                ball.color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                mBalls.add(ball);
            }
            mPaint = new Paint();
            moveBalls();
        }

        public int getVisibleBalls () {
            return visibleBalls;
        }

        public void increaseSpeed(float factor) {
            for(Ball ball : mBalls) {
                ball.xSpeed *= factor;
                ball.ySpeed *= factor;
            }
        }

        public void countVisibleBalls(){
            visibleBalls = 0;
            notVisibleBalls = 0;
            for(Ball ball : mBalls){
                if(ball.x >=0 && ball.x <= width && ball.y >=0 && ball.y <= height){
                    visibleBalls++;
                }else{
                    notVisibleBalls++;
                }
            }
        }

        private void moveBalls(){
            for (Ball ball : mBalls) {
                ball.x += ball.xSpeed;
                ball.y += ball.ySpeed;

                if(ball.x<= 0 ||ball.x >= this.width-ball.radius){
                    ball.xSpeed = -ball.xSpeed;
                    ball.xSpeed *= 1.5;
                    //reducir la velocidad tras x mills
                    postDelayed(()->ball.xSpeed/=1.5,200);
                }

                if(ball.y <=0 || ball.y >= this.height-ball.radius){
                    ball.ySpeed = -ball.ySpeed;
                    ball.ySpeed *= 1.5;
                    postDelayed(()->ball.ySpeed/=1.5,200);
                }
            }
            postInvalidate();
            postDelayed(this::moveBalls, 10);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            for(Ball ball : mBalls) {
                mPaint.setColor(ball.color);
                canvas.drawCircle(ball.x, ball.y, ball.radius, mPaint);
            }
        }
    }
}
