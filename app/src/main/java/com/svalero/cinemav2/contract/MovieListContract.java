package com.svalero.cinemav2.contract;

import com.svalero.cinemav2.domain.Movie;

import java.util.List;

public interface MovieListContract {
    //FUNCIONAMIENTO
    //recuerda que el presenter es el que esta enmedio, desde view(user actions)
    //le digo que carge los coches a presenter, presenter se comunica con model y luego, una vez tiene
    //los datos, presenter se vuelve a comunicar con view para que los pinte en view
    //este contrato es el que tinen IMPLEMENTAR las clases model presenter y vuew

    interface Model {
        interface OnLoadMoviesListener{
            //Si va bien este metodo(Succes) recibira la lista de movies
            void onLoadMoviesSuccess(List<Movie> movieList);
            //Si va makl este fallara y devovera un mensaje de error
            void onLoadMoviesError(String message);
        }

        void loadMovies(OnLoadMoviesListener listener);
        //esto es la asincronia, el listener va a ser la clase que va llamar en segundo plano a la api
        //y cuando la api falle o no, va a llamar a onLoadMoviesSuccess o a onLoadMoviesError

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
