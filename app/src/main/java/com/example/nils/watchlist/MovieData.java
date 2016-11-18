package com.example.nils.watchlist;

import java.io.Serializable;

public class MovieData implements Serializable {

    // possible additions: "Director", "Writer", "Actors"

    public String title;
    public String year;
    public String imdbID;
    public String jsonobject;

    public String poster;
    public String runTime;
    public String genre;
    public String plot;
    public String language;

    public String awards;
    public String metaScore;
    public String imdbRating;
    public String imdbVotes;

    // constructor
    public MovieData(String title, String year, String imdbID, String jsonobject) {
        this.title = title;
        this.year = year;
        this.imdbID = imdbID;
        this.jsonobject = jsonobject;

        this.poster = "";
        this.runTime = "";
        this.genre = "";
        this.plot = "";
        this.language = "";

        this.awards = "";
        this.metaScore = "";
        this.imdbRating = "";
        this.imdbVotes = "";
    }

    public String toString() {
        return title + " (" + year + ")";
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public void setMetaScore(String metaScore) {
        this.metaScore = metaScore;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public void setJsonObject(String jsonobject) {
        this.jsonobject = jsonobject;
    }

}
