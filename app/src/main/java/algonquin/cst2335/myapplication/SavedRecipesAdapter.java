package algonquin.cst2335.myapplication;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class SavedRecipesAdapter extends ListAdapter<RecipeList, SavedRecipesAdapter.RecipeViewHolder> {

    private final OnRecipeClickListener listener;

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

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeList recipe = getItem(position);
        holder.bind(recipe);
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewTitle;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    RecipeList recipe = getItem(position);
                    listener.onRecipeClick(recipe);
                }
            });
        }

        public void bind(RecipeList recipe) {
            textViewTitle.setText(recipe.getTitle());
        }
    }

    public interface OnRecipeClickListener {
        void onRecipeClick(RecipeList recipe);
    }
}

