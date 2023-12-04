package algonquin.cst2335.myapplication;


import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RecipeRepository {

    private RecipeDAO recipeDao;
    private LiveData<List<RecipeList>> allRecipes;
    private Executor executor = Executors.newSingleThreadExecutor();

    public RecipeRepository(Application application) {
        RecipeDatabase database = RecipeDatabase.getInstance(application);
        recipeDao = database.recipeDao();
        allRecipes = recipeDao.getAllRecipes();
    }

    public LiveData<RecipeList> getRecipeById(String recipeId) {
        return recipeDao.getRecipeById(recipeId);
    }

    public void insertRecipe(RecipeList recipe) {
        executor.execute(() -> recipeDao.insertRecipe(recipe));
    }

    public void deleteRecipe(RecipeList recipe) {
        executor.execute(() -> recipeDao.deleteRecipe(recipe));
    }

    public void deleteRecipeById(String recipeId) {
        RecipeList recipeToDelete = new RecipeList();
        recipeToDelete.setId(recipeId);
        deleteRecipe(recipeToDelete);
    }

    public LiveData<List<RecipeList>> getAllRecipes() {
        return allRecipes;
    }
}