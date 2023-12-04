package algonquin.cst2335.myapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class WordViewModel extends ViewModel {
    public MutableLiveData<ArrayList<WordDefinitionEntity>> term = new MutableLiveData<>();
    public MutableLiveData<WordDefinitionEntity> selectedTerm = new MutableLiveData<>();


}
