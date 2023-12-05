/**
 * The SavedRecipesAdapter class is a RecyclerView adapter for displaying a list of saved recipes.
 * It extends the ListAdapter class and uses DiffUtil to efficiently update the list of recipes.
 */
package algonquin.cst2335.myapplication;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class SavedRecipesAdapter extends ListAdapter<RecipeList, SavedRecipesAdapter.RecipeViewHolder> {

    private final OnRecipeClickListener listener;

    /**
     * Constructs a SavedRecipesAdapter with the given listener for recipe click events.
     *
     * @param listener The listener for recipe click events.
     */
    public SavedRecipesAdapter(OnRecipeClickListener listener) {
        super(new DiffUtil.ItemCallback<RecipeList>() {
            @Override
            public boolean areItemsTheSame(@NonNull RecipeList oldItem, @NonNull RecipeList newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull RecipeList oldItem, @NonNull RecipeList newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.listener = listener;
    }

    /**
     * Called when RecyclerView needs a new {@link RecipeViewHolder} of the given type to represent
     * an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new RecipeViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method updates
     * the contents of the {@link RecipeViewHolder} to reflect the item at the given position.
     *
     * @param holder   The RecipeViewHolder that holds the View to be updated.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeList recipe = getItem(position);

        holder.bind(recipe);




    }

    /**
     * The RecipeViewHolder class represents a ViewHolder for the saved recipe items in the RecyclerView.
     * It holds references to the UI elements and binds the data to the views.
     */
    class RecipeViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewTitle;
        private final Button buttonDelete;

        /**
         * Constructs a RecipeViewHolder with the given View representing a saved recipe item.
         *
         * @param itemView The View representing a saved recipe item.
         */
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

            // Set click listener for the entire item view
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    RecipeList recipe = getItem(position);
                    listener.onRecipeClick(recipe);
                }
            });

            // Set click listener for delete button
            buttonDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    RecipeList recipe = getItem(position);
                    // Uncomment the following line if you have an onDeleteClick listener
                    // deleteClickListener.onDeleteClick(recipe);
                }
            });
        }

        /**
         * Binds the data of a RecipeList to the views in the ViewHolder.
         *
         * @param recipe The RecipeList object representing a saved recipe.
         */
        public void bind(RecipeList recipe) {
            textViewTitle.setText(recipe.getTitle());
        }
    }

    /**
     * The OnRecipeClickListener interface defines a listener for handling recipe click events.
     */
    public interface OnRecipeClickListener {
        void onRecipeClick(RecipeList recipe);
    }

    /**
     * The OnDeleteClickListener interface defines a listener for handling delete click events.
     * Uncomment this interface if you have an onDeleteClick listener in the adapter.
     */
    public interface OnDeleteClickListener {
        void onDeleteClick(RecipeList recipe);
    }
}
