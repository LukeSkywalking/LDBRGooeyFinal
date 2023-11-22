package algonquin.cst2335.myapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SongsViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Songs>> songs = new MutableLiveData<>();

    public MutableLiveData<Songs> selectedSongs = new MutableLiveData<>();
}
