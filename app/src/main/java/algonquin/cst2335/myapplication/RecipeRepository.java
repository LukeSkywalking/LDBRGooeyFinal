/**
 * Repository class that acts as an intermediary between the ViewModel and the underlying data sources,
 * providing a clean API to access data related to recipes. It manages operations such as insertion,
 * deletion, and retrieval of recipe data from the Room database.
 */
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

    /**
     * Constructor for the RecipeRepository class. Initializes the RecipeDAO and LiveData objects.
     *
     * @param application The application context.
     */
    public RecipeRepository(Application application) {
        RecipeDatabase database = RecipeDatabase.getInstance(application);
        recipeDao = database.recipeDao();
        allRecipes = recipeDao.getAllRecipes();
    }

    /**
     * Retrieves a specific recipe from the database by its ID.
     *
     * @param recipeId The unique ID of the recipe.
     * @return LiveData containing the recipe information.
     */
    public LiveData<RecipeList> getRecipeById(String recipeId) {
        return recipeDao.getRecipeById(recipeId);
    }

    /**
     * Inserts a new recipe into the database in a background thread.
     *
     * @param recipe The RecipeList object representing the recipe to be inserted.
     */
    public void insertRecipe(RecipeList recipe) {
        executor.execute(() -> recipeDao.insertRecipe(recipe));
    }

    /**
     * Deletes a recipe from the database in a background thread.
     *
     * @param recipe The RecipeList object representing the recipe to be deleted.
     */
    public void deleteRecipe(RecipeList recipe) {
        executor.execute(() -> recipeDao.deleteRecipe(recipe));
    }

    /**
     * Deletes a recipe from the database by its ID in a background thread.
     *
     * @param recipeId The unique ID of the recipe to be deleted.
     */
    public void deleteRecipeById(String recipeId) {
        RecipeList recipeToDelete = new RecipeList();
        recipeToDelete.setId(recipeId);
        deleteRecipe(recipeToDelete);
    }

    /**
     * Retrieves all recipes from the database as LiveData.
     *
     * @return LiveData containing a list of all recipes.
     */
    public LiveData<List<RecipeList>> getAllRecipes() {
        return allRecipes;
    }
}
