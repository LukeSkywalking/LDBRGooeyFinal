package algonquin.cst2335.myapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
/**
 * DefinitionsViewModel is a ViewModel class that manages and provides access to MutableLiveData objects
 * for handling word definitions and selected word definitions.
 *
 * It extends ViewModel to retain and manage UI-related data across configuration changes,
 * allowing communication between the UI and the underlying data-handling classes.
 */
public class DefinitionsViewModel extends ViewModel {
    /**
     * MutableLiveData containing a list of WordDefinitionEntity objects representing word definitions.
     * Observers can be registered to listen for changes in this data.
     */
    public MutableLiveData<ArrayList<WordDefinitionEntity>> wordDefintions = new MutableLiveData<>();
    /**
     * MutableLiveData holding a single WordDefinitionEntity representing the selected word definition.
     * Observers can be registered to listen for changes in this selected data.
     */
    public MutableLiveData<WordDefinitionEntity> selectedWordDefintions = new MutableLiveData<>();

}
