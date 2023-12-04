package algonquin.cst2335.myapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class RecipeViewModel extends ViewModel {

    public MutableLiveData<ArrayList<RecipeList>> recipeList = new MutableLiveData<>();

    public MutableLiveData<RecipeList> selectedRecipe = new MutableLiveData<>();

}
