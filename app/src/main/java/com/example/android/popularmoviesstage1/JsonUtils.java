package com.example.android.popularmoviesstage1;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class JsonUtils {
    final static String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie";
    final static String API_KEY = "Enter your API key here";

    public static URL buildUrl(String[] query) {
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(query[0])
                .appendQueryParameter("api_key", API_KEY)
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