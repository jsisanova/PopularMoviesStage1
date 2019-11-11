package com.example.android.popularmoviesstage1;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

// Create DB that uses entities and it's DAOs
// = (list of classes that we annotated as entities, version that we will increment by updating the DB, exportSchema is optional and we are not going to use it)
@Database(entities = {Movie.class}, version = 9, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName ();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movies";
    private static AppDatabase sInstance;

    // Singleton pattern - restricts the instantiation of a class to one "single" object instance
    public static AppDatabase getInstance(Context context) {
        // first time is sInstance null
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                // new object is created and assigned to sInstance variable
                // this method creates new DB or DB connection to already existing DB
                sInstance = Room.databaseBuilder (context.getApplicationContext (),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        /* If you don’t want to provide migrations, clear database when you upgrade the version
                        *
                        * With Room, if you change the database schema but don’t upgrade the version, your app will crash.
                        * If you upgrade the version but don’t provide any migrations your app will crash or, DB tables are dropped and your users lose data.
                        *
                        * Source: https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929 */
                        .fallbackToDestructiveMigration()
                        .build ();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract MovieDao movieDao();
}