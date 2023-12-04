package algonquin.cst2335.myapplication;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface RecipeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(RecipeList recipe);

    @Delete
    void deleteRecipe(RecipeList recipe);

    @Query("SELECT * FROM recipes")
    LiveData<List<RecipeList>> getAllRecipes();

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    LiveData<RecipeList> getRecipeById(String recipeId);



}