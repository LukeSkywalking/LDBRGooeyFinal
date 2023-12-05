/**
 * The RecipesActivity class represents an activity for displaying detailed information about a selected recipe.
 * It extends the AppCompatActivity and is responsible for updating the UI with the details of the selected recipe.
 */
package algonquin.cst2335.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class RecipesActivity extends AppCompatActivity {

    /**
     * Called when the activity is starting. This method initializes the activity,
     * retrieves the selected recipe from the intent, and updates the UI with recipe details.
     *
     * @param savedInstanceState A Bundle containing the data most recently supplied in onSaveInstanceState().
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        // Retrieve the selected recipe from the intent
        Recipe recipe = getIntent().getParcelableExtra("recipe");

        // Update the UI with recipe details (similar to how you've done in RecipeAdapter)
        ImageView imageViewRecipe = findViewById(R.id.recipeImageView);
        TextView textViewTitle = findViewById(R.id.recipeNameTextView);

        Glide.with(this)
                .load(recipe.getImage())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .centerCrop()
                .into(imageViewRecipe);

        textViewTitle.setText(recipe.getTitle());
    }
}
