package com.example.nils.watchlist;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.util.ArrayList;

public class MovieAsyncTask extends AsyncTask<String, Integer, String> {

    MainActivity activity;
    Context context;
    public ArrayList<MovieData> searchResults;
    public boolean generalSearch;

    // constructor
    public MovieAsyncTask(MainActivity activity, Boolean generalSearch) {
        this.activity = activity;
        this.context = this.activity.getApplicationContext();
        this.searchResults = new ArrayList<>();
        this.generalSearch = generalSearch;
    }

    // onPreExecute()
    protected void onPreExecute() {
        String toastText;
        if (generalSearch) {
            toastText = "Getting search results from server";
        } else {
            toastText = "Getting movie details from server";
        }
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
    }

    // doInBackground()
    protected String doInBackground(String... params) {
        return HttpRequestHelper.downloadFromServer(generalSearch, params);
    }

    // onProgressUpdate()

    // onPostExecute()
    protected void onPostExecute(String result) {
        super.onPostExecute(result); // call existing class
    }
}