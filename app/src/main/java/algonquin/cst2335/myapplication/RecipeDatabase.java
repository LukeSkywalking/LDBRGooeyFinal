package algonquin.cst2335.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RecipeList.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    private static volatile RecipeDatabase instance;

    public abstract RecipeDAO recipeDao();



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