package com.svalero.cinemav2.model;

import com.svalero.cinemav2.api.MoviesApi;
import com.svalero.cinemav2.api.MoviesApiInterface;
import com.svalero.cinemav2.contract.MovieDetailContract;
import com.svalero.cinemav2.domain.Movie;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//AQUI EN CLASE MODEL VA LA LOGICA y es el que llama a la API, ya que tiene el listener
public class MovieDetailModel implements MovieDetailContract.Model {

    @Override
    public void loadMovieDetail(long movieId, OnLoadMovieDetailListener listener) {
        // Obtenemos una instancia de la interfaz de la API
        MoviesApiInterface apiInterface = MoviesApi.buildInstance();

        // Creamos la llamada a la API para obtener los detalles de la película por ID
        Call<Movie> callMovie = apiInterface.getMovie(movieId);

        // Hacemos la llamada de forma asíncrona
        callMovie.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                // Verificamos si la respuesta de la API es exitosa
                if (response.isSuccessful()) {
                    Movie movie = response.body();
                    if (movie != null) {
                        // Si la respuesta es exitosa y el cuerpo no es nulo,
                        // notificamos al Presenter con el objeto Movie
                        listener.onLoadMovieDetailSuccess(movie);
                    } else {
                        // Si el cuerpo es nulo, significa que la película no se encontró
                        listener.onLoadMovieDetailError("Película no encontrada.");
                    }
                } else {
                    // Si la respuesta no es exitosa, notificamos al Presenter
                    String errorMessage = "Error en la respuesta de la API: " + response.code();
                    listener.onLoadMovieDetailError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                // Si la llamada falla (ej. sin conexión a Internet), notificamos al Presenter
                String errorMessage = "Error al conectar con la API: " + t.getMessage();
                listener.onLoadMovieDetailError(errorMessage);
            }
        });
    }
}
