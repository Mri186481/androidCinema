package com.svalero.cinemav2.presenter;

import com.svalero.cinemav2.contract.MovieListContract;
import com.svalero.cinemav2.domain.Movie;
import com.svalero.cinemav2.model.MovieListModel;

import java.util.List;

//AQUI EN PRESENTER VA EL INTERMEDIARIO entre model y View
//ademas de su modelo tiene que implementar tambiemn el contratc con el model
//ya que se tiene que comunicar con el
public class MovieListPresenter implements MovieListContract.Presenter, MovieListContract.Model.OnLoadMoviesListener{
    //El presenter como es intermediario tiene que conocer a los dos, tanto el View como el model..
    //Es el unico que tiene acceso a ambos View y Model no de conocen entre si
    private MovieListContract.View view;
    private MovieListContract.Model model;
    //
    public MovieListPresenter(MovieListContract.View view){
        //La vista viene creada por la propia activity aqui se la paso, y se la paso aqui como parametro
        this.view = view;
        //El model como no existe me lo creo
        model = new MovieListModel();
    }

    @Override
    public void loadMovies() {
        //Alguien me pide que le de las pelis.., pues llamoa al model, y se pasa asi mismo, que soy el listener
        //Quien llame a este metodo recibe como parametro el propio presenter
        model.loadMovies(this);
        //y responde el model, en funcion de si ha ido mal o bien, con uno de los dos metodos siguientes
    }

    @Override
    public void onLoadMoviesSuccess(List<Movie> movieList) {
        //Aqui recibe las respuestas, el presenter hace de mensajero, aqui es cuando va bien
        view.listMovies(movieList);
    }

    @Override
    public void onLoadMoviesError(String message) {
        //Aqui cuando va mal
        view.showErrorMessage(message);

    }
}