package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<String> mImageList;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private final Context mContext;

    // data is passed into the constructor
    ImageAdapter(Context context, List imageList) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mImageList = imageList;
    }

    // stores and recycles views as they are scrolled off screen
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

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_item_image, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the ImageView in each item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String path = mImageList.get(position);

        Picasso.get()
                .load(path)
                .fit()
                .error(R.drawable.ghost)
                .placeholder(R.drawable.ghost)
                .into(holder.myImageView);
    }

    // total number of items
    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mImageList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}