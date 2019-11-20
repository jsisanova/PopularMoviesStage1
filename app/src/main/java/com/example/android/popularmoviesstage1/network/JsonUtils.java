package com.example.android.popularmoviesstage1.network;

import android.net.Uri;
import android.util.Log;

import com.example.android.popularmoviesstage1.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class JsonUtils {
    final static String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie";
    final static String API_KEY_QUERY_PARAM = "api_key";
    // The app uses the Movie Database API. To run the app, you need to request a free API key from themoviedb.org.
    // Create your account here: https://www.themoviedb.org/account/signup . Enter the API key here.
    final static String API_KEY = BuildConfig.ApiKey;

    public static URL buildUrl(String[] query) {
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(query[0])
                .appendQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    // for trailers and reviews
    public static URL buildMovieIdUrl(String id, String query) {
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(query)
                .appendQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String makeHttpRequest(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e){
            Log.e("fail", "failed", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}