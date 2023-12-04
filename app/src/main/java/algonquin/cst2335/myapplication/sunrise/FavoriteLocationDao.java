package algonquin.cst2335.myapplication.sunrise;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Dao;
import androidx.room.Update;

import java.util.List;
@Dao
public interface FavoriteLocationDao {
    @Insert
    void insert(FavoriteLocation favoriteLocation);

    @Query("SELECT * FROM favorite_locations")
    List<FavoriteLocation> getAllFavoriteLocations();

    @Update
    void update(FavoriteLocation favoriteLocation);

    @Delete
    void delete(FavoriteLocation favoriteLocation);
}
