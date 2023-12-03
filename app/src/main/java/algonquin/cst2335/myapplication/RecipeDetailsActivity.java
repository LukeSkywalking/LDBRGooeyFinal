package algonquin.cst2335.myapplication;
// RecipeDetailsActivity.java

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import algonquin.cst2335.myapplication.databinding.ActivityRecipeDetailsBinding;

public class RecipeDetailsActivity extends AppCompatActivity {
    private ActivityRecipeDetailsBinding binding;
    private ImageView imageViewRecipeDetails;
    private TextView textViewTitleDetails;
    private TextView textViewSummaryDetails;
    private Button buttonSaveRecipe;
    ArrayList<Recipe> recipesList = new ArrayList<>();
    RequestQueue queue;
    private RecyclerView.Adapter myAdapter;
    protected String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecipeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.activity_recipe_details);

        // Initialize UI elements
        imageViewRecipeDetails = findViewById(R.id.imageViewRecipeDetails);
//        textViewSummaryDetails = findViewById(R.id.textViewSummaryDetails);
        buttonSaveRecipe = findViewById(R.id.buttonSaveRecipe);
        textViewTitleDetails = findViewById(R.id.textViewTitleDetails);
        if (binding!= null) {
            binding.textViewTitleDetails.setText("Your Text");
        }
        binding.textViewTitleDetails.setOnClickListener(c -> {
            String searchedText = binding.textViewTitleDetails.getText().toString().trim();

            String stringURL;
            try {
                stringURL = "https://api.spoonacular.com/recipes/complexSearch?query=" +
                        URLEncoder.encode(searchedText, "UTF-8") +
                        "&apiKey=b3013f46886845fcb8a9f6d64e736c2f"; // Replace YOUR_API_KEY with your actual API key
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            // Make a GET request to the Spoonacular API
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {
                        try {
                            // Get the array of recipes from the response
                            JSONArray recipesArray = response.getJSONArray("results");

                            // Clear the existing recipesList before adding new data
                            recipesList.clear();

                            Recipe recipe = null;
                            for (int i = 0; i < recipesArray.length(); i++) {
                                JSONObject recipeObject = recipesArray.getJSONObject(i);
                                long id = recipeObject.getLong("id");
                                String title = recipeObject.getString("title");
                                String image = recipeObject.getString("image");
                                // You can fetch other details like summary and source URL as needed

                                recipe = new Recipe(String.valueOf(id), title, image, "", "");

                                recipesList.add(recipe);
                            }
                            // Notify the adapter that the data set has changed
                            String imageUrl = "https://api.spoonacular.com/recipes/" + recipe.getImage() + ".png";

                            File f = new File(recipeName);
                            if (f.exists()) {
                                binding.imageViewRecipeDetails.setImageBitmap(BitmapFactory.decodeFile(recipeName));
                            } else {
                                ImageRequest imgReq = new ImageRequest(imageUrl, bitmap -> binding.imageViewRecipeDetails.setImageBitmap(bitmap),
                                        1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {
                                });
                                queue.add(imgReq);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        // Handle error
                        error.printStackTrace();
                    });
            queue.add(request);
        });
        // Retrieve recipe details from Intent
      //String id = getIntent().getStringExtra("id");
        // Use recipeId to fetch additional details (Image, Summary) and update UI
        // ...

        // Set a click listener for the Save Recipe button if needed
        buttonSaveRecipe.setOnClickListener(v -> {
            // Implement logic to save the recipe to the database
            // ...
            saveRecipeToDatabase();
        });
    }


    private void saveRecipeToDatabase() {
        // Retrieve recipe details from UI elements
        String recipeId = getIntent().getStringExtra("recipeId");
        String title = textViewTitleDetails.getText().toString();
        String summary = textViewSummaryDetails.getText().toString();
        // Add more fields as needed

        // Create a RecipeList object with the retrieved details
        RecipeList recipe = new RecipeList();
        recipe.setId(recipeId);
        recipe.setTitle(title);
        recipe.setSummary(summary);
        // Set other fields as needed

        // Use the RecipeRepository to insert the recipe into the database
        RecipeRepository repository = new RecipeRepository(getApplication());
        repository.insertRecipe(recipe);

        // Show a confirmation message or navigate to a different screen if needed
        Toast.makeText(RecipeDetailsActivity.this, "Recipe saved to database", Toast.LENGTH_SHORT).show();
    }
}



