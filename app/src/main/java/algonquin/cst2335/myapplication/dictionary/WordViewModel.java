package algonquin.cst2335.myapplication.dictionary;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
/**
 * ViewModel class responsible for managing and storing WordDefinitionEntity data.
 */
public class WordViewModel extends ViewModel {
    /**
     * MutableLiveData object to hold a list of WordDefinitionEntity objects.
     */
    public MutableLiveData<ArrayList<WordDefinitionEntity>> term = new MutableLiveData<>();
    /**
     * MutableLiveData object to hold a selected WordDefinitionEntity object.
     */
    public MutableLiveData<WordDefinitionEntity> selectedTerm = new MutableLiveData<>();


}
