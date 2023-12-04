package algonquin.cst2335.myapplication.sunrise;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.myapplication.databinding.LocationdetailsfragmentBinding;

public class LocationDetailFragment extends Fragment {

    TextView timeZoneTitle;
    TextView timeZone;
    TextView dateTitle;
    TextView date;
    TextView sunriseTitle;
    TextView sunriseInfo;
    TextView sunsetTitle;
    TextView sunsetInfo;


    LocationdetailsfragmentBinding binding;
    FavoriteLocationViewModel viewModel;

    private FavoriteLocationViewModel locationViewModel;


    List<FavoriteLocation> locations;
    FavoriteLocationDao favoriteLocationDao;

    FavoriteLocationAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Set sample text (replace with actual data)
        timeZoneTitle = binding.timeZoneTitle;
        timeZone = binding.timeZone;
        dateTitle = binding.dateTitle;
        date = binding.date;
        sunriseTitle = binding.sunriseTitle;
        sunriseInfo = binding.sunriseInfo;
        sunsetTitle = binding.sunsetTitle;
        sunsetInfo = binding.sunsetInfo;

        // Retrieve latitude and longitude from arguments bundle
        String latitude = getArguments().getString("latitude", "");
        String longitude = getArguments().getString("longitude", "");

        // Perform API request using latitude and longitude
        performSunriseSunsetQuery(latitude, longitude);

        AppDatabase locDatabase = Room.databaseBuilder(getContext(), AppDatabase.class, "favorite_locations").build();
        favoriteLocationDao = locDatabase.favoriteLocationDao();

        // Fetch and display saved locations from the database
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            List<FavoriteLocation> locations = favoriteLocationDao.getAllFavoriteLocations();
            viewModel.favoriteLocations.postValue((ArrayList<FavoriteLocation>) locations);
            requireActivity().runOnUiThread(() -> {
                locations.addAll(locations);

                adapter.notifyDataSetChanged();
            });
        });


        // Initialize ViewModel
        locationViewModel = new ViewModelProvider(requireActivity()).get(FavoriteLocationViewModel.class);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = LocationdetailsfragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(FavoriteLocationViewModel.class);




        // Observe changes in favorite locations
        locationViewModel.favoriteLocations.observe(getViewLifecycleOwner(), new Observer<ArrayList<FavoriteLocation>>() {
            @Override
            public void onChanged(ArrayList<FavoriteLocation> favoriteLocations) {

            }
        });

        // Initialize TextViews
        return binding.getRoot();
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
                        String timeZTitle = results.getString("timezone");
                        String date0 = results.getString("date");


                        // Update the UI in LocationDetailFragment
                        timeZoneTitle.setText("Your Time Zone Is:"); // Replace "Your Data" with appropriate data
                        timeZone.setText(timeZTitle);
                        dateTitle.setText("Date:");
                        date.setText(date0);
                        sunriseTitle.setText("Sunrise:");
                        sunriseInfo.setText(sunriseTime);
                        sunsetTitle.setText("Sunset:");
                        sunsetInfo.setText(sunsetTime);


                    } catch (JSONException e) {
                        Log.e("LocationDetailFragment", "Error parsing JSON response: " + e.getMessage());
                        Toast.makeText(getContext(), "Error parsing server response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("LocationDetailFragment", "Error in network request: " + error.getMessage());
                    Toast.makeText(getContext(), "Error in network request", Toast.LENGTH_SHORT).show();
                });

// Add the request to the Volley queue
        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);
    }
}
