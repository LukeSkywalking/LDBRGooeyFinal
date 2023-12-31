package algonquin.cst2335.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import algonquin.cst2335.myapplication.Recipe.RecipeMain;
import algonquin.cst2335.myapplication.databinding.ActivityMainBinding;
import algonquin.cst2335.myapplication.deezer.playlist;
import algonquin.cst2335.myapplication.dictionary.DictionaryActivity;
import algonquin.cst2335.myapplication.sunrise.SunriseMain;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.dictionaryButton.setOnClickListener(click ->{
            startActivity(new Intent(this, DictionaryActivity.class));
        });

        binding.deezerButton.setOnClickListener(click ->{
            startActivity(new Intent(this, playlist.class));
        });

        binding.recipeButton.setOnClickListener(click ->
                startActivity(new Intent(this, RecipeMain.class)));

        binding.sunriseButton.setOnClickListener(click -> {
            startActivity(new Intent(this, SunriseMain.class));
        });
    }
}