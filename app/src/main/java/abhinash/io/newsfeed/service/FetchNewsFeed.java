package abhinash.io.newsfeed.service;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import abhinash.io.newsfeed.model.Article;
import abhinash.io.newsfeed.util.AppConstants;
import abhinash.io.newsfeed.util.AppUtil;

/**
 * Created by khanal on 2/17/17.
 * This service will be used to fecth data from the api.
 */

public class FetchNewsFeed {



    /**
     * Page size for feed loads.
     */
    private static final int pageSize = AppConstants.PAGE_SIZE;

    /**
     * Private constructor for singleton.
     */
    private FetchNewsFeed() {}

    /**
     * Query string.
     */
    private String queryString = AppConstants.API_QUERY_KEYWORD;

    /**
     * Get the list of articles for page.
     * @param page -.
     * @return -.
     */
    public ArrayList<Article> getArticlesFromPage(final int page) {
        Uri uri = Uri.parse(AppConstants.URL_STRING);
        uri = uri.buildUpon()
                .appendQueryParameter(AppConstants.API_KEY_KEY, AppConstants.API_KEY)
                .appendQueryParameter(AppConstants.API_QUERY_KEY, queryString)
                .appendQueryParameter(AppConstants.API_QUERY_PAGE_SIZE_KEY, String.valueOf(pageSize))
                .appendQueryParameter(AppConstants.API_QUERY_PAGE_KEY, String.valueOf(page))
                .build();
        ArrayList<Article> articles = null;
        try {
            String responseBody = downloadUrl(new URL(uri.toString()));
            if (null != responseBody) {
                articles = AppUtil.getArticlesFromResponseString(responseBody);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return articles;
    }


    /**
     * Given a URL, sets up a connection and gets the HTTP response body from the server.
     * If the network request is successful, it returns the response body in String form. Otherwise,
     * it will throw an IOException.
     */
    private String downloadUrl(URL url) throws IOException {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = "";
        try {
            connection = (HttpsURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(3000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(3000);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            if (stream != null) {
                // Converts Stream to String with max length of 500.
                result = readStream(stream, 512);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    /**
     * Converts the contents of an InputStream to a String.
     */
    private String readStream(InputStream stream, int bufferSize) throws IOException {
        String result = "";
        // Read InputStream using the UTF-8 charset.
        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        // Create temporary buffer to hold Stream data with specified max length.
        char[] buffer = new char[bufferSize];
        // Populate temporary buffer with Stream data.
        int readSize = 0;
        while ((readSize = reader.read(buffer)) != -1) {
            result += new String(buffer, 0, readSize);
        }
        return result;
    }

    /**
     * The query string incase needs updating.
     * @param queryString -.
     */
    public void setQueryString(@NonNull final String queryString) {
        this.queryString = queryString;
    }

    /**
     * Holds a singleton instance for the service.
     */
    public static FetchNewsFeed sharedInstance() {
        return new FetchNewsFeed();
    }

}
