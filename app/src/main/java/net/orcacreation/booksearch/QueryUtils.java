package net.orcacreation.booksearch;

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
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils(){
    }

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */

    public static ArrayList<Book> extractFeatureFromJson (String bookJSON){
        if (TextUtils.isEmpty(bookJSON)){
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Book> books = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {

            // Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject jsonRootObject = new JSONObject(bookJSON);
            JSONArray jsonItems = jsonRootObject.optJSONArray("items");

            for (int i = 0; i < jsonItems.length(); i++){
                String mUrlImage;
                String mAuthor;
                JSONObject jsonItem = jsonItems.getJSONObject(i);
                JSONObject jsonVolumeInfo = jsonItem.optJSONObject("volumeInfo");
                String mTitle = jsonVolumeInfo.getString("title");
                //Log.e(LOG_TAG, "mTitle is:" + mTitle);
                JSONArray jsonAuthors = jsonVolumeInfo.optJSONArray("authors");
                if (jsonAuthors != null){
                    mAuthor = jsonAuthors.getString(0);
                } else mAuthor = MainActivity.getContext().getString(R.string.no_author_info); //"No author information available";
                //Log.e(LOG_TAG, "mAuthor is:" + mAuthor);
                JSONObject jsonImageLinks = jsonVolumeInfo.optJSONObject("imageLinks");
                if (jsonImageLinks != null){
                    mUrlImage = jsonImageLinks.getString("smallThumbnail");
                }else mUrlImage = null;
                //Log.e(LOG_TAG, "mUrlImage is:" + mUrlImage);
                String mUrlWeb = jsonVolumeInfo.getString("previewLink");
               // Log.e(LOG_TAG, "mUrlWeb is:" + mUrlWeb);

                books.add(new Book(mTitle, mAuthor, mUrlWeb, mUrlImage));
                //Log.e(LOG_TAG, "item count:" + books.size());
            }


        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }

        return books;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
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
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
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

public static List<Book> fetchEarthQuakeData (String requestUrl){
//Fake delay to simulate slow connection, change millis to the desired time
    try {
        Thread.sleep(0);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    // Create URL object
    URL url = createUrl(requestUrl);

    // Perform HTTP request to the URL and receive a JSON response back
    String jsonResponse = null;
    try {
        jsonResponse = makeHttpRequest(url);
    } catch (IOException e) {
        Log.e(LOG_TAG, "Problem making the HTTP request.", e);
    }

    //Extract relevent fields from the JSON response and create a list of (@link Book}s

    List<Book> books = extractFeatureFromJson(jsonResponse);

    // Return the list of {@link Book}s

    return books;
 }

}
