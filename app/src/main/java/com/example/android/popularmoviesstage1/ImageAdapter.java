package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final Movie[] mMovie;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private final Context mContext;

    // Pass data into the constructor
    public ImageAdapter(Context context, Movie[] movie) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mMovie = movie;
    }

    // Store and recycle views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView myImageView;

        ViewHolder(View itemView) {
            super(itemView);
            myImageView = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    // Inflate the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_item_image, parent, false);
        return new ViewHolder(view);
    }

    // Bind the data to the ImageView in each item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String path = mMovie[position].getPosterPath();

        Picasso.get()
                .load(path)
                .fit()
                .error(R.drawable.ghost)
                .placeholder(R.drawable.ghost)
                .into(holder.myImageView);
    }

    // Total number of items
    @Override
    public int getItemCount() {
        return mMovie.length;
    }

    // Allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // Parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}