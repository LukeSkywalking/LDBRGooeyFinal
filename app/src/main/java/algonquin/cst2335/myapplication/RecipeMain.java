package algonquin.cst2335.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeMain extends AppCompatActivity implements RecipeAdapter.OnRecipeDeleteListener {

    private EditText editTextRecipe;
    private Button buttonSearch;
    private RecyclerView recyclerViewRecipes;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;
    private RecipeRepository recipeRepository;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_help) {
            // Show AlertDialog with instructions
            showHelpDialog();
            return true;
        }
        if (id == android.R.id.home) {
            // Handle the Up button (back button) click
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help_title);
        builder.setMessage(getString(R.string.help_message)); // Replace with actual instructions
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the "OK" button click if needed
            }
        });

        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_main);

        // Initialize UI components
        editTextRecipe = findViewById(R.id.editTextRecipe);
        buttonSearch = findViewById(R.id.buttonSearch);
        recyclerViewRecipes = findViewById(R.id.recyclerViewRecipes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recipeRepository = new RecipeRepository(getApplication());

        // Enable the Up button (back button)
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set up RecyclerView
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(recipeList, this);
        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRecipes.setAdapter(recipeAdapter);

        // Set up button click listener
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editTextRecipe.getText().toString().trim();
                if (!query.isEmpty()) {
                    // Perform the search
                    performRecipeSearch(query);
                } else {
                    // Show a Toast indicating an empty search term
                    Toast.makeText(RecipeMain.this, R.string.enter_recipe_name, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void performRecipeSearch(String query) {
        // Replace "YOUR_API_KEY" with your actual Spoonacular API key
        String apiKey = "b3013f46886845fcb8a9f6d64e736c2f";
        String searchUrl = "https://api.spoonacular.com/recipes/complexSearch?query=" + query + "&apiKey=" + apiKey;

        JsonObjectRequest searchRequest = new JsonObjectRequest(
                Request.Method.GET,
                searchUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject searchResponse) {
                        Log.d("MainActivity", "Search response received: " + searchResponse.toString());
                        // Parse the search response and update the RecyclerView
                        updateRecipeList(searchResponse);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MainActivity", "Error fetching recipes: " + error.toString());
                        // Handle error response
                        Toast.makeText(RecipeMain.this, R.string.error_fetching_recipes, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the search request to the Volley request queue
        Volley.getInstance(this).getRequestQueue().add(searchRequest);
    }


    private void updateRecipeList(JSONObject searchResponse) {
        try {
            // Check if the "results" array is not null and has elements
            if (searchResponse.has("results") && searchResponse.getJSONArray("results").length() > 0) {
                // Extract recipes from the response
                JSONArray resultsArray = searchResponse.getJSONArray("results");

                List<Recipe> recipes = new ArrayList<>();

                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject recipeObject = resultsArray.getJSONObject(i);
                    String id = recipeObject.has("id") ? recipeObject.getString("id") : "";
                    String title = recipeObject.has("title") ? recipeObject.getString("title") : "";
                    String image = recipeObject.has("image") ? recipeObject.getString("image") : "";
                    String summary = recipeObject.has("summary") ? recipeObject.getString("summary") : "";
                    String sourceUrl = recipeObject.has("sourceUrl") ? recipeObject.getString("sourceUrl") : "";

                    Recipe recipe = new Recipe(id, title, image, summary, sourceUrl);
                    recipes.add(recipe);

                    // Insert the recipe into the database
                    RecipeList roomRecipe = new RecipeList();
                    roomRecipe.setId(id);
                    roomRecipe.setTitle(title);
                    roomRecipe.setImage(image);
                    roomRecipe.setSummary(summary);
                    roomRecipe.setSourceUrl(sourceUrl);

                    // Insert the recipe into the database
                    recipeRepository.insertRecipe(roomRecipe);
                }

                // Update the RecyclerView with the new list of recipes
                recipeAdapter.setRecipes(recipes);
            } else {
                // Display a message indicating that no recipes were found
                Toast.makeText(RecipeMain.this,  R.string.no_recipes_found, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    private void showDeleteConfirmationDialog(final Recipe recipe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_recipe_title);
        builder.setMessage(R.string.delete_recipe_message);
        builder.setPositiveButton(R.string.delete_recipe_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the recipe from the database
                deleteRecipeFromDatabase(recipe);
            }
        });
        builder.setNegativeButton(R.string.delete_recipe_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel the delete operation
            }
        });
        builder.show();
    }

    private void deleteRecipeFromDatabase(final Recipe recipe) {
        // Convert the Recipe object to RecipeList and then delete it from the database
        RecipeList roomRecipe = new RecipeList(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getImage(),
                recipe.getSummary(),
                recipe.getSourceUrl()
        );

        recipeRepository.deleteRecipe(roomRecipe);
        // Refresh the RecyclerView after deletion
        refreshRecyclerView();
    }

    private void refreshRecyclerView() {
        // Retrieve the updated list of recipes from the database
        recipeRepository.getAllRecipes().observe(this, recipeLists -> {
            // Convert RecipeList objects to Recipe objects and update the adapter
            List<Recipe> updatedRecipes = convertToRecipes(recipeLists);
            recipeAdapter.setRecipes(updatedRecipes);
        });
    }

    private List<Recipe> convertToRecipes(List<RecipeList> recipeLists) {
        // Convert RecipeList objects to Recipe objects
        List<Recipe> recipes = new ArrayList<>();
        for (RecipeList roomRecipe : recipeLists) {
            Recipe recipe = new Recipe(
                    roomRecipe.getId(),
                    roomRecipe.getTitle(),
                    roomRecipe.getImage(),
                    roomRecipe.getSummary(),
                    roomRecipe.getSourceUrl()
            );
            recipes.add(recipe);
        }
        return recipes;
    }

    @Override
    public void onRecipeDelete(Recipe recipe) {
        showDeleteConfirmationDialog(recipe);

    }
}



