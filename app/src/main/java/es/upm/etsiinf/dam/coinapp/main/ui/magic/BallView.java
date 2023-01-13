package es.upm.etsiinf.dam.coinapp.main.ui.magic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Random;


public class BallView extends View {
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

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Inicializar variables y configurar la vista

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
