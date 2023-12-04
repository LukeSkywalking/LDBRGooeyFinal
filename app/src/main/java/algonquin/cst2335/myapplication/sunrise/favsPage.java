package algonquin.cst2335.myapplication.sunrise;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.myapplication.R;
import algonquin.cst2335.myapplication.databinding.FavspageBinding;

public class favsPage extends AppCompatActivity {
    FavspageBinding binding;

    private ArrayList<FavoriteLocation> favs = new ArrayList<FavoriteLocation>();
    FavoriteLocationDao favoriteLocationDao;

    androidx.appcompat.widget.Toolbar toolbar;
    FavoriteLocationViewModel viewModel;

    FavoriteLocation location;

    Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FavspageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = binding.favsPagetoolBar;
        setSupportActionBar(toolbar);

        setTitle("Your Sunrise and Sunset Favourites");

        // Initialize ViewModel if not done before
        FavoriteLocationViewModel favoriteLocationViewModel = new ViewModelProvider(this).get(FavoriteLocationViewModel.class);

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.favsRV);

        // Initialize Room database
        AppDatabase locDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "favorite_locations").build();
        favoriteLocationDao = locDatabase.favoriteLocationDao();

        // Fetch and display saved locations from the database
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            List<FavoriteLocation> locations = favoriteLocationDao.getAllFavoriteLocations();
            runOnUiThread(() -> {
                favs.addAll(locations);
                viewModel.favoriteLocationVM.postValue((FavoriteLocation) locations);
                adapter.notifyDataSetChanged(); // Corrected this line
            });
        });

        // Pass LayoutInflater to the adapter
        adapter = new Adapter(favs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sunriseandsunsetmain, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deezer:
                // startActivity(new Intent(this, Sunrise.class));
                break;
            case R.id.dictionary:
                // startActivity(new Intent(this, dictionary.class));
                break;
            case R.id.recipe:
                // startActivity(new Intent(this, dictionaskmdkmary.class)
                break;
            case R.id.info:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Welcome to Sunrise and Sunsets to use the app just add in your latitude and longitude coordinates and our application will find the exact moment of when the sunrises and sets at your set location. you can also save your favourite locations with just a click of a button.")
                        .setTitle("Sunrise and Sunsets")
                        .setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();});
        }
        return true;
    }


    public class Adapter extends RecyclerView.Adapter<FavoriteLocationViewHolder> {
        private androidx.appcompat.widget.Toolbar toolbar;


        private List<FavoriteLocation> favoriteLocations;

        public Adapter(List<FavoriteLocation> favoriteLocations) {
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

            toolbar.inflateMenu(R.menu.delete);

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

    public class FavoriteLocationViewHolder extends RecyclerView.ViewHolder {

        private TextView locationNameTextView;
        private TextView sunriseTimeTextView;
        private TextView sunsetTimeTextView;

        public FavoriteLocationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationNameTextView = itemView.findViewById(R.id.locationName);
            sunriseTimeTextView = itemView.findViewById(R.id.sunriseTime);
            sunsetTimeTextView = itemView.findViewById(R.id.sunsetTime);
        }

        public void bind(FavoriteLocation favoriteLocation) {
            locationNameTextView.setText("Location: " + favoriteLocation.getLatitude() + ", " + favoriteLocation.getLongitude());
            sunriseTimeTextView.setText("Sunrise Time: " + favoriteLocation.getSunriseTime());
            sunsetTimeTextView.setText("Sunset Time: " + favoriteLocation.getSunsetTime());
        }
    }
    public void onItemClick(FavoriteLocation favoriteLocation) {
        // Create a bundle to pass data to LocationDetailFragment
        Bundle bundle = new Bundle();
        bundle.putString("latitude", String.valueOf(favoriteLocation.getLatitude()));
        bundle.putString("longitude", String.valueOf(favoriteLocation.getLongitude()));

        // Create a new LocationDetailFragment instance
        LocationDetailFragment fragment = new LocationDetailFragment();
        fragment.setArguments(bundle);

        // Replace the current fragment with LocationDetailFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}
