package es.upm.etsiinf.dam.coinapp.main.ui.magic;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONException;

import java.security.SecureRandom;
import java.util.Locale;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.database.functions.CoinDB;
import es.upm.etsiinf.dam.coinapp.databinding.ContentMagicBinding;
import es.upm.etsiinf.dam.coinapp.databinding.FragmentMagicBinding;
import es.upm.etsiinf.dam.coinapp.modelos.Coin;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;
import es.upm.etsiinf.dam.coinapp.utils.ImageManager;

public class MagicFragment extends Fragment {

    private FragmentMagicBinding binding;
    private ContentMagicBinding contentBinding;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private BallView viewBall;
    private Context context;
    private MagicViewModel magicViewModel;
    private CoinDB coinDB;
    private ImageManager im;

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireActivity();
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        coinDB = new CoinDB(requireActivity());
        im = new ImageManager();

    }

    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMagicBinding.inflate(inflater, container, false);
        contentBinding = binding.includeActivity;
        FrameLayout frameLayout = binding.getRoot();
        viewBall = binding.viewBall;
        viewBall.setVisibility(View.VISIBLE);

        //Para recordar: ¿Porque no he utilizado el ViewModelProvider=
        //No utilizo el ViewModelProvider como en el fragmento de Ranking,
        //porque en este caso no quiero que el ViewModel se guarde.
        //Quiero que cada vez que se acceda al fragment, se vean las bolas,
        //para ellos es necesario generar un nuevo viewmodel en cada instancia del fragment
        MagicViewModelFactory factory = new MagicViewModelFactory(context, mSensorManager, mAccelerometer, viewBall);
        magicViewModel = factory.create(MagicViewModel.class);

        contentBinding.getRoot().setVisibility(View.INVISIBLE);


        ShapeableImageView coin_siv = binding.includeActivity.ivCircleMagicCoin;
        TextView coin_price = binding.includeActivity.magicPrice;
        TextView coin_name = binding.includeActivity.magicName;
        magicViewModel.getRandomCoin().observe(getViewLifecycleOwner(), coin1 -> {
            coin_siv.setImageDrawable(im.getDrawableFromByte(coin1.getImageBytes()));
            coin_name.setText("#"+coin1.getMarket_cap_rank()+"\n"+coin1.getName());
            int firstPositionWithout0 = DataManager.obtenerPrimeraPosicionDecimal(coin1.getCurrent_price());
            if(firstPositionWithout0>2){
                coin_price.setText("$"+String.format(Locale.US,"%,."+(firstPositionWithout0+1)+"f",coin1.getCurrent_price()));
            }else if(firstPositionWithout0==2 && String.valueOf(coin1.getCurrent_price()).length()>2){
                coin_price.setText("$"+String.format(Locale.US,"%,."+(firstPositionWithout0+1)+"f",coin1.getCurrent_price()));
            }else{
                coin_price.setText("$"+String.format(Locale.US,"%,.2f",coin1.getCurrent_price()));
            }
        });





        if(viewBall.getVisibility() != View.INVISIBLE){
            magicViewModel.getNumVisibleBalls().observe(getViewLifecycleOwner(), integer -> {
                if(integer ==0){
                    viewBall.setVisibility(View.INVISIBLE);
                    contentBinding.getRoot().setVisibility(View.VISIBLE);


                    //Crear aqui la magia de la animacion
                    AnimationSet animationSet = new AnimationSet(true);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(500);

                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphaAnimation.setDuration(500);

                    animationSet.addAnimation(scaleAnimation);
                    animationSet.addAnimation(alphaAnimation);


                    coin_siv.setAnimation(animationSet);
                    coin_name.setAnimation(animationSet);
                    coin_price.setAnimation(animationSet);

                    coin_siv.startAnimation(animationSet);
                    coin_name.startAnimation(animationSet);
                    coin_price.startAnimation(animationSet);


                }else{
                    if(integer > 0){
                        viewBall.setVisibility(View.VISIBLE);
                        contentBinding.getRoot().setVisibility(View.INVISIBLE);
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