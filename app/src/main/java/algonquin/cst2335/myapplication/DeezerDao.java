package algonquin.cst2335.myapplication;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DeezerDao {

@Insert
    public long insertSong(Songs song);

@Query("Select * from Songs")
    List<Songs> getAllSongs();

@Delete
    void deleteSongFromPlayList(Songs song);


@Query("Select * from Songs where songTitle = :songTitle")
    void searchSongs(String songTitle);






}
