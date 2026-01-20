package com.svalero.cinemav2.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
        setTitle(getString(R.string.tl_lista_favoritos));
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
        favoriteList.clear();
        loadFavorites();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar2, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_list_movies2) {
            Intent intent = new Intent(this, MovieListView.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_list_screenings2) {
            Intent intent = new Intent(this, ScreeningListView.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_preferences2){
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_favorites2) {
            Intent intent = new Intent(this, FavoriteListView.class);
            startActivity(intent);
        }

        return true;

    }

    private void loadFavorites() {
        //Se realiza la consulta a la BD en un hilo secundario
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "moviedb.db").build();
            List<MovieDb> updatedFavorites = db.movieDbDao().findAll();
            // Tarea en el hilo principal (UI thread) para actualizar la vista
            handler.post(() -> {
                favoriteList.clear();
                favoriteList.addAll(updatedFavorites);
                favoriteAdapter.notifyDataSetChanged();
            });
        });
    }
}