package com.svalero.cinemav2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

//Tiene muy poca logica, no vamos a realizar un presenter nui un model, lo unico que hace es recoger los
//datos del mapa y de las casillas
public class MapActivityView extends AppCompatActivity implements Style.OnStyleLoaded {
    private ArrayList<Movie> movieList;
    private MapView mapView;
    private PointAnnotationManager pointAnnotationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        movieList = intent.getParcelableArrayListExtra("movieList");

        mapView = findViewById(R.id.mapView);
        //esto me permite obtener las preferencias de mi aplicacion
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String mapType = myPreferences.getString("preference_map_type","Calles");
        if (mapType.equals("Calles")){
            //puedo incluso reutilizar la misma variable, y como son strings se aprovecha
            mapType = Style.MAPBOX_STREETS;
        } else if (mapType.equals("Satelite")) {
            mapType = Style.SATELLITE;
        }
        mapView.getMapboxMap().loadStyleUri(mapType, this);
        pointAnnotationManager = MapUtil.initializePointAnnotationManager(mapView);

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