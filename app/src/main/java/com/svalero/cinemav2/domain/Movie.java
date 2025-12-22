package com.svalero.cinemav2.domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.Date;
//En principio, de serie, parece que retrofit no pueda trabajar con Localdate
//Se arregla bien con date, pero hay que hacer unas perqueñas conversiones
//y decirle a retrfit en la definicion de la api, como nos van a venir los datos
public class Movie implements Parcelable {
    private Long id;
    private String movieTitle;
    private String genre;
    private int durationMinutes;
    private double filmingLatitude;
    private double filmingLongitude;
    private Date releaseDate;
    private boolean currentlyShowing;

    public Movie(String movieTitle,String genre, int durationMinutes, double filmingLatitude, double filmingLongitude, Date releaseDate, boolean currentlyShowing){
        this.movieTitle = movieTitle;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.filmingLatitude = filmingLatitude;
        this.filmingLongitude = filmingLongitude;
        this.releaseDate = releaseDate;
        this.currentlyShowing = currentlyShowing;
    }


    protected Movie(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        movieTitle = in.readString();
        genre = in.readString();
        durationMinutes = in.readInt();
        filmingLatitude = in.readDouble();
        filmingLongitude = in.readDouble();
        // Lee el long de la fecha y si no es -1L, crea un nuevo objeto Date
        long releaseDateMillis = in.readLong();
        releaseDate = releaseDateMillis == -1L ? null : new Date(releaseDateMillis);
        currentlyShowing = in.readByte() != 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isCurrentlyShowing() {
        return currentlyShowing;
    }

    public void setCurrentlyShowing(boolean currentlyShowing) {
        this.currentlyShowing = currentlyShowing;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(movieTitle);
        dest.writeString(genre);
        dest.writeInt(durationMinutes);
        dest.writeDouble(filmingLatitude);
        dest.writeDouble(filmingLongitude);
        // Escribe la fecha como un long (milisegundos) o -1L si es nula
        dest.writeLong(releaseDate != null ? releaseDate.getTime() : -1L);
        dest.writeByte((byte) (currentlyShowing ? 1 : 0));
    }
}