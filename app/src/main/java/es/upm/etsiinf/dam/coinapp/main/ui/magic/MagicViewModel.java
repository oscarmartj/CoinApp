package es.upm.etsiinf.dam.coinapp.main.ui.magic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MagicViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MagicViewModel () {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText () {
        return mText;
    }
}