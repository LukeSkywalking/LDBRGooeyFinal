package algonquin.cst2335.myapplication;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import algonquin.cst2335.myapplication.databinding.ActivityPlaylistBinding;
import algonquin.cst2335.myapplication.databinding.AlbumBinding;
import algonquin.cst2335.myapplication.databinding.DeezerBinding;

public class playlist extends AppCompatActivity {

    private RecyclerView.Adapter myAdapter;

    RequestQueue queue = null;
    ArrayList<Songs> songsList = new ArrayList<>();
    SongsViewModel songsViewModel;

    ActivityPlaylistBinding binding;

    SharedPreferences sp;
    AlbumBinding ab;
    Songs song;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaylistBinding.inflate(getLayoutInflater());
        queue = Volley.newRequestQueue(this);
        setContentView(binding.getRoot());

        setTitle(R.string.app_name);

        androidx.appcompat.widget.Toolbar toolBar = (binding.toolbar);
        setSupportActionBar(toolBar);



        binding.searchButton.setOnClickListener(click -> {
            Intent intent = new Intent(this, Deezer.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });

        binding.playlistPageButton.setOnClickListener(click -> {
            startActivity(new Intent(this, playlist.class));
        });



        binding.favSongs.setLayoutManager(new LinearLayoutManager(this));
        songsViewModel = new ViewModelProvider(this).get(SongsViewModel.class);

        binding.searchButton.setOnClickListener(c -> {
            String searchedText = binding.searchTextPlaylist.getText().toString().trim();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.deezer_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.sunrise:
//                startActivity(new Intent(this, Sunrise.class));
                break;
            case R.id.dictionary:
//                startActivity(new Intent(this, dictionary.class));
                break;
            case R.id.recipe:
//                startActivity(new Intent(this, dictionary.class));
                break;
        }

        return true;
    }
}