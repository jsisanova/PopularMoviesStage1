package com.example.android.popularmoviesstage1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView myImage = (ImageView) findViewById(R.id.poster_iv);
        Picasso.get()
               .load("http://i.imgur.com/DvpvklR.png")
               .into(myImage);
    }
}
