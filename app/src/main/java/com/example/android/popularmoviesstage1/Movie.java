package com.example.android.popularmoviesstage1;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

// Room will generate a table named "movie"
// Our class is now entity that is associated with table named "movie"
@Entity(tableName = "movie")
public class Movie implements Parcelable {
    // Annotate the dbMovieId as PrimaryKey
    @PrimaryKey(autoGenerate = true)
    private int dbMovieId;

    private String originalTitle;
    private String posterPath;
    private String overview;
    private double voteAverage;
    private String releaseDate;

    private int movieId;
    private String trailerPath;
    private String reviewAuthor;
    private String reviewBody;
    private String reviewUrl;

//    private final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

    @Ignore
    private boolean isFavoriteMovie = false;


    public Movie()  {
    }

    // Getter methods
    public int getDbMovieId() { return movieId; }
    public String getOriginalTitle() {
        return originalTitle;
    }
    public String getPosterPath() {
        return "https://image.tmdb.org/t/p/w185" + posterPath;
    }
    public double getVoteAverage() {
        return voteAverage;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public String getOverview() {
        return overview;
    }
    public int getMovieId() {
        return movieId;
    }
    public String getTrailerPath() {
        return trailerPath;
    }
    public String getReviewAuthor() {
        return reviewAuthor;
    }
    public String getReviewBody() {
        return reviewBody;
    }
    public String getReviewUrl() {
        return reviewUrl;
    }
    public boolean getIsFavoriteMovie() {
        return isFavoriteMovie;
    }

    // Setter methods
    public void setDbMovieId(int dbMovieId) {
        this.dbMovieId = movieId;
    }
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
    public void setTrailerPath(String trailerPath) {
        this.trailerPath = trailerPath;
    }
    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }
    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }
    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }
    public void setIsFavoriteMovie(boolean isFavoriteMovie) {
        this.isFavoriteMovie = isFavoriteMovie;
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
        dest.writeString(trailerPath);
        dest.writeInt (movieId);
        dest.writeString (reviewAuthor);
        dest.writeString (reviewBody);
        dest.writeString (reviewUrl);
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
        trailerPath = parcel.readString();
        movieId = parcel.readInt ();
        reviewAuthor = parcel.readString ();
        reviewBody = parcel.readString ();
        reviewUrl = parcel.readString ();
    }

    // This method binds everything together. There’s little needed to do here as the createFromParcel method will return newly populated object.
    // Creator - used when un-parceling our parcel (creating the object)
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

    // Return hashcode of object - this method does not do much.
    @Override
    public int describeContents() {
        return hashCode();
    }
}