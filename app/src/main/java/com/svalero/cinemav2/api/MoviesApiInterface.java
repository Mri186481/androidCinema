package com.svalero.cinemav2.api;

import com.svalero.cinemav2.domain.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MoviesApiInterface {

    // son como los query methods del repository de spring-boot pero sin la barra /
    @GET("movies")
    Call<List<Movie>> getMovies();

    @GET("movies/{id}")
    Call<Movie> getMovie(@Path("id") long id);

    @POST("movies")
    Call<Movie> addMovie(@Body Movie movie);

    @DELETE("movies/{id}")
    Call<Void> deleteMovie(@Path("id") long id);

    //
    @PUT("movies/{id}")
    Call<Movie> putMovie(@Path("id") long movieId, @Body Movie movie);



}
