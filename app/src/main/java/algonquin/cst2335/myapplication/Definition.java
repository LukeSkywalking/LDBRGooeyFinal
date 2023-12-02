package algonquin.cst2335.myapplication;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "definitions")
public class Definition {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String meaning;

    @ColumnInfo(name = "word_id")
    private int wordId;

public Definition(String meaning, int wordId){
    this.meaning = meaning;
    this.wordId = wordId;

}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }
}
