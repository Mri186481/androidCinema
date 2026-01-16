package com.svalero.cinemav2.presenter;


import com.svalero.cinemav2.contract.RegisterMovieContract;
import com.svalero.cinemav2.domain.Movie;
import com.svalero.cinemav2.model.RegisterMovieModel;


public class RegisterMoviePresenter implements RegisterMovieContract.Presenter, RegisterMovieContract.Model.OnRegisterMovieListener {
    private RegisterMovieContract.Model model;
    private RegisterMovieContract.View view;
    public RegisterMoviePresenter(RegisterMovieContract.View view){
        model = new RegisterMovieModel();
        this.view = view;
    }
    @Override
    public void registerMovie(Movie movie) {

        if (movie.getMovieTitle().isEmpty()){
            view.showErrorMessage("El titulo de la pelicula no puede estar vacio");
            return;
        }
        if (movie.getGenre().isEmpty()){
            view.showErrorMessage("El genero de la pelicula no puede estar vacio");
            return;
        }
        if (movie.getDurationMinutes() <= 0){
            view.showErrorMessage("El tiempo de duracion debe de ser al menos 1 minuto");
            return;
        }
        if (movie.getReleaseDate() == null) {
            view.showErrorMessage("La fecha de la pelicula no puede estar vacio");
            return;
        }

        model.registerMovie(movie, this);

    }
    @Override
    public void onRegisterMovieSuccess(Movie registeredMovie) {
        view.showSuccesMessage("Movie registrada correctamente con el identificador " + registeredMovie.getId());
    }

    @Override
    public void onRegisterMovieError(String message) {
        view.showErrorMessage(message);
    }

    @Override
    public void updateMovie(Movie movie) {
        if (movie.getMovieTitle().isEmpty()){
            view.showErrorMessage("El titulo de la pelicula no puede estar vacio");
            return;
        }
        if (movie.getGenre().isEmpty()){
            view.showErrorMessage("El genero de la pelicula no puede estar vacio");
            return;
        }
        if (movie.getDurationMinutes() <= 0){
            view.showErrorMessage("El tiempo de duracion debe de ser al menos 1 minuto");
            return;
        }
        if (movie.getReleaseDate() == null) {
            view.showErrorMessage("La fecha de la pelicula no puede estar vacia");
            return;
        }

        model.updateMovie(movie, this);
    }

    public void onUpdateMovieSuccess(Movie updatedMovie) {
        view.showSuccesMessage("Película actualizada correctamente con el identificador " + updatedMovie.getId());
        ((RegisterMovieContract.View) view).onUpdateMovieSuccess();
    }

    public void onUpdateMovieError(String message) {
        view.showErrorMessage(message);
    }

}
