package com.svalero.cinemav2.domain;

import java.time.LocalDate;

public class Movie {
    private Long id;
    private String movieTitle;
    private String genre;
    private int durationMinutes;
    private double filmingLatitude;
    private double filmingLongitude;
    private String releaseDate;
    private boolean currentlyShowing;

    public Movie(Long id, String movieTitle, String genre, int durationMinutes, double filmingLatitude, double filmingLongitude, String releaseDate, boolean currentlyShowing) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.filmingLatitude = filmingLatitude;
        this.filmingLongitude = filmingLongitude;
        this.releaseDate = releaseDate;
        this.currentlyShowing = currentlyShowing;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isCurrentlyShowing() {
        return currentlyShowing;
    }

    public void setCurrentlyShowing(boolean currentlyShowing) {
        this.currentlyShowing = currentlyShowing;
    }
}
