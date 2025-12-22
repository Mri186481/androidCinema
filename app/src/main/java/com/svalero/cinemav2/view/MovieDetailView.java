package com.svalero.cinemav2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;


import com.mapbox.geojson.Point;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.svalero.cinemav2.R;
import com.svalero.cinemav2.api.MoviesApi;
import com.svalero.cinemav2.api.MoviesApiInterface;
import com.svalero.cinemav2.contract.MovieDetailContract;
import com.svalero.cinemav2.domain.Movie;
import com.svalero.cinemav2.model.MovieDetailModel;
import com.svalero.cinemav2.presenter.MovieDetailPresenter;
import com.svalero.cinemav2.util.MapUtil;

import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailView extends AppCompatActivity implements MovieDetailContract.View, Style.OnStyleLoaded {
    private Movie movie;
    private MovieDetailPresenter presenter;
    private MapView mapView;
    private PointAnnotationManager pointAnnotationManager;
    //Para que se referesque cuando se haya actualizado y tenga los datos actualizados, defino un codigo de solicitud
    // Código de solicitud para el resultado
    private static final int UPDATE_MOVIE_REQUEST = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        // Inicializamos el presentador y la vista se pasa a sí misma
        presenter = new MovieDetailPresenter(this, new MovieDetailModel());

        Intent intent = getIntent();
        Long movieId = intent.getLongExtra("movieId", -1);

        // Inicializamos los elementos de la interfaz de usuario
        //Inicializaciomos tb todos los elemntos que vamos a necesitar con el mapa



        mapView = findViewById(R.id.detailMapView);
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

        // Llamamos al presentador para que inicie la carga de datos
        if (movieId != -1) {
            presenter.loadMovieDetail(movieId);
        } else {
            showErrorMessage("ID de película no válido.");
        }


    }

    @Override
    public void showMovieDetail(Movie movie) {
        // Limpiamos el mapa de marcadores anteriores
        if (pointAnnotationManager != null) {
            pointAnnotationManager.deleteAll();
        }
        //guardamos el objeto movie en la variable de clase
        this.movie = movie;
        // Asignamos los datos a los TextViews correspondientes
        ((TextView) findViewById(R.id.screening_movie_id)).setText("ID: " + movie.getId());
        ((TextView) findViewById(R.id.screening_movie_title)).setText(movie.getMovieTitle());
        ((TextView) findViewById(R.id.det_genre)).setText("Género: " + movie.getGenre());
        ((TextView) findViewById(R.id.det_duration_minutes)).setText("Duración: " + movie.getDurationMinutes() + " minutos");

        // Formateamos la fecha antes de mostrarla
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String releaseDate = dateFormat.format(movie.getReleaseDate());
        ((TextView) findViewById(R.id.det_release_date)).setText("Fecha de Estreno: " + releaseDate);

        // Establecemos el estado del CheckBox
        ((CheckBox) findViewById(R.id.det_currently_showing)).setChecked(movie.isCurrentlyShowing());
        // Pintamos la posicion en el mapa
        viewMovie(movie);

    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }
    private void viewMovie(Movie movie) {

        addMarker(movie.getMovieTitle(), movie.getFilmingLatitude(), movie.getFilmingLongitude());

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
        //no llamo a nada ya que aqui no dispongo del movie, tengo que cargar antes los datos de la API
    }
    public void delMovie(View view) {
        //Antes de mostrar el diálogo, comprobamos si tenemos una película cargada ---
        if (movie == null) {
            Toast.makeText(this, "Aún no se han cargado los datos de la película.", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.lb_esta_seguro)
                .setPositiveButton(R.string.lb_si,
                        (dialog, which) -> { // Expresión Lambda para simplificar
                            // Obtenemos el ID directamente de nuestra variable de clase
                            long movieIdToDelete = this.movie.getId();

                            MoviesApiInterface apiInterface = MoviesApi.buildInstance();
                            Call<Void> callDeleteMovie = apiInterface.deleteMovie(movieIdToDelete);

                            callDeleteMovie.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(MovieDetailView.this, "Película eliminada correctamente", Toast.LENGTH_SHORT).show();
                                        // Cerramos la vista de detalle porque la película ya no existe
                                        finish();
                                    } else {
                                        Toast.makeText(MovieDetailView.this, "Error al eliminar. Código: " + response.code(), Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(MovieDetailView.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        })
                .setNegativeButton(R.string.lb_no,
                        (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
    public void updateMovie(View view){
        Intent intent = new Intent(this, RegisterMovieView.class);
        intent.putExtra("dataMovie", movie);
        startActivityForResult(intent, UPDATE_MOVIE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verificamos si la respuesta viene de RegisterMovieView y la operación fue exitosa
        if (requestCode == UPDATE_MOVIE_REQUEST && resultCode == RESULT_OK) {
            // Obtenemos el ID de la película que se actualizó
            Long updatedMovieId = data.getLongExtra("updatedMovieId", -1);
            if (updatedMovieId != -1) {
                // Recargamos los datos de la película para actualizar la vista
                presenter.loadMovieDetail(updatedMovieId);
            }
        }
    }

    public void registerScreening(View view){
//
        Intent intent = new Intent(this, RegisterScreeningView.class);
        intent.putExtra("screeningMovieTitle", movie.getMovieTitle());
        intent.putExtra("screeningMovieId", movie.getId());
        startActivity(intent);

    }


}

