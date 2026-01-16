package com.svalero.cinemav2.domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ScreeningIn implements Parcelable {
    private Long id;
    private String screeningTime;
    private Double ticketPrice;
    private boolean subtitled;
    private Long movieId;
    private Long roomId;

    // --- Constructor ---
    public ScreeningIn(String screeningTime, Double ticketPrice, boolean subtitled, Long movieId, Long roomId) {
        this.screeningTime = screeningTime;
        this.ticketPrice = ticketPrice;
        this.subtitled = subtitled;
        this.movieId = movieId;
        this.roomId = roomId;
    }

    // --- Constructor para Parcelable ---
    protected ScreeningIn(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        screeningTime = in.readString();
        if (in.readByte() == 0) {
            ticketPrice = null;
        } else {
            ticketPrice = in.readDouble();
        }
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
    }

    // --- Creator para Parcelable ---
    public static final Creator<ScreeningIn> CREATOR = new Creator<ScreeningIn>() {
        @Override
        public ScreeningIn createFromParcel(Parcel in) {
            return new ScreeningIn(in);
        }

        @Override
        public ScreeningIn[] newArray(int size) {
            return new ScreeningIn[size];
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

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public boolean isSubtitled() {
        return subtitled;
    }

    public void setSubtitled(boolean subtitled) {
        this.subtitled = subtitled;
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
        if (ticketPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(ticketPrice);
        }
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
    }
}
