package algonquin.cst2335.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Adapter class for displaying recipes in a RecyclerView.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

private  RecipeRepository repository;
    private List<Recipe> recipes;

    private OnRecipeDeleteListener deleteListener;

    private RecipeDatabase RecipeDatabase;

    /**
     * Constructor for RecipeAdapter.
     *
     * @param recipes  List of recipes to be displayed.
     * @param listener Listener for recipe delete events.
     */
    public RecipeAdapter(List<Recipe> recipes, OnRecipeDeleteListener listener) {
        this.recipes = recipes;
        this.deleteListener = listener;
    }

    /**
     * Clears the list of recipes.
     */
    public void clearRecipes() {
        recipes.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Recipe recipe = recipes.get(position);

        // Set the data to the views in the ViewHolder
        Glide.with(holder.itemView.getContext())
                .load(recipe.getImage())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .centerCrop()
                .into(holder.imageViewRecipe);

        // Set other TextViews with additional information
        holder.textViewTitle.setText(recipe.getTitle());
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the delete listener when the delete button is clicked
                repository.deleteRecipe((RecipeList) recipes);
            }
        });

        // Implement a click listener if needed
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe clickedRecipe = recipes.get(position);

                // Create an Intent to start RecipeDetailsActivity
                Intent intent = new Intent(holder.itemView.getContext(), RecipeDetailsActivity.class);

                // Pass necessary details to RecipeDetailsActivity
                intent.putExtra("id", clickedRecipe.getId());

                // Start RecipeDetailsActivity
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    /**
     * Sets the list of recipes to be displayed.
     *
     * @param recipes List of recipes.
     */
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    /**
     * Adds additional recipes to the existing list.
     *
     * @param additionalRecipes Additional recipes to be added.
     */
    public void addRecipes(List<Recipe> additionalRecipes) {
        recipes.addAll(additionalRecipes);
        notifyDataSetChanged();
    }

    /**
     * Interface for handling recipe delete events.
     */
    public interface OnRecipeDeleteListener {
        void onRecipeDelete(Recipe recipe);
    }

    /**
     * ViewHolder class for holding views in each RecyclerView item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewRecipe;
        TextView textViewTitle;
        ImageView buttonDelete;

        /**
         * Constructor for ViewHolder.
         *
         * @param itemView The view of the RecyclerView item.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRecipe = itemView.findViewById(R.id.imageViewRecipe);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
