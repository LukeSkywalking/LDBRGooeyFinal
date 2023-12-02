package algonquin.cst2335.myapplication.deezer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SongsViewModel extends ViewModel {
    public MutableLiveData<List<Songs>> songs = new MutableLiveData<>();

    public MutableLiveData<Songs> selectedSongs = new MutableLiveData<>();


}
