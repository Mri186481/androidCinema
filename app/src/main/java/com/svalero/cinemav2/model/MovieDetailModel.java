package com.svalero.cinemav2.model;

import com.svalero.cinemav2.api.MoviesApi;
import com.svalero.cinemav2.api.MoviesApiInterface;
import com.svalero.cinemav2.contract.MovieDetailContract;
import com.svalero.cinemav2.domain.Movie;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailModel implements MovieDetailContract.Model {

    @Override
    public void loadMovieDetail(long movieId, OnLoadMovieDetailListener listener) {
        MoviesApiInterface apiInterface = MoviesApi.buildInstance();
        Call<Movie> callMovie = apiInterface.getMovie(movieId);

        callMovie.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Movie movie = response.body();
                    if (movie != null) {
                        listener.onLoadMovieDetailSuccess(movie);
                    } else {
                        listener.onLoadMovieDetailError("Película no encontrada.");
                    }
                } else {
                    String errorMessage = "Error en la respuesta de la API: " + response.code();
                    listener.onLoadMovieDetailError(errorMessage);
                }
            }
            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                String errorMessage = "Error al conectar con la API: " + t.getMessage();
                listener.onLoadMovieDetailError(errorMessage);
            }
        });
    }
}
