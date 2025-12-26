package com.svalero.cinemav2.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.svalero.cinemav2.dao.MovieDbDao;
import com.svalero.cinemav2.domain.MovieDb;


@Database(entities = {MovieDb.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MovieDbDao movieDbDao();
}
