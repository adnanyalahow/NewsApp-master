package com.example.yalahow.newsApp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Adnan Yalahow  on 7/8/2018.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    public static final String LOG_TAG = NewsLoader.class.getName();

    /** Query URL */
    private String url;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public NewsLoader(Context context, String url){
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG, "on start is loading a loader");
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        Log.v(LOG_TAG, "loadInBackground is loading some work in the background");
        if (url == null){
            return null;
        }
        // Perform the HTTP request for earthquake data and process the response.
        List<News> newsList = NetworkUtils.fetchNewsData(url);
        return newsList;
    }
}
