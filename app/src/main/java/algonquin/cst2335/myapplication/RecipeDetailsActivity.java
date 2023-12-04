package algonquin.cst2335.myapplication;
// RecipeDetailsActivity.java

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
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
//    private RequestQueue requestQueue;
    private RecyclerView.Adapter myAdapter;
    protected String recipeName;
    private MyVolley myVolley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("RecipeDetailsActivity", "onCreate called");
//        requestQueue = (RequestQueue) MyVolley.newRequestQueue(this);
        myVolley = MyVolley.getInstance(this);

        // Now, use the requestQueue from MyVolley
        RequestQueue requestQueue = myVolley.getRequestQueue();

        binding = ActivityRecipeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize UI elements
        imageViewRecipeDetails = binding.imageViewRecipeDetails;
        buttonSaveRecipe = binding.buttonSaveRecipe;
        textViewTitleDetails = binding.textViewTitleDetails;


        Intent fromPrevious = getIntent();
        String Foodid = fromPrevious.getStringExtra("id");


        String searchedText = textViewTitleDetails.getText().toString().trim();

        String stringURL;
        try {
            stringURL = "https://api.spoonacular.com/recipes/" +
                    URLEncoder.encode(Foodid, "UTF-8") +
                    "/information?apiKey=b3013f46886845fcb8a9f6d64e736c2f"; // Replace YOUR_API_KEY with your actual API key
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // Make a GET request to the Spoonacular API
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                (response) -> {
                    Log.d("API_RESPONSE", response.toString());
                    try {
                        // Check if the response has the necessary details
                        if (response.has("id") && response.has("title") && response.has("image")) {
                            long id = response.getLong("id");
                            String title = response.getString("title");
                            String image = response.getString("image");
                            String summary = response.getString("summary");
                            String sourceUrl = response.getString("sourceUrl");

                            Recipe selectedRecipe = new Recipe(String.valueOf(id), title, image, "", "");

                            // Update UI based on the selected recipe
                            binding.textViewTitleDetails.setText(selectedRecipe.getTitle());
                            binding.textViewSummaryDetails.setText(summary);
                            binding.sourceUrl.setText(sourceUrl);

                            String imageUrl = selectedRecipe.getImage();

                            File f = new File(imageUrl);
                            if (f.exists()) {
                                imageViewRecipeDetails.setImageBitmap(BitmapFactory.decodeFile(imageUrl));
                            } else {
                                ImageRequest imgReq = new ImageRequest(image, bitmap -> {
                                    Log.d("IMAGE_LOAD", "Image loaded successfully");
                                    imageViewRecipeDetails.setImageBitmap(bitmap);
                                },
                                        1024, 1024, ImageView.ScaleType.CENTER, null, error -> {
                                    Log.e("IMAGE_LOAD_ERROR", error.toString());
                                });
                                requestQueue.add(imgReq);
                            }
                        } else {
                            Log.e("API_RESPONSE", "Response is missing necessary details");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Log the error
                    Log.e("API_ERROR", error.toString());
                    // Handle error
                    error.printStackTrace();
                });
        requestQueue.add(request);
    }

//        textViewTitleDetails.setOnClickListener(c -> {
//            )
//    };

    // Retrieve recipe details from Intent
    //String id = getIntent().getStringExtra("id");
    // Use recipeId to fetch additional details (Image, Summary) and update UI
    // ...

    // Set a click listener for the Save Recipe button if needed
//        buttonSaveRecipe.setOnClickListener(v ->
//    {
//        saveRecipeToDatabase();
//    });



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



