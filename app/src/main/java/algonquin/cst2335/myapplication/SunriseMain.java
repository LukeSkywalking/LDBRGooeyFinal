package algonquin.cst2335.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.myapplication.databinding.ActivitySunriseMainBinding;

public class SunriseMain extends AppCompatActivity {

    private EditText editTextLatitude;
    private EditText editTextLongitude;
    private Button lookupButton;
    private Button saveButton;
    private TextView textSunriseInfo;
    private TextView textSunsetInfo;
    private FavoriteLocationViewModel favoriteLocationViewModel;



    private ArrayList<FavoriteLocation> favs = new ArrayList<FavoriteLocation>();
    FavoriteLocationDao favoriteLocationDao;
    FavoriteLocationAdapter adapter;
    androidx.appcompat.widget.Toolbar toolbar;

    ActivitySunriseMainBinding binding;

    FavoriteLocation favoriteLocation = new FavoriteLocation();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunrise_main);
//        setContentView(R.layout.sunrise_item);
//        setContentView(R.layout.fragment_location_detail);

        // Initialize views

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.sunriseToolbar);

        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLongitude = findViewById(R.id.editTextLongitude);
        lookupButton = findViewById(R.id.lookupButton);
        saveButton = findViewById(R.id.saveButton);
        textSunriseInfo = findViewById(R.id.textSunriseInfo);
        textSunsetInfo = findViewById(R.id.textSunsetInfo);

        setTitle("Welcome To Sunrise and Sunsets");


        AppDatabase LocDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "favorite_locations").build();
        FavoriteLocationDao dDao = LocDatabase.favoriteLocationDao();
//        if (favs.isEmpty()) {
//            Executor thread = Executors.newSingleThreadExecutor();
//            thread.execute(() -> {
//                List<FavoriteLocation> locations = dDao.getAllFavoriteLocations();
//                Log.d("Database", "Number of locations: " + locations .size());
//                runOnUiThread(() -> {
//                    favs.addAll(locations);
//                    if (adapter == null) {
//                        adapter = new FavoriteLocationAdapter(favs);
//                        recyclerView.setAdapter(adapter);
//                    } else {
//                        adapter.notifyDataSetChanged();
//                    }
//                });
//            });
//        }

        // Set up RecyclerView

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new FavoriteLocationAdapter(favs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        // Set click listeners or any other setup as needed
        lookupButton.setOnClickListener(v -> performSunriseSunsetQuery(
                editTextLatitude.getText().toString(),
                editTextLongitude.getText().toString()
        ));

        saveButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SunriseMain.this);
            builder.setMessage("Do you want to add this location to your favorites?")
                    .setTitle("Add")
                    .setNegativeButton("No", (dialog, which) -> {
                        // Handle "No" button click
                    })
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Implement save to favorites logic
                        String latitude = editTextLatitude.getText().toString();
                        String longitude = editTextLongitude.getText().toString();
                        String sunriseTime = textSunriseInfo.getText().toString();
                        String sunsetTime = textSunsetInfo.getText().toString();

                        if (!latitude.isEmpty() && !longitude.isEmpty() && !sunriseTime.isEmpty() && !sunsetTime.isEmpty()) {
                            FavoriteLocation favoriteLocation = new FavoriteLocation(
                                    Double.parseDouble(latitude),
                                    Double.parseDouble(longitude),
                                    sunriseTime,
                                    sunsetTime
                            );

                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() -> {
                                dDao.insert(favoriteLocation);
                                favs.add(favoriteLocation);

                                // Notify the adapter about the data set change
                                runOnUiThread(() -> adapter.notifyDataSetChanged());

                                // Initialize ViewModel if not done before
                                if (favoriteLocationViewModel == null) {
                                    favoriteLocationViewModel = new ViewModelProvider(this).get(FavoriteLocationViewModel.class);
                                }

                                // Add to ViewModel
                                favoriteLocationViewModel.favoriteLocationVM.postValue(favoriteLocation);

                                // Display a toast or perform any other UI update as needed
                            });

                            // Show Snackbar for undo option
                            Snackbar.make(findViewById(android.R.id.content), "Location added to favorites list", Snackbar.LENGTH_LONG)
                                    .setAction("Undo", (btn) -> {
                                        Executor thread2 = Executors.newSingleThreadExecutor();
                                        thread2.execute(() -> {
                                            // undo the addition from the database
                                            dDao.delete(favoriteLocation);
                                        });

                                        favs.remove(favoriteLocation);

                                        // Notify the adapter about the data set change
                                        runOnUiThread(() -> adapter.notifyDataSetChanged());
                                    })
                                    .show();
                        }
                    });
            builder.create().show();
        });

        // Handle button click to navigate to LocationDetailFragment
        Button viewSavedLocationsButton = findViewById(R.id.showSavedLocationsButton);
        viewSavedLocationsButton.setOnClickListener(v -> {

        Intent intent = new Intent(this, favsPage.class);
        startActivity(intent);
//            int visibility = recyclerView.getVisibility();
//            recyclerView.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
        });


    }


    private void performSunriseSunsetQuery(String latitude, String longitude) {
        String apiUrl = "https://api.sunrisesunset.io/json?lat=" + latitude +
                "&lng=" + longitude + "&timezone=America/New_York&date=today";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, apiUrl, null,
                response -> {
                    try {
                        JSONObject results = response.getJSONObject("results");
                        String sunriseTime = results.getString("sunrise");
                        String sunsetTime = results.getString("sunset");

                        // Update the UI
                        textSunriseInfo.setText("Sunrise: " + sunriseTime);
                        textSunsetInfo.setText("Sunset: " + sunsetTime);

                        // Now that you have the sunrise and sunset times, you can proceed with other actions
                        // For example, you might want to save this information to a database or update a RecyclerView
                        // This will depend on your specific requirements
                    } catch (JSONException e) {
                        Log.e("SunriseMain", "Error parsing JSON response: " + e.getMessage());
                        Toast.makeText(SunriseMain.this, "Error parsing server response", Toast.LENGTH_SHORT).show();

                    }
                },
                error -> {
                    Log.e("SunriseMain", "Error in network request: " + error.getMessage());
                    Toast.makeText(SunriseMain.this, "Error in network request", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sunrisemain, menu);
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
}
