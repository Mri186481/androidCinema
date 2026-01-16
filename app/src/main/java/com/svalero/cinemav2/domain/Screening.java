package com.svalero.cinemav2.domain; // Asegúrate de que el package sea el correcto

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Screening implements Parcelable {

    private Long id;
    private String screeningTime;
    private double ticketPrice;
    private boolean subtitled;
    private Long movieId;
    private Long roomId;
    private String movieTitle;
    private String roomName;

    // Constructor para cuando se crea un nuevo Screening en la app (sin id)
    public Screening(String screeningTime, double ticketPrice, boolean subtitled, Long movieId, Long roomId, String movieTitle, String roomName) {
        this.screeningTime = screeningTime;
        this.ticketPrice = ticketPrice;
        this.subtitled = subtitled;
        this.movieId = movieId;
        this.roomId = roomId;
        this.movieTitle = movieTitle;
        this.roomName = roomName;
    }

    //
    protected Screening(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        screeningTime = in.readString();
        ticketPrice = in.readDouble();
        subtitled = in.readByte() != 0;
        if (in.readByte() == 0) {
            movieId = null;
        } else {
            movieId = in.readLong();
        }
        if (in.readByte() == 0) {
            roomId = null;
        } else {
            roomId = in.readLong();
        }
        movieTitle = in.readString();
        roomName = in.readString();
    }

    //
    public static final Creator<Screening> CREATOR = new Creator<Screening>() {
        @Override
        public Screening createFromParcel(Parcel in) {
            return new Screening(in);
        }

        @Override
        public Screening[] newArray(int size) {
            return new Screening[size];
        }
    };

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScreeningTime() {
        return screeningTime;
    }

    public void setScreeningTime(String screeningTime) {
        this.screeningTime = screeningTime;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public boolean isSubtitled() {
        return subtitled;
    }

    public void setSubtitled(boolean subtitled) {
        this.subtitled = subtitled;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }


    // --- Métodos de la Interfaz Parcelable ---

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
        dest.writeString(screeningTime);
        dest.writeDouble(ticketPrice);
        dest.writeByte((byte) (subtitled ? 1 : 0));

        if (movieId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(movieId);
        }
        if (roomId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(roomId);
        }
        dest.writeString(movieTitle);
        dest.writeString(roomName);
    }
}
