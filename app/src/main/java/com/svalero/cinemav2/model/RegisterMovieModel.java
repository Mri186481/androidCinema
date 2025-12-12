package com.svalero.cinemav2.model;

import android.util.Log;

import com.svalero.cinemav2.api.MoviesApi;
import com.svalero.cinemav2.api.MoviesApiInterface;
import com.svalero.cinemav2.contract.RegisterMovieContract;
import com.svalero.cinemav2.domain.Movie;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterMovieModel implements RegisterMovieContract.Model {
    @Override
    public void registerMovie(Movie movie, OnRegisterMovieListener listener) {
        MoviesApiInterface moviesApi = MoviesApi.buildInstance();
        //Ahora preparo la llamada
        Call<Movie> callRegisterMovie = moviesApi.addMovie(movie);
        //Ahora HAGO la llamada, teclea new dentro del parentesis e intro
        callRegisterMovie.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                switch (response.code()) {
                    case 200:
                        listener.onRegisterMovieSuccess((response.body()));
                        break;
                    case 400:
                        //Hacer aqui lo de extraer los recursos de cadena para los literales
                        listener.onRegisterMovieError("Error validando la peticion" + response.message());
                        break;
                    case 500:
                        listener.onRegisterMovieError("Error interno en la API" +response.message());
                        break;
                    default:
                        listener.onRegisterMovieError("Error invocando a la API" +response.message());
                        break;
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                //Si va mal se ejecutara este otro, que es que me pasan el mensaje de error
                Log.e("API_FAILURE", "Error de la API: " + t.getMessage(), t);
                listener.onRegisterMovieError("No se puede conectar con el origen de los datos " + t.getMessage());
            }
        });

    }
}
