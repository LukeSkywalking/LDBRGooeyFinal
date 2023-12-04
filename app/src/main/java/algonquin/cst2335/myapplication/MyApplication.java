package algonquin.cst2335.myapplication;

import android.app.Application;

import androidx.room.Room;

public class MyApplication extends Application {
    public static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the database
        database = Room.databaseBuilder(this, AppDatabase.class, "favorite-locations-db").build();
    }
}
