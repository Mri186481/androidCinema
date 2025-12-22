package com.svalero.cinemav2.presenter;

import com.svalero.cinemav2.contract.MovieDetailContract;
import com.svalero.cinemav2.domain.Movie;

//AQUI EN PRESENTER VA EL INTERMEDIARIO entre model y View
//ademas de su modelo tiene que implementar tambiemn el contratc con el model
//ya que se tiene que comunicar con el
public class MovieDetailPresenter implements MovieDetailContract.Presenter, MovieDetailContract.Model.OnLoadMovieDetailListener {

    private MovieDetailContract.View view;
    private MovieDetailContract.Model model;

    /**
     * Constructor del presentador.
     * @param view La instancia de la Vista (Activity o Fragment) que se va a gestionar.
     * @param model La instancia del Modelo que gestiona la lógica de datos.
     */
    public MovieDetailPresenter(MovieDetailContract.View view, MovieDetailContract.Model model) {
        this.view = view;
        this.model = model;
    }

    /**
     * Le indica al Presentador que debe iniciar el proceso de carga
     * de los detalles de una película.
     * @param movieId El ID (tipo long) de la película a cargar.
     */
    @Override
    public void loadMovieDetail(long movieId) {
        // Le pedimos al modelo que cargue los detalles de la película,
        // pasándole a sí mismo como listener para recibir la respuesta.
        model.loadMovieDetail(movieId, this);
    }

    /**
     * Callback del Modelo cuando la carga de datos es exitosa.
     * @param movie El objeto Movie cargado.
     */
    @Override
    public void onLoadMovieDetailSuccess(Movie movie) {
        // Si la carga fue exitosa, le decimos a la Vista que muestre los detalles.
        view.showMovieDetail(movie);
        view.showSuccessMessage("Detalles de la película cargados correctamente.");
    }

    /**
     * Callback del Modelo cuando la carga de datos falla.
     * @param message El mensaje de error.
     */
    @Override
    public void onLoadMovieDetailError(String message) {
        // Si la carga falló, le decimos a la Vista que muestre un mensaje de error.
        view.showErrorMessage(message);
    }
}

