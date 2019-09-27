package com.example.android.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Property;

public class Movie implements Parcelable {
    private String originalTitle;
    private String posterPath;
    private String overview;
    private Double voteAverage;
    private String releaseDate;

    final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

    public Movie()  {
    }

    // Getter methods
    public String getOriginalTitle() {
        return originalTitle;
    }
    public String getPosterPath() {
        return POSTER_BASE_URL + posterPath;
    }
    public Double getVoterAverage() {
        return voteAverage;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public String getOverview() {
        return overview;
    }

    // Setter methods
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
    public void setVoterAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }


    // Use Android Parcelable interface to transfer object and its data between activities
    // (by deconstructing the object in one activity and reconstructing it in another)

    // Add object values to Parcel in preparation for transfer
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
        dest.writeString(overview);
    }

    // This method is the constructor, called on the receiving activity, where you will be collecting values.
    // When the secondary activity calls the getParcelableExtra method of the intent object to start the process,
    // this constructor is where you collect the values and set up the properties of the object:
    // At this point you’ve populated the object with data.

    // Constructor used for parcel
    public Movie(Parcel parcel) {
        // Read and set saved values from parcel
        originalTitle = parcel.readString();
        posterPath = parcel.readString();
        voteAverage = parcel.readDouble();
        releaseDate = parcel.readString();
        overview = parcel.readString();
    }

    // This method binds everything together. There’s little needed to do here as the createFromParcel method will return newly populated object.
    // Creator - used when un-parceling our parcle (creating the object)
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    // Return hashcode of object - this method doesn’t do much.
    @Override
    public int describeContents() {
        return hashCode();
    }
}