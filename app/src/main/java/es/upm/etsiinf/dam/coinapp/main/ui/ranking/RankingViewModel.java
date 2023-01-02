package es.upm.etsiinf.dam.coinapp.main.ui.ranking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RankingViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private int page = 1;
    private boolean isLoading = false;

    public RankingViewModel () {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText () {
        return mText;
    }
}