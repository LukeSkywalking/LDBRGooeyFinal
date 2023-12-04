package algonquin.cst2335.myapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class FavoriteLocationViewModel extends ViewModel {

    /**
     * The MutableLiveData that holds an ArrayList of FavoriteLocation objects.
     */
    private MutableLiveData<Integer> locationCount = new MutableLiveData<>();
    public MutableLiveData<ArrayList<FavoriteLocation>> favoriteLocations = new MutableLiveData<>();

    public  MutableLiveData<FavoriteLocation> favoriteLocationVM = new MutableLiveData<>();
    public void addFavoriteLocation(FavoriteLocation favoriteLocation) {
        ArrayList<FavoriteLocation> currentList = favoriteLocations.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }

        currentList.add(favoriteLocation);
        favoriteLocations.postValue(currentList);

        // Increment the location count
        int count = locationCount.getValue() != null ? locationCount.getValue() : 0;
        locationCount.setValue(count + 1);
    }
}
