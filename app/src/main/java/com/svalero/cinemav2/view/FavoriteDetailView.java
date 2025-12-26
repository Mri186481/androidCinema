package com.svalero.cinemav2.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.svalero.cinemav2.R;
import com.svalero.cinemav2.db.AppDatabase;
import com.svalero.cinemav2.domain.MovieDb;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FavoriteDetailView extends AppCompatActivity {
    private MovieDb movieDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite_detail_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        Long IdDBFavorite = intent.getLongExtra("IdBd", -1);
        // Llamamos al presentador para que inicie la carga de datos


        if (IdDBFavorite != -1) {
            // Si la ID es válida, realizamos la consulta en un hilo secundario
            Executor executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {
                // La consulta a la BD se hace en un hilo secundario
                AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "moviedb.db").build();
                this.movieDb = db.movieDbDao().findById(IdDBFavorite);

                // Los resultados se procesan en el hilo principal
                handler.post(() -> {
                    if (this.movieDb != null) {
                        fillData(this.movieDb);
                    } else {
                        Toast.makeText(this, "La película no se encontró en la BD.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        } else {
            // Si la ID es inválida, mostramos el Toast directamente
            Toast.makeText(this, "ID de película no válido en la BD.", Toast.LENGTH_SHORT).show();
        }


    }
    private void fillData(MovieDb movieDb) {
        EditText movieTitleEditText = findViewById(R.id.det_bd_movie_title);
        movieTitleEditText.setText(movieDb.getMovieTitleDb());

        EditText genreEditText = findViewById(R.id.det_bd_genre);
        genreEditText.setText(movieDb.getGenreDb());

        EditText durationMinutesEditText = findViewById(R.id.det_bd_duration_minutes);
        durationMinutesEditText.setText(String.valueOf(movieDb.getDurationMinutesDb()));

        EditText releaseDateEditText = findViewById(R.id.det_bd_release_date);
        releaseDateEditText.setText(movieDb.getReleaseDateDb());

        CheckBox currentlyShowingCheckBox = findViewById(R.id.det_bd_currently_showing);
        currentlyShowingCheckBox.setChecked(movieDb.isCurrentlyShowingDb());
    }

    public void updateBdFavorite(View view) {
        if (this.movieDb == null) {
            Toast.makeText(this, "No hay película cargada para actualizar.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Recoger los datos de los campos de la interfaz
        EditText movieTitleEditText = findViewById(R.id.det_bd_movie_title);
        EditText genreEditText = findViewById(R.id.det_bd_genre);
        EditText durationMinutesEditText = findViewById(R.id.det_bd_duration_minutes);
        EditText releaseDateEditText = findViewById(R.id.det_bd_release_date);
        CheckBox currentlyShowingCheckBox = findViewById(R.id.det_bd_currently_showing);

        // 2. Actualizar el objeto movieDb con los nuevos datos
        this.movieDb.setMovieTitleDb(movieTitleEditText.getText().toString());
        this.movieDb.setGenreDb(genreEditText.getText().toString());
        try {
            this.movieDb.setDurationMinutesDb(Integer.parseInt(durationMinutesEditText.getText().toString()));
        } catch (NumberFormatException nfe) {
            Toast.makeText(this, "La duración debe ser un número válido.", Toast.LENGTH_SHORT).show();
            return; // Detenemos la ejecución si el número no es válido
        }
        this.movieDb.setReleaseDateDb(releaseDateEditText.getText().toString());
        this.movieDb.setCurrentlyShowingDb(currentlyShowingCheckBox.isChecked());

        // 3. Ejecutar la actualización en un hilo secundario
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "moviedb.db").build();
            db.movieDbDao().updateMovieDb(this.movieDb);

            handler.post(() -> {
                Toast.makeText(this, "Película actualizada correctamente.", Toast.LENGTH_SHORT).show();
            });
        });
    }

    /**
     * Método para BORRAR la película de la base de datos.
     * Este método se enlaza a un botón mediante el atributo android:onClick="deleteBdFavorite" en el XML.
     */
    public void deleteBdFavorite(View view) {
        if (this.movieDb == null) {
            Toast.makeText(this, "No hay película cargada para eliminar.", Toast.LENGTH_SHORT).show();
            return;
        }

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "moviedb.db").build();
            db.movieDbDao().deleteMovieDb(this.movieDb);

            handler.post(() -> {
                Toast.makeText(this, "Película eliminada correctamente.", Toast.LENGTH_SHORT).show();
                finish(); // Cierra la Activity y vuelve a la anterior
            });
        });
    }


}