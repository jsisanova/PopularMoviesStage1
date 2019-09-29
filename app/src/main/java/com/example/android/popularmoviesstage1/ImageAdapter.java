package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

    // Inflate the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_item_image, parent, false);
        return new ViewHolder(view);
    }

    // Bind the data to the view in each item (invoked by Layout Manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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
            mContext.startActivity(intent);
        });
    }

    // Total number of items
    @Override
    public int getItemCount() {
        return mMovie.length;
    }
}