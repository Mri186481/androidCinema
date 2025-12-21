package com.svalero.cinemav2.contract;

import com.svalero.cinemav2.domain.Movie;

public interface MovieDetailContract {
        interface Model {
            /**
             * Listener para notificar al Presenter el resultado de la carga de datos.
             */
            interface OnLoadMovieDetailListener {
                // Si la carga tiene éxito, se recibe el objeto Movie.
                void onLoadMovieDetailSuccess(Movie movie);
                // Si la carga falla, se recibe un mensaje de error.
                void onLoadMovieDetailError(String message);
            }

            /**
             * Método que inicia la carga de los detalles de una película específica.
             * @param movieId El ID (tipo long) de la película que se quiere cargar.
             * @param listener El listener que será notificado cuando la operación termine.
             */
            void loadMovieDetail(long movieId, OnLoadMovieDetailListener listener);
        }

        interface View {
            /**
             * Muestra los detalles de la película en la interfaz de usuario.
             * @param movie El objeto Movie con todos los datos a mostrar.
             */
            void showMovieDetail(Movie movie);

            /**
             * Muestra un mensaje de error al usuario (ej. un Toast o un Snackbar).
             * @param message El mensaje de error a mostrar.
             */
            void showErrorMessage(String message);

            /**
             * Muestra un mensaje de éxito o informativo al usuario.
             * @param message El mensaje a mostrar.
             */
            void showSuccessMessage(String message);
        }

        interface Presenter {
            /**
             * Le indica al Presentador que debe iniciar el proceso de carga
             * de los detalles de una película.
             * @param movieId El ID (tipo long) de la película a cargar.
             */
            void loadMovieDetail(long movieId);
        }
    }

