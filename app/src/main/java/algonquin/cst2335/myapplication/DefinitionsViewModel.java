package algonquin.cst2335.myapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class DefinitionsViewModel extends ViewModel {
    public MutableLiveData<ArrayList<WordDefinitionEntity>> wordDefintions = new MutableLiveData<>();
    public MutableLiveData<WordDefinitionEntity> selectedWordDefintions = new MutableLiveData<>();

}
