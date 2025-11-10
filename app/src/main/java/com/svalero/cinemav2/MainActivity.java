package com.svalero.cinemav2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.cinemav2.adapter.MovieAdapter;
import com.svalero.cinemav2.domain.Movie;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MovieAdapter movieAdapter;
//    private List<Movie> movieList;
// Al poner la lista publica y estatica, pueden acceder de todas las activities y hacer lo que quieran
// de esa manera puedo mezclar activitiis, la del mapa y la de regsditro de la moovie
// esto es a falta de BD, y asi simulamos que tenemos datos
    public static List<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Paso 2Aqui llamo al metodo para ver si va bien, con una lista sin api
        populateList();

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

    //Paso 1Pequeña trampa para rellenar datos sin necesidad de API ni de BD, asi se prueban componentes
    private void populateList() {
        movieList = new ArrayList<>();
        movieList.add(new Movie("La Guerra de las Galaxias", "Scifi", 141, 41.651667, -0.8840512, LocalDate.now(), true));
        movieList.add(new Movie("Galactica", "Scifi", 122, 41.651667, -0.8850512, LocalDate.now(), false));
        movieList.add(new Movie("ET El extraterestre", "Scifi", 132, 41.651667, -0.8860512, LocalDate.now(), true));
        movieList.add(new Movie("El Imperio Contraataca", "Scifi", 121, 41.651667, -0.8870512, LocalDate.now(), true));
    }


}