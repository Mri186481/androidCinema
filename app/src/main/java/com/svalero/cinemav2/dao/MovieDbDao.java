package com.svalero.cinemav2.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.cinemav2.domain.MovieDb;

import java.util.List;

@Dao
public interface MovieDbDao {
    @Query("SELECT * FROM MovieDb")
    public List<MovieDb> findAll();
    @Query("SELECT * FROM MovieDb WHERE idDb = :idDb")
    public MovieDb findById(Long idDb);
    @Insert
    void addMovieDb (MovieDb movieDb);
    @Delete
    void deleteMovieDb (MovieDb movieDb);
    @Update
    void updateMovieDb(MovieDb movieDb);
}
