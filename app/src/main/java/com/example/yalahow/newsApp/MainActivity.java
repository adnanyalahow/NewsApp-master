package com.example.yalahow.newsApp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private NewsAdapter mAdapter;

    private TextView mEmptyTextView;

    private View loadingIndicator;

    private String GUARDIAN_URL = "https://content.guardianapis.com/search?";

    private static final int EARTHQUAKE_LOADER_ID = 1;

    public static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);
        //link the listView to an empty if no news date is found
        mEmptyTextView = (TextView) findViewById(R.id.empty);
        newsListView.setEmptyView(mEmptyTextView);



        loadingIndicator= findViewById(R.id.progressBar);


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMngr= (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMngr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo !=null && networkInfo.isConnected()){

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            Log.v(LOG_TAG, "initLoader is about to intialize a loader");
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible

            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyTextView.setText(R.string.no_internet_connection);
        }

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        /*
        Set an item click listener on the ListView, which sends an intent to a web browser
        to open a website with more information about the selected news.
        */
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Find the current news item that was clicked on
                News currentNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getWebUrl());


                // Create a new intent to view the news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                //Send the intent to launch a new activity
                startActivity(websiteIntent);

            }
        });

    }




    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "onCreateLoader is creating a loader");
        Uri baseUir = Uri.parse(GUARDIAN_URL);
        Uri.Builder builder = baseUir.buildUpon();

        builder.appendQueryParameter("q", "technology%20AND%20economy%20ANDbusiness");
        builder.appendQueryParameter("order-by", "newest");
        builder.appendQueryParameter("page-size", "20");
        builder.appendQueryParameter("api-key", "1ddfcbda-1398-42ad-9856-fe474ff9b9b4");
        return new NewsLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        Log.v(LOG_TAG, "onLoadFinished is finishing a loader");

        //set the progressBar to Gone when the onLoadFinished() is called
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No news found."
        mEmptyTextView.setText(R.string.results);

        // Clear the adapter of previous news data
        mAdapter.clear();

        // If there is a valid list of {@link News}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty()){
            mAdapter.addAll(news);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.v(LOG_TAG, "onLoaderReset is reseting a loader");
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();

    }
}
