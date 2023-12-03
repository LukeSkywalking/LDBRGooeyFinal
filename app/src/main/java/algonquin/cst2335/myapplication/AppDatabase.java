package algonquin.cst2335.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {WordDefinitionEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WordDefinitionDao wordDefinitionDao();

}
