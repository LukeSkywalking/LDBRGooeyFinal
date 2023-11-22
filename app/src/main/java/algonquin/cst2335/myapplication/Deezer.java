package algonquin.cst2335.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.myapplication.databinding.AlbumBinding;
import algonquin.cst2335.myapplication.databinding.DeezerBinding;
import algonquin.cst2335.myapplication.databinding.SongBinding;

public class Deezer extends AppCompatActivity {

    public static final String SEARCH_TEXT = "SEARCH_TEXT";
    private RecyclerView.Adapter myAdapter;

    RequestQueue queue = null;
    ArrayList<Songs> songsList = new ArrayList<>();

    ArrayList<DeezerAlbum> albumsList = new ArrayList<>();

    AlbumsViewModel albumModel;

    SharedPreferences sp;
    Songs song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeezerBinding binding = DeezerBinding.inflate(getLayoutInflater());
        queue = Volley.newRequestQueue(this);
        setContentView(binding.getRoot());

        setTitle(R.string.app_name);

        androidx.appcompat.widget.Toolbar toolBar = (binding.toolbar);
        setSupportActionBar(toolBar);


        binding.searchButton.setOnClickListener(click -> {
            startActivity(new Intent(this, Deezer.class));
        });

        binding.playlistPageButton.setOnClickListener(click -> {
            startActivity(new Intent(this, playlist.class));
        });

        sp = getSharedPreferences("DeezerSong", MODE_PRIVATE);
        String searchText = sp.getString(SEARCH_TEXT, "");


        binding.searchButton.setOnClickListener(c -> {
            AlbumBinding ab;

            String searchedText = binding.searchText.getText().toString().trim();

            String stringURL = null;

            try {
                stringURL = "https://api.deezer.com/search/artist/?q=" +
                        URLEncoder.encode(searchedText, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            RequestQueue queue = null;
            queue = Volley.newRequestQueue(this);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {
                        try {
                            if (response.has("data")) {
                                JSONArray albumArray = response.getJSONArray("data");

                                for (int i = 0; i < albumArray.length(); i++) {
                                    JSONObject album = albumArray.getJSONObject(i);

                                    long albumId = album.getLong("id");
                                    String albumName = album.getString("title");
                                    String albumCoverUrl = album.getString("cover_medium");

                                    JSONObject artist = album.getJSONObject("artist");
                                    String artistName = artist.getString("name");
                                    DeezerAlbum deezerAlbum = new DeezerAlbum(albumId,albumName,artistName,albumCoverUrl);
                                    albumsList.add(deezerAlbum);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, (error) -> {
                if (error.networkResponse != null) {
                    int j = 0;
                }
            });
            queue.add(request);
        });


//        binding.deezerAlbums.setLayoutManager(new LinearLayoutManager(this));
//        albumModel = new ViewModelProvider(this).get(AlbumsViewModel.class);
//        albumsList = albumModel.deezerAlbum.getValue();

// Add the request to the request queue

        class MyAlbumHolder extends RecyclerView.ViewHolder {
            TextView albumName;
            TextView artistName;
            ImageView imageView;
            Toolbar albumMenu;

            public MyAlbumHolder(@NonNull View itemView) {
                super(itemView);

                albumName = itemView.findViewById(R.id.albumName);
                artistName = itemView.findViewById(R.id.artistName);
                imageView = itemView.findViewById(R.id.imageView);
            }

        }



        binding.deezerAlbums.setAdapter(myAdapter = new RecyclerView.Adapter<MyAlbumHolder>() {

            @NonNull
            @Override
            public MyAlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                AlbumBinding binding = AlbumBinding.inflate(getLayoutInflater(),parent,false);
                return new MyAlbumHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyAlbumHolder holder, int position) {
                DeezerAlbum deezerAlbum = albumsList.get(position);
                holder.albumName.setText(deezerAlbum.getTitle());
                holder.artistName.setText(deezerAlbum.getArtistName());
                //need one for the album picture
            }

            @Override
            public int getItemCount() {
                return albumsList.size();
            }
        });

//            class MySongHolder extends RecyclerView.ViewHolder {
//                TextView songName;
//                TextView artistName;
//                ImageView imageView;
//
//                Toolbar songMenu;
//
//                public MySongHolder(@NonNull View itemView) {
//                    super(itemView);
//
//
//                    songName = itemView.findViewById(R.id.songName);
//                    artistName = itemView.findViewById(R.id.artistName);
//                    imageView = itemView.findViewById(R.id.imageView);
//                    songMenu = itemView.findViewById(R.id.toolbar);
//
//                }
//            }
//
//
//            binding.deezerSongs.setAdapter(new RecyclerView.Adapter<MySongHolder>() {
//                @NonNull
//                @Override
//                // responsible for creating a layout for a row and setting the Text views in code.
//                public MySongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                    SongBinding binding = SongBinding.inflate(getLayoutInflater());
//                    return new MySongHolder(binding.getRoot());
//                }
//
//                @Override
//                public void onBindViewHolder(@NonNull MySongHolder holder, int position) {
////                Songs song = songsList.get(position);
////                holder.songName.setText(song.songTitle);
////                holder.artistName.setText(song.artistName);
//
//                }
//
//                @Override
//                public int getItemCount() {
//                    return songsList.size();
//                }
//
//
//            });

        }
    }



