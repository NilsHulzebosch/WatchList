package com.example.nils.watchlist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Set;

public class ShowWatchlist extends AppCompatActivity {

    public ArrayList<MovieData> searchResultsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_watchlist);

        // get SharedPreferences
        SharedPreferences shared = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        Set<String> set = shared.getStringSet("key", null);

        try {
            convertToMovie(set); // turn into MovieData objects
        } catch (JSONException e) {
            e.printStackTrace();
        }

        showWatchList(); // display adapter with movies
    }

    /* Converts the Set of Strings into an ArrayList of MovieData's,
     * because SharedPreferences has no Serializable store option.
     */
    public void convertToMovie(Set<String> set) throws JSONException {
        searchResultsArray = new ArrayList<>();

        // check values
        if (set != null) {
            for (Object aSet : set) {
                JSONObject movie = new JSONObject(String.valueOf(aSet));
                String title = movie.getString("Title");
                String year = movie.getString("Year");
                String imdbID = movie.getString("imdbID");
                String jsonobject = movie.toString();

                MovieData movieD = new MovieData(title, year, imdbID, jsonobject);
                movieD.setPoster(movie.getString("Poster"));
                movieD.setRunTime(movie.getString("Runtime"));
                movieD.setGenre(movie.getString("Genre"));
                movieD.setPlot(movie.getString("Plot"));
                movieD.setLanguage(movie.getString("Language"));
                movieD.setAwards(movie.getString("Awards"));
                movieD.setMetaScore(movie.getString("Metascore"));
                movieD.setImdbRating(movie.getString("imdbRating"));
                movieD.setImdbVotes(movie.getString("imdbVotes"));

                searchResultsArray.add(movieD);
            }
        }
    }

    // pass the ArrayList of MovieData's to an Adapter and show the list
    // also add setOnItemClickListener in case the user clicks on an item
    public void showWatchList() {
        final ListView results = (ListView) findViewById(R.id.watchList);
        ArrayAdapter<MovieData> movieOverview = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, searchResultsArray);
        results.setAdapter(movieOverview);

        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MovieData movie = (MovieData) results.getItemAtPosition(i);
                Intent showMovie = new Intent(ShowWatchlist.this, ShowMovie.class);
                showMovie.putExtra("movie", movie); // pass MovieData object to next activity
                showMovie.putExtra("checked", true); // set checkbox to checked
                startActivity(showMovie);
            }
        });
    }

    // when the button is clicked, go to MainActivity to search for movies
    public void goToSearch(View view) {
        Intent searchForMovies = new Intent(ShowWatchlist.this, MainActivity.class);
        startActivity(searchForMovies);
    }
}
