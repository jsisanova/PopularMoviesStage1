package com.example.android.popularmoviesstage1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ImageAdapter.ItemClickListener {

    ImageAdapter adapter;
    RecyclerView recyclerView;
    Movie[] movies;

    private final String POPULAR_QUERY = "popular";
    private final String TOP_RATED_QUERY = "top_rated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recycler_view);

        // Use GridLayoutManager
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        // Set up spinner
        Spinner mySpinner = findViewById(R.id.spinner);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // If Most Popular is selected
                        new FetchDataAsyncTask().execute(POPULAR_QUERY);
                        break;
                    case 1:
                        // If Highest Rating is selected
                        new FetchDataAsyncTask().execute(TOP_RATED_QUERY);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Auto-generated method stub
            }
        });
    }

    // Toast message after clicking on single item in RecycleView
    @Override
    public void onItemClick(View view, int position) {
        String toastMessage = "You clicked on item at cell position " + position;
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    // Change string of movie data to an ARRAY OD MOVIE OBJECTS
    public Movie[] changeMoviesDataToArray(String moviesJsonResults) throws JSONException {
        // JSON FILTERS
        final String RESULTS = "results";

        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String VOTER_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String OVERVIEW = "overview";

        // Get results as an array
        JSONObject moviesJson = new JSONObject(moviesJsonResults);
        JSONArray resultsArray = moviesJson.getJSONArray(RESULTS);

        // Create array of Movie objects that stores data from the JSON string
        movies = new Movie[resultsArray.length()];

        // Iterate through movies and get data
        for (int i = 0; i < resultsArray.length(); i++) {
            // Initialize each object before it can be used
            movies[i] = new Movie();

            // Object contains all tags we're looking for
            JSONObject movieInfo = resultsArray.getJSONObject(i);

            // Store data in movie object
            movies[i].setOriginalTitle(movieInfo.getString(ORIGINAL_TITLE));
            movies[i].setPosterPath(movieInfo.getString(POSTER_PATH));
            movies[i].setOverview(movieInfo.getString(OVERVIEW));
            movies[i].setVoterAverage(movieInfo.getDouble(VOTER_AVERAGE));
            movies[i].setReleaseDate(movieInfo.getString(RELEASE_DATE));
        }
        return movies;
    }

    // Use AsyncTask to fetch movie data
    public class FetchDataAsyncTask extends AsyncTask<String, Void, Movie[]> {
        public FetchDataAsyncTask() {
            super();
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            // Holds data returned from the API
            String movieResults;

            try {
                URL url = JsonUtils.buildUrl(params);
                movieResults = JsonUtils.makeHttpRequest(url);

                if(movieResults == null) {
                    return null;
                }
            } catch (IOException e) {
                return null;
            }

            try {
                // Call method to change string of movie data to an ARRAY OD MOVIE OBJECTS
                return changeMoviesDataToArray (movieResults);
            } catch (JSONException e) {
                e.printStackTrace ();
            }
            return null;
        }

        protected void onPostExecute(Movie[] movies) {
            adapter = new ImageAdapter(getApplicationContext(), movies);
            adapter.setClickListener(MainActivity.this);
            recyclerView.setAdapter(adapter);
        }
    }
}