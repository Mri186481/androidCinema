package com.svalero.cinemav2.model;

import android.util.Log;

import com.svalero.cinemav2.api.MoviesApi;
import com.svalero.cinemav2.api.MoviesApiInterface;
import com.svalero.cinemav2.contract.MovieListContract;
import com.svalero.cinemav2.domain.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieListModel implements MovieListContract.Model {
    @Override
    public void loadMovies(OnLoadMoviesListener listener) {
        MoviesApiInterface moviesApi = MoviesApi.buildInstance();
        Call<List<Movie>> getMoviesCall = moviesApi.getMovies();
        getMoviesCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.code() == 200) {
                    listener.onLoadMoviesSuccess(response.body());
                } else if (response.code() == 500) {
                    listener.onLoadMoviesError("La API no se encuentra disponible en este momento. Prueba de nuevo");
                } else {
                    listener.onLoadMoviesError(String.valueOf(response.code()));
                }
            }
            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e("API_FAILURE", "Error de la API: " + t.getMessage(), t);
                listener.onLoadMoviesError(t.getMessage());
            }
        });


    }

}
