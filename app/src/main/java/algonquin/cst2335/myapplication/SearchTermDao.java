package algonquin.cst2335.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SearchTermDao {

    @Insert
    void insert(word word);

    @Delete
    void delete(word word);

//    @Query("SELECT * FROM word")
//    LiveData<List<word>> getAllSearchTerms();
//
//    // Query to get definitions for a specific term
//    @Query("SELECT * FROM word WHERE term = :searchTerm")
//    LiveData<List<word>> getDefinitionsForWord(String searchTerm);

    // Other necessary queries
}
