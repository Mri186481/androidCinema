package com.svalero.cinemav2.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.svalero.cinemav2.R;

public class MovieDetailActivityView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_view);

        Intent intent = getIntent();
        Long movieId = intent.getLongExtra("movieId", 0);


        printMovie(movieId);

    }

    private void printMovie(Long movieId) {
        ((TextView) findViewById(R.id.det_movie_id)).setText(String.valueOf(movieId));



    }
}

// Ahora aqui llamaria a la api y visualizaria el coche, debere hacer un
// model/view/presenter  para que pueda cargar la ficha igual que hizo la
//MovieListView pero utilizando otros contratos y otro acceso a la API
//Hago un movies/{moviesId} de la API
//Y en vez de pasar una id para paso un objeto completo para pintarlo
//Y aqui hacemos un boton de editar y un boton de borrar abajo y un boton de sesiones
//que dara a un listado de sesiones de la pelicula y desde alli darla de baja o modificarla
//en la pantalla principal hacemos tbn un boton de sesiones,  nos llevara a una pantalla de sesiones
//general, podremos clicar en la que sea y verla y aya estaria. Esto tampoco hace falta
//Aqui tambien implementare un diaologo a la hora de confirmar de borrar
//o alguna opcion critica de la APP como modificar tb, eso es otro punto de la AA
//LUEGO HAREMOS UN MENU DE PREFERENCIAS, ya con eso hay 5 ACtivities
//Faltara implementar screeening y salen un monton, pero screeening no llevan mapa
//Explorar el tema de imagenes, hacer fotos y manejar galeria