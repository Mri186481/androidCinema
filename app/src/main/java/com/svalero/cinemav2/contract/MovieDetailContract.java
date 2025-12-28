package com.svalero.cinemav2.contract;

import com.svalero.cinemav2.domain.Movie;

public interface MovieDetailContract {
        interface Model {
            interface OnLoadMovieDetailListener {
                // Si la carga tiene éxito, se recibe el objeto Movie.
                void onLoadMovieDetailSuccess(Movie movie);
                // Si la carga falla, se recibe un mensaje de error.
                void onLoadMovieDetailError(String message);
            }

            void loadMovieDetail(long movieId, OnLoadMovieDetailListener listener);
        }

        interface View {

            void showMovieDetail(Movie movie);

            void showErrorMessage(String message);

            void showSuccessMessage(String message);
        }

        interface Presenter {
            void loadMovieDetail(long movieId);
        }
    }

