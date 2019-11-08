//package com.example.android.popularmoviesstage1;
//
//import android.app.Application;
//import android.arch.lifecycle.AndroidViewModel;
//import android.arch.lifecycle.LiveData;
//import android.util.Log;
//
//public class MainViewModel extends AndroidViewModel {
//    private static final String TAG = MainViewModel.class.getSimpleName();
//
//    // Add movies variable for an array of Movie objects wrapped in a LiveData
//    private LiveData<Movie[]> movies;
//
//    // In the constructor use the loadAllMovies of the movieDao to initialize the movies variable
//    public MainViewModel(Application application) {
//        super (application);
//        AppDatabase database = AppDatabase.getInstance (this.getApplication ());
//        Log.d(TAG, "Actively retrieving the movies from the DataBase");
//        movies = database.movieDao().loadAllMovies();
//    }
//
//    // Create a getter for the movies variable
//    public LiveData<Movie[]> getMovies() {
//        return movies;
//    }
//}
