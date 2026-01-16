package com.svalero.cinemav2.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MovieDb {
    @PrimaryKey(autoGenerate = true)
    private Long idDb;
    @ColumnInfo
    private String movieTitleDb;
    @ColumnInfo
    private String genreDb;
    @ColumnInfo
    private int durationMinutesDb;
    @ColumnInfo
    private double filmingLatitudeDb;
    @ColumnInfo
    private double filmingLongitudeDb;
    @ColumnInfo
    private String releaseDateDb;
    @ColumnInfo
    private boolean currentlyShowingDb;
    @ColumnInfo
    private String movieImageDb;


    // Constructor sin parámetros (¡MUY IMPORTANTE para Room!)
    public MovieDb() {
    }

    public MovieDb(String movieTitleDb, String genreDb, int durationMinutesDb, double filmingLatitudeDb, double filmingLongitudeDb, String releaseDateDb, boolean currentlyShowingDb, String movieImageDb){
        this.movieTitleDb = movieTitleDb;
        this.genreDb = genreDb;
        this.durationMinutesDb = durationMinutesDb;
        this.filmingLatitudeDb = filmingLatitudeDb;
        this.filmingLongitudeDb = filmingLongitudeDb;
        this.releaseDateDb = releaseDateDb;
        this.currentlyShowingDb = currentlyShowingDb;
        this.movieImageDb = movieImageDb;
    }

    public Long getIdDb() {
        return idDb;
    }

    public void setIdDb(Long idDb) {
        this.idDb = idDb;
    }
    public String getMovieTitleDb() {
        return movieTitleDb;
    }

    public void setMovieTitleDb(String movieTitleDb) {
        this.movieTitleDb = movieTitleDb;
    }

    public String getGenreDb() {
        return genreDb;
    }

    public void setGenreDb(String genreDb) {
        this.genreDb = genreDb;
    }

    public int getDurationMinutesDb() {
        return durationMinutesDb;
    }

    public void setDurationMinutesDb(int durationMinutesDb) {
        this.durationMinutesDb = durationMinutesDb;
    }

    public double getFilmingLatitudeDb() {
        return filmingLatitudeDb;
    }

    public void setFilmingLatitudeDb(double filmingLatitudeDb) {
        this.filmingLatitudeDb = filmingLatitudeDb;
    }

    public double getFilmingLongitudeDb() {
        return filmingLongitudeDb;
    }

    public void setFilmingLongitudeDb(double filmingLongitudeDb) {
        this.filmingLongitudeDb = filmingLongitudeDb;
    }

    public String getReleaseDateDb() {
        return releaseDateDb;
    }

    public void setReleaseDateDb(String releaseDateDb) {
        this.releaseDateDb = releaseDateDb;
    }

    public boolean isCurrentlyShowingDb() {
        return currentlyShowingDb;
    }

    public void setCurrentlyShowingDb(boolean currentlyShowingDb) {
        this.currentlyShowingDb = currentlyShowingDb;
    }

    public String getMovieImageDb() {
        return movieImageDb;
    }

    public void setMovieImageDb(String movieImageDb) {
        this.movieImageDb = movieImageDb;
    }


}
