package algonquin.cst2335.myapplication;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "words")
public class word {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "term")
    private String term;

    public word(String term) {
        this.term = term;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }
}

