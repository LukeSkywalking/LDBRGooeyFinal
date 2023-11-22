package algonquin.cst2335.myapplication;

public class DeezerAlbum {
    private long albumId;
    private String title;
    private String artistName;
    private String coverUrl;

    public DeezerAlbum(long albumId, String title, String artistName, String coverUrl) {
        this.albumId = albumId;
        this.title = title;
        this.artistName = artistName;
        this.coverUrl = coverUrl;
    }

    public long getAlbumId() {
        return albumId;
    }

    public String getTitle() {
        return title;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getCoverUrl() {
        return coverUrl;
    }
}
