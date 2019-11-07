package com.example.android.popularmoviesstage1;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface MovieDao {
    // The fact that we can request objects back, makes Room 'object relational mapping - ORL' library
    @Query("SELECT * FROM movie")
    Movie[] loadAllMovies();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);
}