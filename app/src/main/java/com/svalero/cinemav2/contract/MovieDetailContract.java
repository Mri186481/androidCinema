package com.svalero.cinemav2.contract;

import com.svalero.cinemav2.domain.Movie;

public interface MovieDetailContract {
        interface Model {
            interface OnLoadMovieDetailListener {
                void onLoadMovieDetailSuccess(Movie movie);
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

