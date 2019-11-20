package com.example.android.popularmoviesstage1;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MovieDao {
    // The fact that we can request objects back, makes Room 'object relational mapping - ORL' library
    @Query("SELECT * FROM movie")
    LiveData<Movie[]> loadAllMovies();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM movie WHERE movieId = :movieId")
    LiveData<Movie> loadMovieById(int movieId);
}