package com.example.android.popularmoviesstage1;

public class Movie {
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
}