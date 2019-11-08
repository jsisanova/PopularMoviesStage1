//package com.example.android.popularmoviesstage1;
//
//import android.arch.lifecycle.ViewModel;
//import android.arch.lifecycle.ViewModelProvider;
//
//// In MovieDao loadMovieById(int id) we need id parameter, that's why we need also ViewModelFactory
//public class MovieDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
//
//    // Add two member variables. One for the database and one for the movieId that we want to update
//    private final AppDatabase mDb;
//    private final int mMovieId;
//
//    // Initialize the member variables in the constructor with the parameters received
//    public MovieDetailsViewModelFactory(AppDatabase database, int movieId) {
//        mDb = database;
//        mMovieId = movieId;
//    }
//
//    // Note: This is quite standard code that can be reused with minor modifications
//    @Override
//    public <T extends ViewModel> T create(Class<T> modelClass) {
//        return (T) new MovieDetailsViewModel(mDb, mMovieId);
//    }
//}