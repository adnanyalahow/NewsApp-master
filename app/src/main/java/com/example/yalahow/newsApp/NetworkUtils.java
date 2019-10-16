package com.example.yalahow.newsApp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Adnan Yalahow on 7/8/2018.
 */

public class NetworkUtils {
    public static final String LOG_TAG = NetworkUtils.class.getName();
    /**
     * Create a private constructor because no one should ever create a {@link NetworkUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name NetworkUtils (and an object instance of NetworkUtils is not needed).
     */
    private NetworkUtils() {
    }
    /**
     * Query the GUARDIAN WEBSITE and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {


        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link News} object
        List<News> newsList = extractNews(jsonResponse);

        // Return the list of {@link News}
        return newsList;
    }
    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //extract and parsing the json
    public static List<News> extractNews(String newsJSON) {


        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding news to
        List<News> news = new ArrayList<>();
        // Try to parse the newsJSON. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject root = new JSONObject(newsJSON);
            //create another object to access the array that contains the data of news
            JSONObject root2 = root.getJSONObject("response");
            //create an array to access the date we need for the news list
            JSONArray jsonArray = root2.getJSONArray("results");
            // For each news item in the newsList, create an {@link News} object
            for (int i = 0; i < jsonArray.length(); i++) {
                // Get a single news item at position i within the list of news
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extract the value for the key called "webTitle"
                String webTitle = jsonObject.getString("webTitle");

                // Extract the value for the key called "webUrl"
                String webUrl = jsonObject.getString("webUrl");

                //Extract the value for the key called "sectionName"
                String sectionName = jsonObject.getString("sectionName");

                //Extract the value for the key called "webPublicationDate"
                String webPublicationDate = jsonObject.getString("webPublicationDate");
                //format the extracted Date using the method formatDate()
                webPublicationDate = formatDate(webPublicationDate);
//

                // Create a new {@link News} object with the webTitle, webUrl, webPublicationDate,
                News newsdata = new News(webTitle, webUrl, sectionName, webPublicationDate);

                // Add the new {@link News} to the list of news..
                news.add(newsdata);


            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the News JSON results", e);



        }
        // Return the list of news
        return news;
    }
    /**
     * Returns a formatted Date
     */
    private static String formatDate(String dateInJson) {
        String jsonDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat jsonFormatter = new SimpleDateFormat(jsonDatePattern, Locale.UK);
        try {
            Date parsedJsonDate = jsonFormatter.parse(dateInJson);
            String finalDatePattern = "MMM d, yyy";
            SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalDatePattern, Locale.UK);
            return finalDateFormatter.format(parsedJsonDate);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error parsing JSON date: ", e);
            return "";
        }
    }


}
