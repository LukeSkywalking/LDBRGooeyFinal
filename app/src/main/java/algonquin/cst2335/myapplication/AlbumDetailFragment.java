package algonquin.cst2335.myapplication;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.IOException;
import java.util.List;

import algonquin.cst2335.myapplication.databinding.AlbumlistLayoutBinding;
import algonquin.cst2335.myapplication.databinding.SongBinding;

import android.app.Activity;
import android.graphics.BitmapFactory;

import android.widget.Toast;
import android.widget.Toolbar;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AlbumDetailFragment extends Fragment {

    AlbumlistLayoutBinding albumlistLayoutBinding;
    private RecyclerView recyclerView;
    private SongsAdapter songsAdapter;
    private List<Songs> songsList;
    private RequestQueue queue;
    private MediaPlayer mediaPlayer;

    SongsViewModel songModel;
    DeezerAlbum album;

    // Pass DeezerAlbum object to the fragment
    public AlbumDetailFragment(List<Songs> songsList, DeezerAlbum album, RequestQueue queue) {
        this.songsList = songsList;
        this.album = album;
        this.queue = queue;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AlbumlistLayoutBinding albumlistLayoutBinding =
                AlbumlistLayoutBinding.inflate(getLayoutInflater(), container, false);


        songModel = new ViewModelProvider(this).get(SongsViewModel.class);
        albumlistLayoutBinding.albumNameF.setText(album.getTitle());
        albumlistLayoutBinding.artistNameF.setText(album.getArtistName());

        String pathname = getActivity().getFilesDir() + "/" + album.getCoverUrl();
        File file = new File(pathname);

        if (file.exists()) {
            // Load album cover image from local storage
            Bitmap albumCover = BitmapFactory.decodeFile(pathname);
            albumlistLayoutBinding.albumCoverF.setImageBitmap(albumCover);
            albumlistLayoutBinding.albumCoverF.setVisibility(View.VISIBLE);
        } else {
            // If the album cover is not in local storage, make a network request to fetch it
            ImageRequest imgReq = new ImageRequest(album.getCoverUrl(), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    albumlistLayoutBinding.albumCoverF.setImageBitmap(bitmap);
                    albumlistLayoutBinding.albumCoverF.setVisibility(View.VISIBLE);
                    try {
                        // Save the album cover to local storage
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                                getActivity().openFileOutput(album.getArtistName() + ".png", Activity.MODE_PRIVATE));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1024, 1024, ImageView.ScaleType.CENTER, null, error -> {
                // Handle error in fetching the image
                error.printStackTrace();
            });
            queue.add(imgReq);

        }
        setHasOptionsMenu(true);
        recyclerView = albumlistLayoutBinding.albumsSongsL;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        songsAdapter = new SongsAdapter(songsList);
        recyclerView.setAdapter(songsAdapter);


        return albumlistLayoutBinding.getRoot();
    }


    // Inner class for SongsViewHolder
    public class SongsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView artistTextView;
        TextView durationTextView;
        androidx.appcompat.widget.Toolbar songmenu;


        SongsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.songName);
            artistTextView = itemView.findViewById(R.id.artistName);
            durationTextView = itemView.findViewById(R.id.duration);
            songmenu = itemView.findViewById(R.id.songTools);
        }
    }


    // Inner class for SongsAdapter
    class SongsAdapter extends RecyclerView.Adapter<SongsViewHolder> {

        private List<Songs> songsList;

        SongsAdapter(List<Songs> songsList) {
            this.songsList = songsList;
        }


        @NonNull
        @Override
        public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            SongBinding songBinding = SongBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

            return new SongsViewHolder(songBinding.getRoot());
        }

        @Override
        public void onBindViewHolder(@NonNull SongsViewHolder holder, int position) {


            Songs song = songsList.get(position);
            holder.titleTextView.setText(song.getTitle());
            holder.artistTextView.setText(song.getArtistName());
            holder.durationTextView.setText(formatDuration(song.getDuration()));

            androidx.appcompat.widget.Toolbar toolbar = holder.songmenu; // Assuming you have a toolbar in your layout
            toolbar.inflateMenu(R.menu.songmenu); // Replace with your menu resource

            toolbar.setOnMenuItemClickListener(item -> {
                SongsDatabase songsDatabase = Room.databaseBuilder(requireContext(),SongsDatabase.class, "deezerDB").build();
                DeezerDao dDao = songsDatabase.deezerDao();

                switch (item.getItemId()) {
                    case R.id.addToPlaylist:
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setMessage("Do you want to add this song to your playlist?")
                                .setTitle("Add")
                                .setNegativeButton("No", (dialog, which) -> {
                                    // if "No" is clicked
                                })
                                .setPositiveButton("Yes", (dialog, which) -> {
                                    // if "Yes" is clicked
                                    Songs songToAdd = songModel.selectedSongs.getValue();
                                    if (songToAdd != null) {
                                        Executor thread1 = Executors.newSingleThreadExecutor();
                                        thread1.execute(() -> {
                                            // delete from the database
                                            dDao.insertSong(songToAdd);
                                        });

                                        songsList.add(songToAdd); // remove from the array list
                                        songsAdapter.notifyItemChanged(position); // notify the adapter of the removal

                                        Snackbar.make(requireView(), "Song added to playlist", Snackbar.LENGTH_LONG)
                                                .setAction("Undo", (btn) -> {
                                                    Executor thread2 = Executors.newSingleThreadExecutor();
                                                    thread2.execute(() -> {
                                                        // undo the addition from the database
                                                        dDao.deleteSongFromPlayList(songToAdd);
                                                    });

                                                    songsList.remove(songToAdd);
                                                    songsAdapter.notifyItemChanged(position);
                                                })
                                                .show();
                                    }
                                });
                        builder.create().show();
                        break;
                        case R.id.songPreview:
                        try {
                            // Construct the URL for the Deezer API to get tracks of the selected album
                            String tracksURL = "https://api.deezer.com/album/" + album.getAlbumId() + "/tracks";

                            // Make a GET request to the Deezer API to get tracks of the selected album
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, tracksURL, null,
                                    (response) -> {
                                        try {
                                            // Get the array of songs from the response
                                            JSONArray songsArray = response.getJSONArray("data");

                                            // Iterate through the songsArray to find the matching song ID
                                            for (int i = 0; i < songsArray.length(); i++) {
                                                JSONObject currentSong = songsArray.getJSONObject(i);

                                                // Check if the current song has the same ID as the selected song
                                                int currentSongId = currentSong.getInt("id");
                                                if (currentSongId == song.getId()) { // Replace selectedSongId with the actual ID of the selected song
                                                    // Get the preview URL of the matched song
                                                    String previewUrl = currentSong.getString("preview");

                                                    // Play the preview
                                                    playPreview(previewUrl);

                                                    // Exit the loop after finding the match
                                                    return;
                                                }
                                            }
                                        // If no match is found, show a Snackbar
                                            Snackbar.make(requireView(), "No preview available for this song", Snackbar.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    },
                                    error -> {
                                        // Handle error in fetching tracks
                                        error.printStackTrace();
                                    });
                            queue.add(request); // Add the tracks request
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }

                return false;
            });

        }

        private void playPreview(String previewUrl) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = new MediaPlayer();

            try {
                mediaPlayer.setDataSource(previewUrl);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
                Snackbar.make(requireView(), "Error playing preview", Snackbar.LENGTH_SHORT).show();
            }
        }


            @Override
            public int getItemCount () {
                return songsList.size();
            }

            private String formatDuration ( int duration){
                // You can implement a custom format for the duration if needed
                int minutes = duration / 60;
                int seconds = duration % 60;
                return String.format("%02d:%02d", minutes, seconds);
            }
        }
}
