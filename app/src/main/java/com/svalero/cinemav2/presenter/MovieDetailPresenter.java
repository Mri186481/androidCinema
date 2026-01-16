package com.svalero.cinemav2.presenter;

import com.svalero.cinemav2.contract.MovieDetailContract;
import com.svalero.cinemav2.domain.Movie;

public class MovieDetailPresenter implements MovieDetailContract.Presenter, MovieDetailContract.Model.OnLoadMovieDetailListener {

    private MovieDetailContract.View view;
    private MovieDetailContract.Model model;

    public MovieDetailPresenter(MovieDetailContract.View view, MovieDetailContract.Model model) {
        this.view = view;
        this.model = model;
    }
    @Override
    public void loadMovieDetail(long movieId) {
        model.loadMovieDetail(movieId, this);
    }

    @Override
    public void onLoadMovieDetailSuccess(Movie movie) {
        view.showMovieDetail(movie);
        view.showSuccessMessage("Detalles de la película cargados correctamente.");
    }

    @Override
    public void onLoadMovieDetailError(String message) {
        view.showErrorMessage(message);
    }
}

