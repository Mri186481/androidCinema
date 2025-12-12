package com.svalero.cinemav2.presenter;

import com.svalero.cinemav2.contract.RegisterMovieContract;
import com.svalero.cinemav2.domain.Movie;
import com.svalero.cinemav2.model.RegisterMovieModel;


public class RegisterMoviePresenter implements RegisterMovieContract.Presenter, RegisterMovieContract.Model.OnRegisterMovieListener {
    //Estoy en el presenter.. debo definir el model y el view
    private RegisterMovieContract.Model model;
    private RegisterMovieContract.View view;
    //Ahora el costructor..
    public RegisterMoviePresenter(RegisterMovieContract.View view){
        model = new RegisterMovieModel();
        this.view = view;

    }

    @Override
    public void registerMovie(Movie movie) {
        //Aqui puedo comprobar los campos, no en la vista
        if (movie.getMovieTitle().isEmpty()){
            view.showErrorMessage("El titulo de la pelicula no puede estar vacio");
            return;
        }

        model.registerMovie(movie, this);

    }

    @Override
    public void onRegisterMovieSuccess(Movie registeredMovie) {
        view.showSuccesMessage("Movie registrada correctamente con el identificador" + registeredMovie.getId());
    }

    @Override
    public void onRegisterMovieError(String message) {
        view.showErrorMessage(message);
    }
}
