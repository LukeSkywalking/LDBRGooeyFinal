package algonquin.cst2335.myapplication.Recipe;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import algonquin.cst2335.myapplication.R;

public class RecipesActivity extends AppCompatActivity {

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