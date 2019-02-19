package limamauricio.popularmovies2.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import limamauricio.popularmovies2.model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "movieDB";
    private static AppDatabase databaseInstance;

    public static AppDatabase getInstance(Context context){

        if (databaseInstance == null){
            synchronized (LOCK){

                Log.d(LOG_TAG,"Creating new database instace");
                databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }

        Log.d(LOG_TAG, "Getting movie database instance");
        return databaseInstance;

    }

    public abstract MovieDao movieDao();

}
