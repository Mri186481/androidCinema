package com.svalero.cinemav2.contract;

import com.svalero.cinemav2.domain.Movie;

public interface RegisterMovieContract {


    interface Model {
        interface OnRegisterMovieListener{
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
        void showErrorMessage(String message);
        void showSuccesMessage(String message);
        //metodo para manejar el resultado de una actualizacion exitosa
        void onUpdateMovieSuccess();
    }

    interface Presenter {
        void registerMovie(Movie movie);
        void updateMovie(Movie movie);

    }
}
