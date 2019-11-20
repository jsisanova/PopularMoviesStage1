package com.example.android.popularmoviesstage1.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.popularmoviesstage1.model.Movie;

@Dao
public interface MovieDao {
    // The fact that we can request objects back, makes Room 'object relational mapping - ORL' library
    @Query("SELECT * FROM movie")
    LiveData<Movie[]> loadAllMovies();
    // Database is not re-queried unnecessarily. LiveData is used to observe changes in the database and update the UI accordingly.
    // Database is not re-queried unnecessarily after rotation. Cached LiveData from ViewModel is used instead.

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM movie WHERE movieId = :movieId")
    LiveData<Movie> loadMovieById(int movieId);
}