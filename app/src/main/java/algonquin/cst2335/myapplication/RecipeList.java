package algonquin.cst2335.myapplication;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class representing a saved recipe in the Room database.
 */
@Entity(tableName = "recipes")
public class RecipeList {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "summary")
    public String summary;

    @ColumnInfo(name = "sourceUrl")
    public String sourceUrl;

    /**
     * Default constructor required by Room.
     */
    public RecipeList() {
        // Default constructor required by Room
    }

    /**
     * Constructor for creating a RecipeList instance with details.
     *
     * @param id        The unique identifier of the recipe.
     * @param title     The title of the recipe.
     * @param image     The URL of the recipe image.
     * @param summary   A brief summary of the recipe.
     * @param sourceUrl The source URL of the recipe.
     */
    public RecipeList(@NonNull String id, String title, String image, String summary, String sourceUrl) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.summary = summary;
        this.sourceUrl = sourceUrl;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
