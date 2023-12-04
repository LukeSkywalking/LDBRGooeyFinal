package algonquin.cst2335.myapplication.deezer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.myapplication.R;
import algonquin.cst2335.myapplication.databinding.AlbumlistLayoutBinding;
import algonquin.cst2335.myapplication.databinding.SongBinding;

public class AlbumDetailFragment extends Fragment {

    private AlbumlistLayoutBinding albumlistLayoutBinding;
    private RecyclerView recyclerView;
    private SongsAdapter songsAdapter;
    private List<Songs> songsList;
    private List<Songs> favsList;  // Added
    private RequestQueue queue;
    private MediaPlayer mediaPlayer;

    private SongsDatabase songsDatabase;
    private SongsViewModel songModel;
    private DeezerAlbum album;

    public AlbumDetailFragment(List<Songs> songsList, DeezerAlbum album, RequestQueue queue) {
        this.songsList = songsList;
        this.album = album;
        this.queue = queue;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        albumlistLayoutBinding = AlbumlistLayoutBinding.inflate(inflater, container, false);
        songModel = new ViewModelProvider(this).get(SongsViewModel.class);

        albumlistLayoutBinding.albumNameF.setText(album.getTitle());
        albumlistLayoutBinding.artistNameF.setText(album.getArtistName());


        favsList = new ArrayList<>();

        String pathname = getActivity().getFilesDir() + "/" + album.getCoverUrl();
        File file = new File(pathname);

        if (file.exists()) {

            Bitmap albumCover = BitmapFactory.decodeFile(pathname);
            albumlistLayoutBinding.albumCoverF.setImageBitmap(albumCover);
            albumlistLayoutBinding.albumCoverF.setVisibility(View.VISIBLE);
        } else {

            ImageRequest imgReq = new ImageRequest(album.getCoverUrl(), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    albumlistLayoutBinding.albumCoverF.setImageBitmap(bitmap);
                    albumlistLayoutBinding.albumCoverF.setVisibility(View.VISIBLE);
                    try {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                                getActivity().openFileOutput(album.getArtistName() + ".png", getActivity().MODE_PRIVATE));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1024, 1024, ImageView.ScaleType.CENTER, null, error -> {

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
            SongsDatabase songsDatabase = Room.databaseBuilder(requireContext(),SongsDatabase.class, "deezerDB").build();
            DeezerDao dDao = songsDatabase.deezerDao();

            Songs song = songsList.get(position);
            holder.titleTextView.setText(song.getTitle());
            holder.artistTextView.setText(song.getArtistName());
            holder.durationTextView.setText(formatDuration(song.getDuration()));

            androidx.appcompat.widget.Toolbar toolbar = holder.songmenu;
            toolbar.inflateMenu(R.menu.songmenu);

            toolbar.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.addToPlaylist:
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setMessage("Do you want to add this song to your playlist?")
                                .setTitle("Add")
                                .setNegativeButton("No", (dialog, which) -> {
                                })
                                .setPositiveButton("Yes", (dialog, which) -> {

                                    Songs songToAdd = songsList.get(position);
                                    favsList.add(songToAdd);

                                    if (songToAdd != null) {
                                        songsAdapter.notifyItemChanged(position);
                                        Executor thread1 = Executors.newSingleThreadExecutor();
                                        thread1.execute(() -> {
                                            try {
                                                // Ensure that the insertSong method is correctly implemented
                                                long result = dDao.insertSong(songToAdd);
                                                Log.d("InsertResult", "Rows affected: " + result);
                                            } catch (Exception e) {
                                                Log.e("InsertError", "Error inserting song", e);
                                            }
                                        });

                                        // Log to check if the song is being added to the list
                                        Log.d("SongToAdd", "Song to add: " + songToAdd.toString());

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
                            String tracksURL = "https://api.deezer.com/album/" + album.getAlbumId() + "/tracks";

                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, tracksURL, null,
                                    (response) -> {
                                        try {
                                            JSONArray songsArray = response.getJSONArray("data");

                                            for (int i = 0; i < songsArray.length(); i++) {
                                                JSONObject currentSong = songsArray.getJSONObject(i);

                                                int currentSongId = currentSong.getInt("id");
                                                if (currentSongId == song.getId()) {
                                                    String previewUrl = currentSong.getString("preview");

                                                    playPreview(previewUrl);

                                                    return;
                                                }
                                            }
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
        public int getItemCount() {
            return songsList.size();
        }

        private String formatDuration(int duration) {
            int minutes = duration / 60;
            int seconds = duration % 60;
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
}
