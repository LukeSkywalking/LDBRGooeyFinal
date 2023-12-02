package algonquin.cst2335.myapplication.deezer;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DeezerDao {

@Insert
    long insertSong(Songs song);

@Query("Select * from Songs")
    List<Songs> getAllSongs();

@Delete
    void deleteSongFromPlayList(Songs song);

    @Query("SELECT * FROM Songs WHERE title = :title")

    List<Songs> searchSong(String title);
}

