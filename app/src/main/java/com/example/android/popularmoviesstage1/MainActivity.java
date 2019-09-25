package com.example.android.popularmoviesstage1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ImageAdapter.ItemClickListener {

    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // data to populate the RecyclerView with
        String[] data = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        adapter = new ImageAdapter(this, data);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
    }

    @Override
    public void onItemClick(View view, int position) {
        String toastMessage = "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position;
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
    }
}