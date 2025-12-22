package com.svalero.cinemav2.contract;

import com.svalero.cinemav2.domain.Movie;

public interface RegisterMovieContract {


    interface Model {
        interface OnRegisterMovieListener{
            //misma lógica
            //on se puede interpretar como cuando, cuando el registro del coche va bien
            //metodos para registro
            void onRegisterMovieSuccess(Movie registeredMovie);
            void onRegisterMovieError(String message);
            //Metodos actualizacion
            void onUpdateMovieSuccess(Movie updatedMovie);
            void onUpdateMovieError(String message);
        }
        void registerMovie(Movie movie, OnRegisterMovieListener listener);
        void updateMovie(Movie movie, OnRegisterMovieListener listener);


    }

    interface View {
        //void listMovies(List<Movie> movieList);
        void showErrorMessage(String message);
        void showSuccesMessage(String message);
        //metodo para manejar el resultado de una actualizacion exitosa
        void onUpdateMovieSuccess();



    }

    interface Presenter {
        void registerMovie(Movie movie);
        // Nuevo método para actualizar una película
        void updateMovie(Movie movie);

    }
}
