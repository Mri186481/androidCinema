package com.svalero.cinemav2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.cinemav2.R;
import com.svalero.cinemav2.adapter.MovieAdapter;
import com.svalero.cinemav2.contract.MovieListContract;
import com.svalero.cinemav2.domain.Movie;
import com.svalero.cinemav2.presenter.MovieListPresenter;

import java.util.ArrayList;
import java.util.List;


public class MovieListView extends AppCompatActivity implements MovieListContract.View {

    private MovieAdapter movieAdapter;
    private List<Movie> movieList;

    private MovieListContract.Presenter presenter;
    private SharedPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Movies");

        presenter = new MovieListPresenter(this);
        movieList = new ArrayList<>();

        RecyclerView moviesView = findViewById(R.id.screenings_view);
        moviesView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((this));
        moviesView.setLayoutManager(linearLayoutManager);

        movieAdapter = new MovieAdapter(movieList);
        moviesView.setAdapter(movieAdapter);
        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        movieList.clear();
        presenter.loadMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_map) {
            Intent intent = new Intent(this, MapActivityView.class);
            intent.putParcelableArrayListExtra("movieList", new ArrayList<>(movieList));
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_favorites) {
            Intent intent = new Intent(this, FavoriteListView.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_list_screenings) {
            Intent intent = new Intent(this, ScreeningListView.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_preferences){
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public void registerMovie(View view) {
        Intent intent = new Intent(this, RegisterMovieView.class);
        startActivity(intent);
    }

    @Override
    public void listMovies(List<Movie> movieList) {
        this.movieList.addAll(movieList);
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(String message) {
        if (myPreferences.getBoolean("notifications", false)) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void showSuccesMessage(String message) {
        if (myPreferences.getBoolean("notifications", false)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}