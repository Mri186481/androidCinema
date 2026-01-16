package com.svalero.cinemav2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.mapbox.geojson.Point;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.svalero.cinemav2.R;
import com.svalero.cinemav2.domain.Movie;
import com.svalero.cinemav2.util.MapUtil;

import java.util.ArrayList;

public class MapActivityView extends AppCompatActivity implements Style.OnStyleLoaded {
    private ArrayList<Movie> movieList;
    private MapView mapView;
    private PointAnnotationManager pointAnnotationManager;
    private static final String CHANNEL_ID = "notificationes";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setTitle(getString(R.string.tl_mapa_filamciones));

        Intent intent = getIntent();
        movieList = intent.getParcelableArrayListExtra("movieList");

        mapView = findViewById(R.id.mapView);
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String mapType = myPreferences.getString("preference_map_type","Calles");
        if (mapType.equals("Calles")){
            mapType = Style.MAPBOX_STREETS;
        } else if (mapType.equals("Satelite")) {
            mapType = Style.SATELLITE;
        }
        mapView.getMapboxMap().loadStyleUri(mapType, this);
        pointAnnotationManager = MapUtil.initializePointAnnotationManager(mapView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar2, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_list_screenings2) {
            Intent intent = new Intent(this, ScreeningListView.class);
            startActivity(intent);
            //Con esto inicio la otra activity en el metodo oncreate
        } else if (item.getItemId() == R.id.action_preferences2){
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_favorites2) {
            Intent intent = new Intent(this, FavoriteListView.class);
            startActivity(intent);
        }
        return true;

    }


    private void viewMovies() {
        for (Movie movie : movieList) {
            addMarker(movie.getMovieTitle(), movie.getFilmingLatitude(), movie.getFilmingLongitude());
        }
    }

    private void addMarker(String message, double latitude, double longitude) {
        PointAnnotationOptions marker = new PointAnnotationOptions()
                .withIconImage(BitmapFactory.decodeResource(getResources(), R.mipmap.red_marker))
                .withTextField(message)
                .withTextColor(Color.RED)
                .withTextHaloColor(Color.WHITE)
                .withTextHaloWidth(2.0)
                .withIconSize(1.0)
                .withPoint(Point.fromLngLat(longitude, latitude));
        pointAnnotationManager.create(marker);
    }


    @Override
    public void onStyleLoaded(@NonNull Style style) {
        viewMovies();
    }

    public void registerMovieFromMap(View view) {
        Intent intent = new Intent(this, RegisterMovieView.class);
        startActivity(intent);
    }
}