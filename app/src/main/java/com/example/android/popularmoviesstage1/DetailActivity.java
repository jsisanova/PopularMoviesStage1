package com.example.android.popularmoviesstage1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {
    private RecyclerView reviewRecyclerView;
    private RecyclerView.Adapter reviewAdapter;

    public final static String RESULTS_QUERY = "results";

    public final static String VIDEO_QUERY = "videos";
    public final static String VIDEO_TRAILER_KEY = "key";
    public final static String YOUTUBE_APP_BASE = "vnd.youtube:";
    public final static String YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=";

    public final static String REVIEW_URL_QUERY = "reviews";
    public final static String REVIEW_AUTHOR_QUERY = "author";
    public final static String REVIEW_BODY_QUERY = "content";
    public final static String REVIEW_URL = "url";

    public final static String FROM_RATINGS = " / 10";

    private Movie movie;
    private AppDatabase mDb;
    ToggleButton favoriteMoviesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView originalTitle = (TextView) findViewById (R.id.title_tv);
        ImageView poster = (ImageView) findViewById (R.id.poster_iv);
        TextView rating = (TextView) findViewById (R.id.rating_tv);
        TextView releaseDate = (TextView) findViewById (R.id.release_date_tv);
        TextView overview = (TextView) findViewById (R.id.overview_tv);

        Button trailerButton = findViewById (R.id.trailer_button);
        favoriteMoviesButton = findViewById (R.id.favorite_movies_button);

        // Set up the RecyclerView
        reviewRecyclerView = (RecyclerView) findViewById(R.id.reviews_rv);
        // Use LinearLayoutManager
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Collect the intent object and extract the movie class that’s been converted into a parcel in the second activity.
        // Once you’ve done this you can call the standard methods to get the data like movie title, poster etc..
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        movie = intent.getParcelableExtra("movie");

        originalTitle.setText(movie.getOriginalTitle());
        rating.setText ("Rating \n" + String.valueOf(movie.getVoteAverage()) + FROM_RATINGS);
        releaseDate.setText ("Release Date \n" + movie.getReleaseDate());
        overview.setText (movie.getOverview ());

        Picasso.get()
                .load(movie.getPosterPath())
//                .load("http://i.imgur.com/DvpvklR.png")
                .fit()
                .error(R.drawable.ghost)
                .placeholder(R.drawable.ghost)
                .into(poster);

        // execute TrailerAsyncTask
        // To fetch trailers youmake a request to the /movie/{id}/videos endpoint.
        new TrailerAsyncTask(trailerButton).execute(String.valueOf(movie.getMovieId()), VIDEO_QUERY);
        // execute ReviewsAsyncTask
        // To fetch reviews you will want to make a request to the /movie/{id}/reviews endpoint
        new ReviewsAsyncTask().execute(String.valueOf(movie.getMovieId()), REVIEW_URL_QUERY);


        mDb = AppDatabase.getInstance (getApplicationContext ());

        // Set initial favorite movies button values
        favoriteMoviesButton.setTextOn(getString(R.string.unfavorite_button_text));
        favoriteMoviesButton.setTextOff(getString(R.string.add_to_favorites_button_text));

        setUpFavoriteMovieButton();

        // Toggle
        favoriteMoviesButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toggle is enabled
                    favoriteMoviesButton.setBackgroundColor(getColor(R.color.colorAccent));
                    favoriteMoviesButton.getTextOn();
                    // insert movie
                    clickOnFavoriteMoviesButton();
                } else {
                    // Toggle is disabled
                    favoriteMoviesButton.setBackgroundColor(getColor(R.color.textColor));
                    favoriteMoviesButton.getTextOff();

                    // Delete movie
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.movieDao().deleteMovie(movie);
                        }
                    });
                }
            }
        });
    }

    // Insert movie
    private void clickOnFavoriteMoviesButton() {

        final Movie movie = getIntent().getParcelableExtra("movie");

        AppExecutors.getInstance().diskIO().execute (new Runnable() {
            // We will simplify this later
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDb.movieDao().insertMovie(movie);
                    }
                });
            }
        });
    }

    // Load movie by id
    private void setUpFavoriteMovieButton () {
        MovieDetailsViewModelFactory factory = new MovieDetailsViewModelFactory (mDb, movie.getMovieId());
        final MovieDetailsViewModel viewModel = ViewModelProviders.of(this, factory).get(MovieDetailsViewModel.class);

        // Observe the LiveData object in the ViewModel. Use it also when removing the observer
        viewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie dbMovie) {
                viewModel.getMovie().removeObserver(this);

                if (dbMovie == null) {
                    favoriteMoviesButton.setChecked (false);
                } else if ((movie.getMovieId() == dbMovie.getMovieId()) && !favoriteMoviesButton.isChecked ()){
                    favoriteMoviesButton.setChecked (true);
                } else {
                    favoriteMoviesButton.setChecked (false);
                }
            }
        });
    }

    // Use AsyncTask for the watch trailer button
    private class TrailerAsyncTask extends AsyncTask<String, Void, String> {
        private final Button button;

        public TrailerAsyncTask(Button button) {
            this.button = button;
        }

        @Override
        protected String doInBackground(String... strings) {
            Movie[] movies;
            try {
                URL url = JsonUtils.buildMovieIdUrl(strings[0], strings[1]);
                String movieSearchResults = JsonUtils.makeHttpRequest(url);

                JSONObject root = new JSONObject(movieSearchResults);
                JSONArray resultsArray = root.getJSONArray (RESULTS_QUERY);

                if (resultsArray.length () == 0) {
                    return null;
                } else {
                    movies = new Movie[resultsArray.length()];

                    for (int i = 0; i < resultsArray.length(); i++) {
                        // Initialize each object before it can be used
                        movies[i] = new Movie();

                        // Object contains all tags we're looking for
                        JSONObject movieInfo = resultsArray.getJSONObject(i);

                        // Store data in movie object, get the key
                        movies[i].setTrailerPath(movieInfo.getString(VIDEO_TRAILER_KEY));
                    }
                    // Returns only the first trailer from the results array, since there can be multiple trailers
                    return movies[0].getTrailerPath();
                }
            } catch (IOException  | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String temp) {
            button.setOnClickListener((View v) -> {
                if (temp == null) {
                    Toast.makeText(getApplicationContext(), "Sorry, no trailers here", Toast.LENGTH_SHORT).show();
                } else {
                    watchYoutubeVideo(getApplicationContext(), temp);
                }
            });
        }
    }

    // Youtube video intent - source: https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
    // Use an Intent to open a youtube link in a web browser
    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_APP_BASE + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + id));

        // Add the additional flags to the intent
        // if your android version is below Android - 6 then you need to add this line otherwise it will work above Android - 6.
        // Bcs. of error: AndroidRuntimeException: Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag.
        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // If youtube is not installed, plays from web
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }


    private class ReviewsAsyncTask extends AsyncTask<String, Void, Movie[]> {
        @Override
        protected Movie[] doInBackground(String... strings) {
            Movie[] movies;

            try {
                URL url = JsonUtils.buildMovieIdUrl(strings[0], strings[1]);
                String movieSearchResults = JsonUtils.makeHttpRequest(url);

                JSONObject root = new JSONObject(movieSearchResults);
                JSONArray resultsArray = root.getJSONArray(RESULTS_QUERY);
                movies = new Movie[resultsArray.length()];

                for (int i = 0; i < resultsArray.length(); i++) {
                    // Initialize each object before it can be used
                    movies[i] = new Movie();

                    // Object contains all tags we're looking for
                    JSONObject movieInfo = resultsArray.getJSONObject(i);

                    // Store data in movie object
                    movies[i].setReviewAuthor(movieInfo.getString(REVIEW_AUTHOR_QUERY));
                    movies[i].setReviewBody(movieInfo.getString(REVIEW_BODY_QUERY));
                    movies[i].setReviewUrl(movieInfo.getString(REVIEW_URL));
                }
                // Returns all reviews from the results array
                return movies;

            } catch (IOException  | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Movie[] movies) {
            reviewAdapter = new ReviewAdapter(movies, getApplicationContext());
            if (reviewAdapter.getItemCount()  == -1) {
                TextView noReviews = (TextView) findViewById(R.id.no_reviews);
                noReviews.setVisibility(TextView.VISIBLE);
            } else {
                // Set adapter on Recycler View
                reviewRecyclerView.setAdapter(reviewAdapter);
//                reviewRecyclerView.setNestedScrollingEnabled (false);
                reviewRecyclerView.setNestedScrollingEnabled (true);
            }
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
    }
}