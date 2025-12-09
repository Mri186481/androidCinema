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

//AQUI EN CLASE MODEL VA LA LOGICA y es el que llama a la API, ya que tiene el listener
public class MovieListModel implements MovieListContract.Model {
    @Override
    public void loadMovies(OnLoadMoviesListener listener) {
        MoviesApiInterface moviesApi = MoviesApi.buildInstance();
        //Ahora devuelvo de la api un objeto Call con el contenido de la llamada
        Call<List<Movie>> getMoviesCall = moviesApi.getMovies();
        //Ahora es cuando llamo a la API..
        //El Callback significa que llamo preimero a la api, y el Callback es una llamada de vuelta
        //No sabemos cuanto va a tardar la llamada, pero cuando termine va a llamar a uno de estos dos metodos
        getMoviesCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                //Si va bien la respuesta, se ejecutara este metodo, y en el response tengo los datos
                //Esos datos se los tengo que pasar al presenter, que no esta aqui, esta en forma de listener
                //Con esto devuelvo los datos si ha ido bien la llamada, esta llamda es el model devolviendo los datos al presenter
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
                //Si va mal se ejecutara este otro, que es que me pasan el mensaje de error
                Log.e("API_FAILURE", "Error de la API: " + t.getMessage(), t);
                listener.onLoadMoviesError(t.getMessage());
            }
        });


    }

}
