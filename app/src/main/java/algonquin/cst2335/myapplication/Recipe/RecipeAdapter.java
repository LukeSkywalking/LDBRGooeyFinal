package algonquin.cst2335.myapplication.Recipe;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.List;

import algonquin.cst2335.myapplication.R;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private RecipeDAO dao;
    private List<Recipe> recipes;
    //private Context context;
    private OnRecipeDeleteListener deleteListener;

    public RecipeAdapter(List<Recipe> recipes, OnRecipeDeleteListener listener) {
        this.recipes = recipes;
       // this.context = context;
        this.deleteListener = listener;
    }
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
    public void onBindViewHolder(@NonNull ViewHolder holder,@SuppressLint("RecyclerView") int position) {
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
                if (deleteListener != null) {
                    deleteListener.onRecipeDelete(recipes.get(position));
                }
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
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }
    public void addRecipes(List<Recipe> additionalRecipes) {
        recipes.addAll(additionalRecipes);
        notifyDataSetChanged();
    }
    public interface OnRecipeDeleteListener {
        void onRecipeDelete(Recipe recipe);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewRecipe;
        TextView textViewTitle;
       ImageView buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRecipe = itemView.findViewById(R.id.imageViewRecipe);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}