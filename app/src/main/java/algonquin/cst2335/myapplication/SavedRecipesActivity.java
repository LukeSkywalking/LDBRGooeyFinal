/**
 * The SavedRecipesActivity class represents an activity for displaying a list of saved recipes.
 * It extends the AppCompatActivity and is responsible for setting up the UI to show saved recipes.
 */
package algonquin.cst2335.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SavedRecipesActivity extends AppCompatActivity {

    private RecipeRepository repository;
    private RecyclerView recyclerView;
    private SavedRecipesAdapter adapter;

    /**
     * Called when the activity is starting. This method initializes the activity,
     * sets up the UI to display saved recipes using a RecyclerView, and observes the LiveData
     * from the repository to update the UI when the saved recipes change.
     *
     * @param savedInstanceState A Bundle containing the data most recently supplied in onSaveInstanceState().
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes);

        repository = new RecipeRepository(getApplication());

        recyclerView = findViewById(R.id.recyclerViewSavedRecipes);
        adapter = new SavedRecipesAdapter(recipe -> openRecipeDetails(recipe.getId()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Observe the LiveData from the repository to update the UI when the saved recipes change
        repository.getAllRecipes().observe(this, recipes -> {
            // Update the adapter with the new list of recipes
            adapter.submitList(recipes);
        });
    }

    /**
     * Opens the RecipeDetailsActivity to view details of the selected recipe.
     *
     * @param recipeId The ID of the selected recipe.
     */
    private void openRecipeDetails(String recipeId) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("id", recipeId);
        startActivity(intent);
    }
}
