package com.example.nils.watchlist;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequestHelper {

    // make string for URL
    private static final String startURL = "http://www.omdbapi.com/?";
    private static final String generalSearchStr = "s=";
    private static final String byImdbID = "i=";
    private static final String endURL = "&plot=short&r=json";

    // method to download from server
    protected static String downloadFromServer(Boolean generalSearch, String... params) {

        // declare return String result
        String result = "";

        // get chosen tag from argument
        String chosenTag = params[0]; // this is the user input

        String completeURL = "";
        if (generalSearch) {
            // if it is a search for movies in general, choose this URL
            completeURL= startURL + generalSearchStr + chosenTag + endURL;
        } else {
            // else it is a search for a specific movie ID, then choose this URL
            completeURL = startURL + byImdbID + chosenTag + endURL;
        }

        // turn string into URL
        URL url = null;

        try {
            url = new URL(completeURL);
        } catch(MalformedURLException e) {
            e.printStackTrace();
        }


        // make the connection;
        HttpURLConnection connection;
        if (url != null) {
            try {
                // open connection, set request method
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // get response code
                Integer responseCode = connection.getResponseCode();

                //if response if between 200 and 300, there is a connection,read InputStream
                if (responseCode >= 200 && responseCode < 300) {
                    InputStream is = connection.getInputStream();
                    System.out.println("TEST2");
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;

                    while ((line = br.readLine()) != null) {
                        result = result + line;
                    }

                } else {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getErrorStream()) );
                    // communicate correct error
                }

            } catch(IOException e) {
                System.out.println("Something went wrong");
            }
        }
        return result;
    }
}
