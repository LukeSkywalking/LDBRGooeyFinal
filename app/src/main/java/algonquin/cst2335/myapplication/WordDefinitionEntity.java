package algonquin.cst2335.myapplication;
import androidx.annotation.ColorLong;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * WordDefinitionEntity represents an entity class mapped to the 'word_definitions' table in the database.
 * It contains fields annotated with Room annotations to define the structure of the table.
 */
@Entity(tableName = "word_definitions")
public class WordDefinitionEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String word;
    private String definitions;
    /**
     * Constructs a WordDefinitionEntity with a word and its definitions.
     *
     * @param word        The word for which definitions are stored
     * @param definitions The definitions associated with the word
     */
    public WordDefinitionEntity(String word, String definitions) {
        this.word = word;
        this.definitions = definitions;
    }
    /**
     * Retrieves the ID of the word definition.
     *
     * @return The ID of the word definition
     */
    public int getId() {
        return id;
    }
    /**
     * Sets the ID of the word definition.
     *
     * @param id The ID to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Retrieves the word.
     *
     * @return The word
     */
    public String getWord() {
        return word;
    }
    /**
     * Retrieves the definitions associated with the word.
     *
     * @return The definitions of the word
     */
    public String getDefinitions() {
        return definitions;
    }
}
