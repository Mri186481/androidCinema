package com.svalero.cinemav2.contract;

import com.svalero.cinemav2.domain.Movie;

import java.util.List;

public interface MovieListContract {

    interface Model {
        interface OnLoadMoviesListener{
            void onLoadMoviesSuccess(List<Movie> movieList);
            void onLoadMoviesError(String message);
        }
        void loadMovies(OnLoadMoviesListener listener);
    }

    interface View {
        void listMovies(List<Movie> movieList);
        void showErrorMessage(String message);
        void showSuccesMessage(String message);
    }

    interface Presenter {
        void loadMovies();
    }
}
