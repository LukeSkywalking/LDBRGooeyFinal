package algonquin.cst2335.myapplication;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;

@Entity
public class Songs {


@PrimaryKey
@ColumnInfo(name = "SongID")
    /**
     * the id in db
     */
    private long id;

    /**
     * the title of the song
     */
    @ColumnInfo(name = "title")
    private String title;

    /**
     * the duration of the song in seconds
     */
    @ColumnInfo(name = "duration")
    private int duration; // in second

    /**
     * the album name
     */
    @ColumnInfo(name = "albumName")
    private String albumName;

    /**
     * the album cover image url;
     */
    @ColumnInfo(name = "albumCover")
    private String albumCover;

    @ColumnInfo(name = "artistName")
    private String artistName;


    public Songs(long id, String title, int duration, String albumName, String albumCover, String artistName) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.albumName = albumName;
        this.albumCover = albumCover;
        this.artistName = artistName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDurationInMMSS() {
        int minute = duration / 60;
        int second = duration % 60;
        return String.format(Locale.getDefault(), "%d:%02d", minute, second);
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }


}