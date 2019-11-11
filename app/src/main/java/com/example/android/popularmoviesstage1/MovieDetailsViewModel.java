package com.example.android.popularmoviesstage1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

// Make this class extend ViewModel (not AndroidViewModel bcs we use factory - bcs we need movieId)
public class MovieDetailsViewModel extends ViewModel{

    // Add a movie variable for the Movie object wrapped in a LiveData
    private LiveData<Movie> movie;

    // Create a constructor where you call loadMovieById of the movieDao to initialize the movies variable
    public MovieDetailsViewModel(AppDatabase database, int movieId) {
        movie = database.movieDao().loadMovieById(movieId);
    }

    // Create a getter for the movie variable
    public LiveData<Movie> getMovie() {
        return movie;
    }
}