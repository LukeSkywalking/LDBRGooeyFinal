package algonquin.cst2335.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Room Database class for managing RecipeList entities.
 */
@Database(entities = {RecipeList.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    private static volatile RecipeDatabase instance;

    /**
     * Abstract method to provide access to the RecipeDAO.
     *
     * @return RecipeDAO instance.
     */
    public abstract RecipeDAO recipeDao();

    /**
     * Gets the singleton instance of RecipeDatabase.
     *
     * @param context The application context.
     * @return The RecipeDatabase instance.
     */
    public static RecipeDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (RecipeDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RecipeDatabase.class,
                            "recipe_database"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return instance;
    }
}
