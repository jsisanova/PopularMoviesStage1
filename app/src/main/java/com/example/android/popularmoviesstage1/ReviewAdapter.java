package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Movie[] movies;
    private Context context;

    // Pass data into the constructor
    public ReviewAdapter(Movie[] movies, Context context) {
        this.context = context;
        this.movies = movies;
    }

    // Store and recycle views as they are scrolled off screen
    // Provide a reference to the item views in viewholder
    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView reviewAuthorTv;
        TextView reviewBodyTv;
        Button fullReviewButton;
        View divider;

        ViewHolder(View itemView) {
            super(itemView);
            reviewAuthorTv = (TextView) itemView.findViewById (R.id.review_author_tv);
            reviewBodyTv = (TextView) itemView.findViewById (R.id.review_body_tv);
            fullReviewButton = (Button) itemView.findViewById (R.id.full_review_button);
            // needed to set visibility to GONE by last item
            divider = (View) itemView.findViewById(R.id.review_divider);
        }
    }

    // Inflate the cell layout from xml when needed (invoked by Layout Manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater;
        mInflater = LayoutInflater.from(parent.getContext());

        View view = mInflater.inflate(R.layout.single_movie_review, parent, false);
        return new ViewHolder(view);
    }

    // Bind the data to the view in each item (invoked by Layout Manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get element from the dataset at this position
        // Replace the contents of the view with that element
        holder.reviewAuthorTv.setText(movies[position].getReviewAuthor());
        holder.reviewBodyTv.setText(movies[position].getReviewBody());

        // Open url to see the full review on themoviedb.org
        holder.fullReviewButton.setOnClickListener((View v) -> {
            Intent reviewIntent = new Intent(Intent.ACTION_VIEW);
            reviewIntent.setData(Uri.parse(movies[position].getReviewUrl()));
            // Add the additional flags to the intent
            reviewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(reviewIntent);
        });

        // Set visibility of divider to GONE by last item
        if (position == movies.length - 1) {
            holder.divider.setVisibility(View.GONE);
        }
    }

    // Total number of items
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (movies == null || movies.length == 0) {
            return -1;
        }
        return movies.length;
    }
}