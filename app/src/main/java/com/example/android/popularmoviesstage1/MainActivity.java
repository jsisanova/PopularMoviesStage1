package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageAdapter adapter;
    private RecyclerView recyclerView;
    private Movie[] movies;

    private final String MOST_POPULAR_QUERY = "popular";
    private final String HIGHEST_RATED_QUERY = "top_rated";
    // To use Snackbar
    private View coordinator_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recycler_view);

        // Use GridLayoutManager
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        if (isOnline ()) {
            //Default to Popular Query Sort
            new FetchDataAsyncTask().execute(MOST_POPULAR_QUERY);
        } else {
            coordinator_layout = (View) findViewById(R.id.coordinator_layout);
            Snackbar snackbar = Snackbar
                    .make(coordinator_layout, "Currently there is no internet connection.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Snackbar snackbar1 = Snackbar.make(coordinator_layout, "Message is restored!", Snackbar.LENGTH_SHORT);
                            snackbar1.show();
                        }
                    });
            // Changing message text color ("RETRY")
            snackbar.setActionTextColor(Color.RED);

            // Changing action button text color ("Currently there is no internet connection.")
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);

            snackbar.show();
//            Toast.makeText(getApplicationContext (), "Currently there is no internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        MenuInflater inflater = getMenuInflater ();
        inflater.inflate(R.menu.menu_main, menu);

        // Return true to display this menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get the ID from the MenuItem
        int id = item.getItemId ();
        if (id == R.id.most_popular_setting) {
            new FetchDataAsyncTask ().execute(MOST_POPULAR_QUERY);
        } else if (id == R.id.highest_rated_setting) {
            new FetchDataAsyncTask().execute(HIGHEST_RATED_QUERY);
        } else {
            // TODO: Add later my favorite movies intent
            new FetchDataAsyncTask().execute(MOST_POPULAR_QUERY);
        }
        return super.onOptionsItemSelected (item);
    }


    // Change string of movie data to an ARRAY OF MOVIE OBJECTS
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
            recyclerView.setAdapter(adapter);
        }
    }

    /***
     * Make sure the app does not crash when there is no network connection (ping a server)
     * source: https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     ***/

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }



    // Make the Adapter an inner class of the MainActivity (so it has access to the activity directly and you can start another one easily).
    // Otherwise AndroidRuntimeException occurs, when you initialise the adapter this way: adapter = new ImageAdapter(getApplicationContext(), movies);
    // and when you then call startActivity() from outside of an Activity context (getApplicationContext() would be a wrong type of context in this case).
    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
        private Movie[] mMovie;
        private LayoutInflater mInflater;
        private final Context mContext;
        // Pass data into the constructor
        public ImageAdapter(Context context, Movie[] movie) {
            this.mInflater = LayoutInflater.from(context);
            this.mContext = context;
            this.mMovie = movie;
        }
        // Store and recycle views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder  {
            ImageView myImageView;
            ViewHolder(View itemView) {
                super(itemView);
                myImageView = itemView.findViewById(R.id.image);
            }
        }
        // Inflate the cell layout from xml when needed (invoked by Layout Manager)
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.single_item_image, parent, false);
            return new ViewHolder(view);
        }
        // Bind the data to the view in each item (invoked by Layout Manager)
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final String path = mMovie[position].getPosterPath();
            Picasso.get()
                    .load(path)
                    .fit()
                    .error(R.drawable.ghost)
                    .placeholder(R.drawable.ghost)
                    .into(holder.myImageView);
            // Create your intent object inside the first activity and use the putExtra method to add the whole class as an extra.
            // (at this point parcelable starts serializing your object). If this works, the new activity will open.
            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("movie", mMovie[position]);
                MainActivity.this.startActivity(intent);
            });
        }
        // Total number of items
        @Override
        public int getItemCount() {
            return mMovie.length;
        }
    }
}