package algonquin.cst2335.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SongsDatabase extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "SongDataBase";

    protected final static int VERSION_NUM = 1;

    public SongsDatabase (@Nullable Context context){
        super(context,DATABASE_NAME,null,VERSION_NUM);

    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " %s text," +
                        " %s integer, " +
                        " %s text," +
                        " %s text); ", Songs.TABLE_NAME_FAVORITE, Songs.COL_ID, Songs.COL_TITLE,
                Songs.COL_DURATION, Songs.COL_ALBUM_NAME, Songs.COL_ALBUM_COVER));

        db.execSQL(String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " %s text," +
                        " %s integer, " +
                        " %s text," +
                        " %s text); ", Songs.TABLE_NAME_SEARCH_RESULT, Songs.COL_ID, Songs.COL_TITLE,
                Songs.COL_DURATION, Songs.COL_ALBUM_NAME, Songs.COL_ALBUM_COVER));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + Songs.TABLE_NAME_SEARCH_RESULT);
        db.execSQL( "DROP TABLE IF EXISTS " + Songs.TABLE_NAME_FAVORITE);

        //Create the new table:
        onCreate(db);
    }

}
