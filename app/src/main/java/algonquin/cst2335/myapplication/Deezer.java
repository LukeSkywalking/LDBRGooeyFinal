package algonquin.cst2335.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.myapplication.databinding.AlbumBinding;
import algonquin.cst2335.myapplication.databinding.DeezerBinding;
import algonquin.cst2335.myapplication.databinding.SongBinding;

public class Deezer extends AppCompatActivity {
    private RecyclerView.Adapter myAdapter;

    RequestQueue queue = null;
    ArrayList<Songs> songsList = new ArrayList<>();

    ArrayList<DeezerAlbum> albumsList = new ArrayList<>();

    AlbumsViewModel albumModel;

    DeezerBinding binding;

    SharedPreferences sp;
    AlbumBinding ab;
    Songs song;


    protected Bitmap albumCover;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DeezerBinding.inflate(getLayoutInflater());
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
            Intent intent = new Intent(this, playlist.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);});

        binding.deezerAlbums.setLayoutManager(new LinearLayoutManager(this));
        albumModel = new ViewModelProvider(this).get(AlbumsViewModel.class);

        binding.searchButton.setOnClickListener(c -> {
            String searchedText = binding.searchText.getText().toString().trim();

            String stringURL = null;
            try {
                stringURL = "https://api.deezer.com/search/album/?q=" +
                        URLEncoder.encode(searchedText, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            // Make a GET request to the Deezer API
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {
                        try {
                            // Get the array of albums from the response
                            JSONArray albumArray = response.getJSONArray("data");

                            // Clear the existing albumsList before adding new data
                            albumsList.clear();

                            for (int i = 0; i < albumArray.length(); i++) {
                                JSONObject album0 = albumArray.getJSONObject(i);
                                long albumId = album0.getLong("id");
                                String albumName = album0.getString("title");
                                String albumCoverUrl = album0.getString("cover_xl");
                                // Get the artist information
                                JSONObject artist = album0.getJSONObject("artist");
                                String artistName = artist.getString("name");

                                DeezerAlbum album = new DeezerAlbum(albumId, albumName, artistName, albumCoverUrl);
                                albumsList.add(album);
                            }
                            // Notify the adapter that the data set has changed
                            myAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    ,
                    error -> {
                        // Handle error
                        error.printStackTrace();
                    });
            queue.add(request);
        });

            albumModel.selectedAlbums.observe(this, album -> {
                if (album != null) {
                    try {
                        // Construct the URL for the Deezer API to get tracks of the selected album
                        String tracksURL = "https://api.deezer.com/album/" + album.getAlbumId() + "/tracks";

                        // Make a GET request to the Deezer API to get tracks of the selected album
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, tracksURL, null,
                                (response) -> {
                                    try {
                                        // Get the array of albums from the response
                                        JSONArray songsArray = response.getJSONArray("data");
                                        // Clear the existing songsList before adding new data
                                        songsList.clear();

                                        for (int i = 0; i < songsArray.length(); i++) {
                                            JSONObject trackObject = songsArray.getJSONObject(i);
                                            long trackId = trackObject.getLong("id");
                                            String trackTitle = trackObject.getString("title");
                                            String artistName = trackObject.getJSONObject("artist").getString("name");
                                            int duration = trackObject.getInt("duration");

                                            Songs track = new Songs(trackId, trackTitle, duration, album.getTitle(), album.getCoverUrl(), artistName);
                                            songsList.add(track);
                                        }
                                        // Notify the adapter that the data set has changed
                                        myAdapter.notifyDataSetChanged();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    AlbumDetailFragment albumDetailFragment = new AlbumDetailFragment(songsList, album,queue);
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                                    transaction.addToBackStack("");
                                    transaction.replace(R.id.albumFragment, albumDetailFragment);
                                    transaction.commit();
                                },
                                error -> {
                                    // Handle error in fetching tracks
                                    error.printStackTrace();
                                });
                        queue.add(request); // Add the tracks request


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });



        binding.deezerAlbums.setAdapter(myAdapter = new RecyclerView.Adapter<MyAlbumHolder>() {
            @NonNull
            @Override
            public MyAlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                AlbumBinding ab = AlbumBinding.inflate(getLayoutInflater(), parent, false);
                return new MyAlbumHolder(ab.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyAlbumHolder holder, int position) {
                DeezerAlbum deezerAlbum = albumsList.get(position);
                holder.bind(deezerAlbum);
            }

            @Override
            public int getItemCount() {
                return albumsList.size();
            }
        });

    }

    class MyAlbumHolder extends RecyclerView.ViewHolder {
        TextView albumName;
        TextView artistName;
        ImageView imageView;
        Toolbar albumMenu;


        public MyAlbumHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(c -> {
                int position = getAbsoluteAdapterPosition();
                DeezerAlbum selected = albumsList.get(position);
                albumModel.selectedAlbums.postValue(selected);
            });

            albumName = itemView.findViewById(R.id.albumName);
            artistName = itemView.findViewById(R.id.artistName);
            imageView = itemView.findViewById(R.id.albumCover);
//            albumMenu = itemView.findViewById(R.id.albumBar);

//            albumMenu.setOnClickListener(v -> {
//                // Implement your toolbar-like functionality here
//                // For example, you might show a popup menu:
//                showAlbumOptionsMenu(v, getAbsoluteAdapterPosition());
//            });

        }


        public void bind(DeezerAlbum deezerAlbum) {
            albumName.setText(deezerAlbum.getTitle());
            artistName.setText(deezerAlbum.getArtistName());

            String pathname = getFilesDir() + "/" + deezerAlbum.getCoverUrl();
            File file = new File(pathname);

            if (file.exists()) {
                albumCover = BitmapFactory.decodeFile(pathname);
                imageView.setImageBitmap(albumCover);
                imageView.setVisibility(View.VISIBLE);
            } else {
                ImageRequest imgReq = new ImageRequest(deezerAlbum.getCoverUrl(), new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                        imageView.setVisibility(View.VISIBLE);
                        try {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                                    Deezer.this.openFileOutput(deezerAlbum.getArtistName() + ".png", Activity.MODE_PRIVATE));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 1024, 1024, ImageView.ScaleType.CENTER, null, error -> {
                });
                queue.add(imgReq);
            }


        }
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



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.albummenu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        ArrayList<DeezerAlbum> albumsListDetails = new ArrayList<>();
//
//
//        switch (item.getItemId()) {
//            // Construct the URL for the Deezer API to search for albums
//            case R.id.albumDetails:
//
//
//                break;
//
//
//        }
//        return true;
//    }


//        if(albumsList ==null)
//
//    {
//        albumModel.deezerAlbum.setValue(albumsList = new ArrayList<>());
//
//        Executor thread = Executors.newSingleThreadExecutor();
//        {
//            albumsList.addAll()
//        }
//    }

//
//    class MySongHolder extends RecyclerView.ViewHolder {
//        TextView songName;
//        TextView artistName;
//        ImageView imageView;
//
//        Toolbar songMenu;
//
//        public MySongHolder(@NonNull View itemView) {
//            super(itemView);
//
//
//            songName = itemView.findViewById(R.id.songName);
//            artistName = itemView.findViewById(R.id.artistName);
//            songMenu = itemView.findViewById(R.id.toolbar);
//
//        }
//    }


//            binding.deezerSongs.setAdapter(new RecyclerView.Adapter<MySongHolder>()
//
//    {
//        @NonNull
//        @Override
//        // responsible for creating a layout for a row and setting the Text views in code.
//        public MySongHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType){
//        SongBinding binding = SongBinding.inflate(getLayoutInflater());
//        return new MySongHolder(binding.getRoot());
//    }
//
//        @Override
//        public void onBindViewHolder (@NonNull MySongHolder holder,int position){
//                Songs song = songsList.get(position);
//                holder.songName.setText(song.songTitle);
//                holder.artistName.setText(song.artistName);
//
//    }
//
//        @Override
//        public int getItemCount () {
//        return songsList.size();
//    }
//
//
//    });
//

}





