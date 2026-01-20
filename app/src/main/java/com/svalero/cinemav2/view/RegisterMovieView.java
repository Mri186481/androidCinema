package com.svalero.cinemav2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.geojson.Point;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.AnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.GesturesPlugin;
import com.mapbox.maps.plugin.gestures.GesturesUtils;
import com.mapbox.maps.plugin.gestures.OnMapClickListener;
import com.svalero.cinemav2.R;
import com.svalero.cinemav2.contract.RegisterMovieContract;
import com.svalero.cinemav2.domain.Movie;
import com.svalero.cinemav2.presenter.RegisterMoviePresenter;
import com.svalero.cinemav2.util.DateUtil;
import com.svalero.cinemav2.util.MapUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RegisterMovieView extends AppCompatActivity implements RegisterMovieContract.View,
        Style.OnStyleLoaded, OnMapClickListener {

    private RegisterMoviePresenter presenter;

    private MapView mapView;

    private AnnotationManager pointAnnotationManager;
    private GesturesPlugin gesturesPlugin;
    private Point currentPoint;
    private long movieId = -1;
    private Button registerButton;
    private SharedPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movie);
        setTitle(getString(R.string.tl_add_movie));
        presenter = new RegisterMoviePresenter(this);
        registerButton = findViewById(R.id.add_movie_button);

        initializeMapView();
        pointAnnotationManager = MapUtil.initializePointAnnotationManager(mapView);
        initializeGesturesPlugin();

        Intent intent = getIntent();
        if (intent != null) {
            Movie movie = intent.getParcelableExtra("dataMovie");

            if (movie != null) {
                this.movieId = movie.getId();
                setTitle("Modificar Movie");
                registerButton.setText("Modificar");
                registerButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));

                EditText movieTitleEditText = findViewById(R.id.movie_title);
                movieTitleEditText.setText(movie.getMovieTitle());

                EditText genreEditText = findViewById(R.id.genre);
                genreEditText.setText(movie.getGenre());

                EditText durationMinutesEditText = findViewById(R.id.duration_minutes);
                durationMinutesEditText.setText(String.valueOf(movie.getDurationMinutes()));

                EditText releaseDateEditText = findViewById(R.id.release_date);
                if (movie.getReleaseDate() != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    String releaseDate = dateFormat.format(movie.getReleaseDate());
                    releaseDateEditText.setText(releaseDate);
                }

                CheckBox currentlyShowingCheckBox = findViewById(R.id.currently_showing);
                currentlyShowingCheckBox.setChecked(movie.isCurrentlyShowing());

                currentPoint = Point.fromLngLat(movie.getFilmingLongitude(), movie.getFilmingLatitude());
                addMarker(currentPoint.latitude(), currentPoint.longitude());
            }
        }
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

    public void register(View view){
        if (currentPoint == null) {
            if (myPreferences.getBoolean("notifications", false)) {
                String preferencesName = myPreferences.getString("your_name","");
                Toast.makeText(this, preferencesName + getString(R.string.select_site), Toast.LENGTH_LONG).show();
                return;
            } else {
                Toast.makeText(this, "Notifications are disabled", Toast.LENGTH_LONG).show();
                return;
            }
        }
        try {
            EditText movieTitleEditText = findViewById((R.id.movie_title));
            String movieTitle = movieTitleEditText.getText().toString();
            //
            EditText genreEditText = findViewById((R.id.genre));
            String genre = genreEditText.getText().toString();
            //
            EditText durationMinutesEditText = findViewById((R.id.duration_minutes));
            String durationText = durationMinutesEditText.getText().toString();
            int durationMinutes = 0;
            if (!durationText.isEmpty()) {
                try {
                    durationMinutes = Integer.parseInt(durationText);
                } catch (NumberFormatException e) {
                    durationMinutes = 0;
                }
            }

            EditText releaseDateEditText = findViewById((R.id.release_date));
            //
            String releaseDateText = releaseDateEditText.getText().toString();
            Date releaseDate = null;

            if (!releaseDateText.isEmpty()) {
                releaseDate = DateUtil.format(releaseDateEditText.getText().toString());
            }

            CheckBox currentlyShowingCheckBox = findViewById(R.id.currently_showing);
            boolean currentlyShowing = currentlyShowingCheckBox.isChecked();

            Movie movie = new Movie(movieTitle,genre,durationMinutes,currentPoint.latitude(), currentPoint.longitude(),releaseDate,currentlyShowing);

            if (movieId != -1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.lb_esta_seguro_modificar)
                        .setPositiveButton(R.string.lb_si,
                                (dialog, which) -> {
                                    // Modo edición
                                    movie.setId(movieId);
                                    presenter.updateMovie(movie);
                                })
                        .setNegativeButton(R.string.lb_no,
                                (dialog, which) -> dialog.dismiss());
                builder.create().show();

            } else {
                presenter.registerMovie(movie);
            }


        } catch (ParseException pe) {
            pe.printStackTrace();
        }

    }

    @Override
    public void showErrorMessage(String message) {
        if (myPreferences.getBoolean("notifications", false)) {
            Snackbar.make(findViewById((R.id.add_movie_button)), message, BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showSuccesMessage(String message) {
        if (myPreferences.getBoolean("notifications", false)) {
            Snackbar.make(findViewById((R.id.add_movie_button)), message, BaseTransientBottomBar.LENGTH_SHORT).show();
        }


    }
    //Inicializo el mapa..
    private void initializeMapView() {
        mapView = findViewById(R.id.detailMapView);
        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String mapType = myPreferences.getString("preference_map_type","Calles");
        if (mapType.equals("Calles")){
            mapType = Style.MAPBOX_STREETS;
        } else if (mapType.equals("Satelite")) {
            mapType = Style.SATELLITE;
        }
        mapView.getMapboxMap().loadStyleUri(mapType, this);
    }
    @Override
    public void onStyleLoaded(@NonNull Style style) {
    }
    private void initializeGesturesPlugin() {
        gesturesPlugin = GesturesUtils.getGestures(mapView);
        gesturesPlugin.addOnMapClickListener(this);
    }
    private void addMarker(double latitude, double longitude) {
        PointAnnotationOptions marker = new PointAnnotationOptions()
                .withIconImage(BitmapFactory.decodeResource(getResources(), R.mipmap.red_marker))
                .withTextColor(Color.RED)
                .withTextHaloColor(Color.WHITE)
                .withTextHaloWidth(2.0)
                .withIconSize(1.0)
                .withPoint(Point.fromLngLat(longitude, latitude));
        pointAnnotationManager.create(marker);
    }

    @Override
    public boolean onMapClick(@NonNull Point point) {
        pointAnnotationManager.deleteAll();
        currentPoint = point;
        addMarker(point.latitude(), point.longitude());

        return true;
    }
    public void onUpdateMovieSuccess() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedMovieId", movieId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}