package algonquin.cst2335.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
/**
 * AppDatabase is a Room Database class responsible for handling the persistence of WordDefinitionEntity objects.
 * It extends RoomDatabase, providing access to the DAO (Data Access Object) for WordDefinitionEntity operations.
 *
 * This database class is configured with specific entities and version numbers using Room annotations.
 * It provides an abstract method to retrieve the WordDefinitionDao for performing database operations.
 */
@Database(entities = {WordDefinitionEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    /**
     * Provides access to the WordDefinitionDao for database operations related to WordDefinitionEntity.
     *
     * @return An instance of WordDefinitionDao to perform CRUD (Create, Read, Update, Delete) operations on WordDefinitionEntity objects.
     */
    public abstract WordDefinitionDao wordDefinitionDao();

}
