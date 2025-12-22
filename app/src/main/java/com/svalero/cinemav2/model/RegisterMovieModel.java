package com.svalero.cinemav2.model;

import android.util.Log;

import com.svalero.cinemav2.api.MoviesApi;
import com.svalero.cinemav2.api.MoviesApiInterface;
import com.svalero.cinemav2.contract.RegisterMovieContract;
import com.svalero.cinemav2.domain.Movie;

import java.util.List;

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

    @Override
    public void updateMovie(Movie movie, OnRegisterMovieListener listener) {
        MoviesApiInterface moviesApi = MoviesApi.buildInstance();
        // Llamamos al método de actualización en la API
        Call<Movie> callUpdateMovie = moviesApi.putMovie(movie.getId(), movie);

        callUpdateMovie.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                switch (response.code()) {
                    case 200:
                    case 204: // 204 No Content es una respuesta común para actualizaciones exitosas
                        // Llama al método del listener para indicar que la actualización fue exitosa
                        listener.onUpdateMovieSuccess(response.body());
                        break;
                    case 400:
                        listener.onUpdateMovieError("Error validando la petición de actualización: " + response.message());
                        break;
                    case 404:
                        listener.onUpdateMovieError("Película no encontrada. No se pudo actualizar.");
                        break;
                    case 500:
                        listener.onUpdateMovieError("Error interno en la API al actualizar: " + response.message());
                        break;
                    default:
                        listener.onUpdateMovieError("Error al actualizar la película: " + response.message());
                        break;
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("API_FAILURE", "Error de la API al actualizar: " + t.getMessage(), t);
                listener.onUpdateMovieError("No se puede conectar con el origen de los datos " + t.getMessage());
            }
        });
    }
}
