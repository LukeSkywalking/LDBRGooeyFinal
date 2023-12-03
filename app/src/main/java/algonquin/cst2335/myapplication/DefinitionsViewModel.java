package algonquin.cst2335.myapplication;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class DefinitionsViewModel {
    public MutableLiveData<ArrayList<WordDefinitionEntity>> wordDefintions = new MutableLiveData<>();

    public MutableLiveData<WordDefinitionEntity> selectedWordDefintions = new MutableLiveData<>();

}
