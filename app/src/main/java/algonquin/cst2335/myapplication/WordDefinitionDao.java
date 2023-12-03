package algonquin.cst2335.myapplication;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDefinitionDao {

    @Insert
    long insertWordDefinition(WordDefinitionEntity wordDefinition);

    @Query("SELECT * FROM word_definitions")
    List<WordDefinitionEntity> getAllWordDefinitions();

    @Query("SELECT * FROM word_definitions")
    List<WordDefinitionEntity> getAllWords();

    @Delete
    void deleteWordDefinition(WordDefinitionEntity wordDefinition);
}

