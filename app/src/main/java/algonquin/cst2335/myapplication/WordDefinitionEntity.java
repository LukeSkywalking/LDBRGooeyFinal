package algonquin.cst2335.myapplication;
import androidx.annotation.ColorLong;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word_definitions")
public class WordDefinitionEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String word;
    private String definitions;

    public WordDefinitionEntity(String word, String definitions) {
        this.word = word;
        this.definitions = definitions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public String getDefinitions() {
        return definitions;
    }
}
