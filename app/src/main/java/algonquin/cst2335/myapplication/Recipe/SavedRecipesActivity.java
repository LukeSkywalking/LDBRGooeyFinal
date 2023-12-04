package algonquin.cst2335.myapplication.Recipe;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import algonquin.cst2335.myapplication.R;

public class SavedRecipesActivity extends AppCompatActivity {

    private RecipeRepository repository;
    private RecyclerView recyclerView;
    private SavedRecipesAdapter adapter;

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

    private void openRecipeDetails(String recipeId) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("id", recipeId);
        startActivity(intent);
    }
}

