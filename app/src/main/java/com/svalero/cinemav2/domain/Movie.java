package com.svalero.cinemav2.domain;

import java.time.LocalDate;

public class Movie {

    private String movieTitle;
    private String genre;
    private int durationMinutes;
    private double filmingLatitude;
    private double filmingLongitude;
    private LocalDate releaseDate;
    private boolean currentlyShowing;

    public Movie(String movieTitle, String genre, int durationMinutes, double filmingLatitude, double filmingLongitude, LocalDate releaseDate, boolean currentlyShowing) {
        this.movieTitle = movieTitle;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.filmingLatitude = filmingLatitude;
        this.filmingLongitude = filmingLongitude;
        this.releaseDate = releaseDate;
        this.currentlyShowing = currentlyShowing;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public double getFilmingLatitude() {
        return filmingLatitude;
    }

    public void setFilmingLatitude(double filmingLatitude) {
        this.filmingLatitude = filmingLatitude;
    }

    public double getFilmingLongitude() {
        return filmingLongitude;
    }

    public void setFilmingLongitude(double filmingLongitude) {
        this.filmingLongitude = filmingLongitude;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isCurrentlyShowing() {
        return currentlyShowing;
    }

    public void setCurrentlyShowing(boolean currentlyShowing) {
        this.currentlyShowing = currentlyShowing;
    }
}
