package algonquin.cst2335.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import algonquin.cst2335.myapplication.databinding.ActivityMainBinding;
/**
 * MainActivity serves as the entry point for the application, displaying the main interface.
 * It extends AppCompatActivity and sets up the initial user interface components.
 */
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    /**
     * Initializes the activity layout and sets up the onClick listener for the dictionary button.
     *
     * @param savedInstanceState The saved state of the activity (if available)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.dictionaryButton.setOnClickListener(click ->{
            startActivity(new Intent(this,DictionaryActivity.class));
        });


    }
}