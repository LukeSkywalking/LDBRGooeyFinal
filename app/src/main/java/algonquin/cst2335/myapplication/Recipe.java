package algonquin.cst2335.myapplication;

/**
 * Represents a Recipe with details such as id, title, image, summary, and source URL.
 */
public class Recipe {

    private String id;
    private String title;
    private String image;
    private String summary;
    private String sourceUrl;

    /**
     * Constructor for creating a Recipe instance with all fields.
     *
     * @param id        The unique identifier of the recipe.
     * @param title     The title of the recipe.
     * @param image     The URL of the recipe image.
     * @param summary   A brief summary of the recipe.
     * @param sourceUrl The source URL of the recipe.
     */
    public Recipe(String id, String title, String image, String summary, String sourceUrl) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.summary = summary;
        this.sourceUrl = sourceUrl;
    }

    /**
     * Constructor for creating a Recipe instance with minimal details.
     *
     * @param id    The unique identifier of the recipe.
     * @param title The title of the recipe.
     * @param image The URL of the recipe image.
     */
    public Recipe(long id, String title, String image) {
        // Additional implementation for long id if needed in the future.
        this.id = String.valueOf(id);
        this.title = title;
        this.image = image;
    }

    // Getters and setters for each field (id, title, image, summary, sourceUrl)

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
