package com.svalero.cinemav2.presenter;

import com.svalero.cinemav2.contract.MovieListContract;
import com.svalero.cinemav2.domain.Movie;
import com.svalero.cinemav2.model.MovieListModel;

import java.util.List;

public class MovieListPresenter implements MovieListContract.Presenter, MovieListContract.Model.OnLoadMoviesListener{
    private MovieListContract.View view;
    private MovieListContract.Model model;
    //
    public MovieListPresenter(MovieListContract.View view){
        this.view = view;
        model = new MovieListModel();
    }

    @Override
    public void loadMovies() {
        model.loadMovies(this);
    }

    @Override
    public void onLoadMoviesSuccess(List<Movie> movieList) {
        view.listMovies(movieList);
    }

    @Override
    public void onLoadMoviesError(String message) {
        view.showErrorMessage(message);
    }
}
