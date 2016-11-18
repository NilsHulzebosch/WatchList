package com.example.nils.watchlist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashSet;
import java.util.Set;

public class ShowMovie extends AppCompatActivity {

    MovieData movie;
    String jsonobjectStr;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_movie);

        Bundle extras = getIntent().getExtras();
        movie = (MovieData) extras.getSerializable("movie");
        Boolean checked = extras.getBoolean("checked", false);
        displayMovie(checked);

    }

    /* This method takes care of the visual part:
     * put all the correct movie data in the correct TextView, show Image and toggle CheckBox.
     */
    public void displayMovie(Boolean checked) {
        if (movie != null) {

            // set checkbox to checked
            checkBox = (CheckBox) findViewById(R.id.toggleWatchList);
            checkBox.setChecked(checked);

            // get movie specifics
            String poster = movie.poster;
            String runTime = movie.runTime;
            String genre = movie.genre;
            String plot = movie.plot;
            String awards = movie.awards;
            String metaScore = movie.metaScore;
            String imdbRating = movie.imdbRating;
            String imdbVotes = movie.imdbVotes;
            jsonobjectStr = movie.jsonobject;

            // display movie title and year
            TextView titleTV = (TextView) findViewById(R.id.title_year);
            String titleYear = movie.toString();
            titleTV.setText(titleYear);

            // display poster image in an ImageView
            if ( !(poster.length() == 0) ) {
                new DownloadImageTask((ImageView) findViewById(R.id.poster)).execute(poster);
            }

            // display genre
            TextView genreTV = (TextView) findViewById(R.id.genre);
            genreTV.setText(genre);

            // display duration
            TextView durationTV = (TextView) findViewById(R.id.duration);
            durationTV.setText(runTime);

            // display plot
            TextView plotTV = (TextView) findViewById(R.id.plot);
            plotTV.setText(plot);

            // display rating
            TextView ratingTV = (TextView) findViewById(R.id.rating);
            String newimdbRating = imdbRating + "/10";
            ratingTV.setText(newimdbRating);

            // display amount of votes
            TextView votesTV = (TextView) findViewById(R.id.votes);
            String newImdbVotes = imdbVotes + " votes";
            votesTV.setText(newImdbVotes);

            // display amount of votes
            TextView metaTV = (TextView) findViewById(R.id.meta);
            String newMetaScore = metaScore + " Metascore";
            metaTV.setText(newMetaScore);

            // display awards
            TextView awardsTV = (TextView) findViewById(R.id.awards);
            awardsTV.setText(awards);

        } else {
            Toast.makeText(this, "Something went wrong. Please try again.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /* This method is called when the checkbox is clicked.
     * First get SharedPreferences, and check whether checkbox was already checked.
     * Then execute correct activity, give Toast and update SharedPreferences.
     */
    public void addToWatchList(View view) {
        SharedPreferences shared = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        Set<String> set = shared.getStringSet("key", null);

        checkBox = (CheckBox) findViewById(R.id.toggleWatchList);
        boolean checked = checkBox.isChecked();

        if( checked ) {
            checkBox.setChecked(true);
            if (set != null) {
                set.add(jsonobjectStr);
                editor.putStringSet("key", set);
            } else {
                Set<String> newset = new HashSet<>();
                newset.add(jsonobjectStr);
                editor.putStringSet("key", newset);
            }
            Toast.makeText(this, "Added " + movie.title + " to watchlist.", Toast.LENGTH_SHORT).show();
        } else {
            checkBox.setChecked(false);
            if (set != null) {
                set.remove(jsonobjectStr);
            }
            Toast.makeText(this, "Removed " + movie.title + " from watchlist.", Toast.LENGTH_SHORT).show();
        }
        editor.apply();
    }

    // go to ShowWatchlist activity
    // no arguments are given, this is stored in SharedPreferences
    public void goToWatchlist(View view) {
        Intent addToWatchList = new Intent(this, ShowWatchlist.class);
        startActivity(addToWatchList);
        finish();
    }
}
