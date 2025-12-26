package com.svalero.cinemav2.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.svalero.cinemav2.R;
import com.svalero.cinemav2.adapter.FavoriteAdapter;
import com.svalero.cinemav2.db.AppDatabase;
import com.svalero.cinemav2.domain.MovieDb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FavoriteListView extends AppCompatActivity {

    private List<MovieDb> favoriteList;
    private FavoriteAdapter favoriteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite_list_view);
        setTitle("Lista de favoritos");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        favoriteList = new ArrayList<>();
        RecyclerView favoritesView = findViewById(R.id.favorites_view);
        favoritesView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((this));
        favoritesView.setLayoutManager(linearLayoutManager);

        favoriteAdapter = new FavoriteAdapter(favoriteList);
        favoritesView.setAdapter(favoriteAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //la Activity viene del segundo plano: Refrescar la lista por si ha habido algún cambio
        favoriteList.clear();
        loadFavorites();
    }
    /*
    Una Mejora Importante: Evitar allowMainThreadQueries()
    Usando .allowMainThreadQueries(), es una mala práctica porque realiza operaciones de base de datos en el hilo principal de la aplicación.
    Si la base de datos crece, la aplicación se congelará o se volverá muy lenta al cargar la lista, provocando una mala experiencia de usuario
    (lo que se conoce como un error ANR o "Application Not Responding").
    La solución es usar el patrón Executor + Handler, que tb voy a utilizar en la vista de detalle. Segun parece es una forma un poco mas correcta de hacerlo en Room.
     */

    private void loadFavorites() {
        // Usamos un Executor para realizar la consulta a la BD en un hilo secundario
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            // Tarea en segundo plano (Background thread)
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "moviedb.db").build();
            List<MovieDb> updatedFavorites = db.movieDbDao().findAll();

            // Tarea en el hilo principal (UI thread) para actualizar la vista
            handler.post(() -> {
                // Limpiamos la lista actual
                favoriteList.clear();
                // Añadimos todos los nuevos datos
                favoriteList.addAll(updatedFavorites);
                // Notificamos al adaptador que los datos han cambiado para que refresque el RecyclerView
                favoriteAdapter.notifyDataSetChanged();
            });
        });
    }
}