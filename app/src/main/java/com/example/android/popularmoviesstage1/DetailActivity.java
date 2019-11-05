package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView originalTitle = (TextView) findViewById (R.id.title_tv);
        ImageView poster = (ImageView) findViewById (R.id.poster_iv);
        TextView rating = (TextView) findViewById (R.id.rating_tv);
        TextView releaseDate = (TextView) findViewById (R.id.release_date_tv);
        TextView overview = (TextView) findViewById (R.id.overview_tv);

        // Collect the intent object and extract the movie class that’s been converted into a parcel in the second activity.
        // Once you’ve done this you can call the standard methods to get the data like movie title, poster etc..
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        Movie movie = intent.getParcelableExtra("movie");

        originalTitle.setText(movie.getOriginalTitle());
        rating.setText ("Rating \n" + String.valueOf(movie.getVoterAverage ()) + " / 10");
        releaseDate.setText ("Release Date \n" + movie.getReleaseDate());
        overview.setText (movie.getOverview ());

        Picasso.get()
                .load(movie.getPosterPath())
//                .load("http://i.imgur.com/DvpvklR.png")
                .fit()
                .error(R.drawable.ghost)
                .placeholder(R.drawable.ghost)
                .into(poster);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
    }
}