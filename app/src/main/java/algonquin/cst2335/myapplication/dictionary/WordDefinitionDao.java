package algonquin.cst2335.myapplication.dictionary;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
/**
 * WordDefinitionDao is the Data Access Object (DAO) interface used for defining database operations related to WordDefinitionEntity.
 * It contains methods annotated with Room annotations for inserting, querying, and deleting word definitions.
 */
@Dao
public interface WordDefinitionDao {
    /**
     * Inserts a new word definition into the database.
     *
     * @param wordDefinition The WordDefinitionEntity object to be inserted
     * @return The row ID of the inserted entry
     */
    @Insert
    long insertWordDefinition(WordDefinitionEntity wordDefinition);
    /**
     * Retrieves all word definitions from the database.
     *
     * @return A list of WordDefinitionEntity objects representing all word definitions
     */
    @Query("SELECT * FROM word_definitions")
    List<WordDefinitionEntity> getAllWordDefinitions();
    /**
     * Retrieves all words from the database.
     *
     * @return A list of WordDefinitionEntity objects representing all words
     */
    @Query("SELECT * FROM word_definitions")
    List<WordDefinitionEntity> getAllWords();
    /**
     * Deletes a specific word definition from the database.
     *
     * @param word The WordDefinitionEntity object to be deleted
     */
    @Delete
    void deleteWordDefinition(WordDefinitionEntity word);
    /**
     * Retrieves specific word definitions based on the provided word.
     *
     * @param word The word for which definitions are to be retrieved
     * @return A list of WordDefinitionEntity objects representing the definitions of the given word
     */
    @Query("SELECT * FROM word_definitions WHERE word = :word")
    List<WordDefinitionEntity> getSpecificWords(String word);
}

