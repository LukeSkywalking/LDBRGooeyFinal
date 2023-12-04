package algonquin.cst2335.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;



public class FavoriteLocationAdapter extends RecyclerView.Adapter<FavoriteLocationViewHolder> {

    private List<FavoriteLocation> favoriteLocations;
    private LayoutInflater inflater;

    public FavoriteLocationAdapter(List<FavoriteLocation> favoriteLocations) {
        this.favoriteLocations = favoriteLocations;
    }

    @NonNull
    @Override
    public FavoriteLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sunrise_item, parent, false);
        return new FavoriteLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteLocationViewHolder holder, int position) {
        FavoriteLocation favoriteLocation = favoriteLocations.get(position);
        holder.bind(favoriteLocation);
    }

    @Override
    public int getItemCount() {
        return favoriteLocations.size();
    }
    //  method to update the data
    public void setFavoriteLocations(List<FavoriteLocation> favoriteLocations) {
        this.favoriteLocations = favoriteLocations;
        notifyDataSetChanged();
    }
}