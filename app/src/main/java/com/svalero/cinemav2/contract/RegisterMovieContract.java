package com.svalero.cinemav2.contract;

import com.svalero.cinemav2.domain.Movie;

public interface RegisterMovieContract {


    interface Model {
        interface OnRegisterMovieListener{
            //misma lógica
            //on se puede interpretar como cuando, cuando el registro del coche va bien
            void onRegisterMovieSuccess(Movie registeredMovie);
            void onRegisterMovieError(String message);
        }
        void registerMovie(Movie movie, OnRegisterMovieListener listener);


    }

    interface View {
        //void listMovies(List<Movie> movieList);
        void showErrorMessage(String message);
        void showSuccesMessage(String message);



    }

    interface Presenter {
        void registerMovie(Movie movie);

    }
}
