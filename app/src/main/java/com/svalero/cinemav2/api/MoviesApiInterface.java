package com.svalero.cinemav2.api;

import com.svalero.cinemav2.domain.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MoviesApiInterface {

    // son como los query methods del repository de spring-boot pero sin la barra /
    @GET("movies")
    Call<List<Movie>> getMovies();

    @POST("movies")
    Call<Movie> addMovie(@Body Movie movie);



}

