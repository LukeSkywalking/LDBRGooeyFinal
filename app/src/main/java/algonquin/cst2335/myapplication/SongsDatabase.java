package algonquin.cst2335.myapplication;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Songs.class},version = 1)
public abstract class SongsDatabase extends RoomDatabase {
    public abstract DeezerDao deezerDao();
}
