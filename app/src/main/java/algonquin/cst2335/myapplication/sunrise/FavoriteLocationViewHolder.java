package algonquin.cst2335.myapplication.sunrise;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import algonquin.cst2335.myapplication.R;

public class FavoriteLocationViewHolder extends RecyclerView.ViewHolder {

    private TextView locationNameTextView;
    private TextView sunriseTimeTextView;
    private TextView sunsetTimeTextView;
        private androidx.appcompat.widget.Toolbar toolbar;

    public FavoriteLocationViewHolder(@NonNull View itemView) {
        super(itemView);
        locationNameTextView = itemView.findViewById(R.id.locationName);
        sunriseTimeTextView = itemView.findViewById(R.id.sunriseTime);
        sunsetTimeTextView = itemView.findViewById(R.id.sunsetTime);
        toolbar = itemView.findViewById(R.id.itemToolbar);

    }

    public void bind(FavoriteLocation favoriteLocation) {
        locationNameTextView.setText("Location: " + favoriteLocation.getLatitude() + ", " + favoriteLocation.getLongitude());
        sunriseTimeTextView.setText("Sunrise Time: " + favoriteLocation.getSunriseTime());
        sunsetTimeTextView.setText("Sunset Time: " + favoriteLocation.getSunsetTime());
    }

}