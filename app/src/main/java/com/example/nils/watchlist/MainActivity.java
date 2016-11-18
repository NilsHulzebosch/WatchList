package com.example.nils.watchlist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public String userInput;
    public String searchResults;
    public ArrayList<MovieData> searchResultsArray;
    public String detailedResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            // first check whether there is a saved instance state, if so restore it
            userInput = savedInstanceState.getString("search");
            EditText userInputET = (EditText) findViewById(R.id.insertedMovie);
            userInputET.setText(userInput);

            searchResults = savedInstanceState.getString("searchResults");
            if (!(userInput.length() == 0)) {
                getSearchResults();
            }
        } else {

            // reset SharedPreferences when starting the app
            //Bundle extras = getIntent().getExtras();
            //if (extras != null) {
            //Boolean savedData = extras.getBoolean("savedData", false);
            //if ( !savedData ) {
            SharedPreferences shared = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.clear().apply();
            //}
            //}
        }
    }

    @Override
    // this method saves the state of the program
    public void onSaveInstanceState(Bundle outState) {
        EditText userInputET = (EditText) findViewById(R.id.insertedMovie);
        userInput = userInputET.getText().toString(); // turn user input into a string
        outState.putString("search", userInput);
        outState.putString("searchResults", searchResults);
        super.onSaveInstanceState(outState);
    }


    // check if user input is valid, then continue or give Toast
    public void searchMovies(View view) {
        EditText userInputET = (EditText) findViewById(R.id.insertedMovie);
        userInput = userInputET.getText().toString(); // turn user input into a string
        if (!(userInput.length() == 0)) {
            getSearchResults();
        } else {
            Toast.makeText(this, "Please enter a movie title", Toast.LENGTH_SHORT).show();
        }
    }

    // based on user search term, get movie results in ListView
    public void getSearchResults() {
        MovieAsyncTask movieAsyncTask = new MovieAsyncTask(this, true);
        movieAsyncTask.execute(userInput);

        try {
            searchResults = movieAsyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        showSearchResults();
    }

    public void showSearchResults() {
        if (searchResults.length() == 0) {
            Toast.makeText(this, "No data was found", Toast.LENGTH_LONG).show();
        } else {
            try {
                searchResultsArray = new ArrayList<>();
                JSONObject respObj = new JSONObject(searchResults);
                JSONArray movies = respObj.getJSONArray("Search");

                for (int i = 0; i < movies.length(); i++) {
                    String title = movies.getJSONObject(i).getString("Title");
                    String year = movies.getJSONObject(i).getString("Year");
                    String imdbID = movies.getJSONObject(i).getString("imdbID");
                    String jsonobject = movies.getJSONObject(i).toString();

                    searchResultsArray.add(new MovieData(
                            title, year, imdbID, jsonobject));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            final ListView results = (ListView) findViewById(R.id.movieResults);
            ArrayAdapter<MovieData> movieDatas = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, searchResultsArray);
            results.setAdapter(movieDatas);

            results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MovieData movie = (MovieData) results.getItemAtPosition(i);
                    getMovieDetails(movie.imdbID);

                    if ( !(detailedResult == null)) {
                        try {
                            JSONObject jsonMovie = new JSONObject(String.valueOf(detailedResult));
                            movie.setPoster(jsonMovie.getString("Poster"));
                            movie.setRunTime(jsonMovie.getString("Runtime"));
                            movie.setGenre(jsonMovie.getString("Genre"));
                            movie.setPlot(jsonMovie.getString("Plot"));
                            movie.setLanguage(jsonMovie.getString("Language"));
                            movie.setAwards(jsonMovie.getString("Awards"));
                            movie.setMetaScore(jsonMovie.getString("Metascore"));
                            movie.setImdbRating(jsonMovie.getString("imdbRating"));
                            movie.setImdbVotes(jsonMovie.getString("imdbVotes"));
                            movie.setJsonObject(jsonMovie.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Intent showMovie = new Intent(MainActivity.this, ShowMovie.class);
                    showMovie.putExtra("movie", movie); // pass MovieData object to next activity
                    startActivity(showMovie);
                }
            });
        }
    }

    /* When a user clicks on a movie from the movie results list,
     * the specific movie details from that movie are fetched from the server,
     * added to the movie object and this is passed as a parameter for the intent.
     * This way more detailed info can be given about the movie without loading all
     * this info before a user clicks on one.
     */
    public void getMovieDetails(String imdbID) {
        MovieAsyncTask movieAsyncTask = new MovieAsyncTask(this, false);
        movieAsyncTask.execute(imdbID);

        try {
            detailedResult = movieAsyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


}

