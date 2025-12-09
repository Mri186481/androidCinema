package com.svalero.cinemav2.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.cinemav2.MapActivity;
import com.svalero.cinemav2.R;
import com.svalero.cinemav2.RegisterMovie;
import com.svalero.cinemav2.adapter.MovieAdapter;
import com.svalero.cinemav2.contract.MovieListContract;
import com.svalero.cinemav2.domain.Movie;
import com.svalero.cinemav2.presenter.MovieListPresenter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class MovieListView extends AppCompatActivity implements MovieListContract.View {

    private MovieAdapter movieAdapter;
    //Creo una movilist pero privada ya no hace falta publica y estatica
    private List<Movie> movieList;

    //La view si que habla con el presenter aqui tengo que definir el presenter
    private MovieListContract.Presenter presenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Lo primero que hago es hablar con el presenter y me paso a mi mismo que soy la view
        presenter = new MovieListPresenter(this);
        //Cuando he llamado a este costructor le he pasado la view y estoy creando el model


        //Ahora le digo al presenter que carge las peliculas, el presenter llamara al model,
        //el model llamara a la API, recibira los datos, y en base a ir bien o no, llamara
        //al listener que es el presenter, que acabara llamando a la view, esta separado
        presenter.loadMovies();
        // Ahora  intancio la lista
        movieList = new ArrayList<>();

        RecyclerView moviesView = findViewById(R.id.movies_view);
        moviesView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((this));
        moviesView.setLayoutManager(linearLayoutManager);

        movieAdapter = new MovieAdapter(movieList);
        moviesView.setAdapter(movieAdapter);
    }
    //Este metodo lo genero para la action bar a traves de generate override metthods y busco OnCreateOptions,
    //luego cambio la linea para inflar/generar la action_bar y devuelvo true para que el menu se pinte o presente
    //Con esto el menu aparece pero no hace nada de momento..
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }
    // Ahora voy otra vez y genero otro metodo .... busco ---> OnOptionsItemSelected   precisamente para hacer algo con los botones


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Aqui programo algo, como solo hay dos programo con un if
        if (item.getItemId() == R.id.action_map) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_register_movie) {
            Intent intent = new Intent(this, RegisterMovie.class);
            startActivity(intent);
            //Con esto inicio la otra activity en el metodo oncreate
        }
        //En cualquier caso si he gestionado el caso devuelvo un true
        return true;

    }

    public void registerMovie(View view) {
        Intent intent = new Intent(this, RegisterMovie.class);
        startActivity(intent);
    }



    @Override
    public void listMovies(List<Movie> movieList) {
        //en mi lista de aqui privada añado lo que viene ahora que viene del model
        this.movieList.addAll(movieList);
        //le paso la lista a mi adapter para que la pinte
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();

    }

    @Override
    public void showSuccesMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
