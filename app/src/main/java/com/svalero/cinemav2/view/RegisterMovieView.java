package com.svalero.cinemav2.view;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Date;


public class RegisterMovieView extends AppCompatActivity implements RegisterMovieContract.View,
        Style.OnStyleLoaded, OnMapClickListener {

    //Tiene muy poca logica, no vamos a realizar un presenter nui un model, lo unico qeu hace es recoger los
    //datos del mapa y de las casillas

    private RegisterMoviePresenter presenter;

    private MapView mapView;

    private AnnotationManager pointAnnotationManager;
    //Para inizilizar los gestos
    private GesturesPlugin gesturesPlugin;
    //para capturar el punto que selecciona el usuario, utilizo currentpoint
    private Point currentPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movie);
        // le paso al presnter el view
        presenter = new RegisterMoviePresenter(this);

        //Inicializaciomos tb todos los elemntos que vamos a necesitar con el mapa
        initializeMapView();
        pointAnnotationManager = MapUtil.initializePointAnnotationManager(mapView);
        initializeGesturesPlugin();

    }

    //Tengo que asignar en el diseño un metodo al boton add_movie_buttom en el onclick
    public void register(View view){
        //recogo datos y llamo al presenter, nada mas, solo tareas de vista no puedo comprobar aqui..
        //en el presenter si que se pueden comprobar campos..
        if (currentPoint == null) {
            Toast.makeText(this, "Debes seleccionar una ubicación de filamcion principal", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            EditText movieTitleEditText = findViewById((R.id.movie_title));
            String movieTitle = movieTitleEditText.getText().toString();
            //
            EditText genreEditText = findViewById((R.id.genre));
            String genre = genreEditText.getText().toString();
            //
            EditText durationMinutesEditText = findViewById((R.id.duration_minutes));
            int durationMinutes = Integer.parseInt(durationMinutesEditText.getText().toString());
            //
            EditText releaseDateEditText = findViewById((R.id.release_date));
            Date releaseDate = DateUtil.format(releaseDateEditText.getText().toString());

            Movie movie = new Movie(movieTitle,genre,durationMinutes,currentPoint.latitude(), currentPoint.longitude(),releaseDate,true);
            presenter.registerMovie(movie);

        } catch (ParseException pe) {
            pe.printStackTrace();
        }



    }


    @Override
    public void showErrorMessage(String message) {
        //Siempre lo primero que piden es el componente del interfaz al que queda ligado el SnackBar
        Snackbar.make(findViewById((R.id.add_movie_button)), message, BaseTransientBottomBar.LENGTH_INDEFINITE).show();
    }

    @Override
    public void showSuccesMessage(String message) {
        Snackbar.make(findViewById((R.id.add_movie_button)), message, BaseTransientBottomBar.LENGTH_SHORT).show();

    }
    //Inicializo el mapa..
    private void initializeMapView() {
        mapView = findViewById(R.id.registerMapView);
        mapView.getMapboxMap().loadStyleUri(Style.SATELLITE_STREETS, this);
    }
    @Override
    public void onStyleLoaded(@NonNull Style style) {

    }
    //Inicializo para interactuar con el mapa
    private void initializeGesturesPlugin() {
        gesturesPlugin = GesturesUtils.getGestures(mapView);
        //Hacemos un listener para que se pueda hacer un click en el mapa
        //al ponerle this me tiene que extender tb de una interface que coloco arriba
        gesturesPlugin.addOnMapClickListener(this);
    }
    //cazo el punto, y no me interesa el texto como en la otra, asi que lo quito
    private void addMarker(double latitude, double longitude) {
        PointAnnotationOptions marker = new PointAnnotationOptions()
                .withIconImage(BitmapFactory.decodeResource(getResources(), R.mipmap.red_marker))
                .withPoint(Point.fromLngLat(longitude, latitude));
        pointAnnotationManager.create(marker);
    }

    @Override
    public boolean onMapClick(@NonNull Point point) {
        //Antes de nada borro lo anterior asi que se queda el ultimo
        pointAnnotationManager.deleteAll();
        //Cada vez que se haga un click llamara a este metodo que tiene que devolver un true para
        //validar la operacion, pero antes de ese true puedo hacer lo que quiera, y es donde
        //voy a recoger donde a hecho click el usuario, para eso nuevamente tengo que definir
        //un elemento nuevo que es un point
        //Me apunto que el usuario ha hecho click en un punto..como ? asi..
        currentPoint = point;
        //Ahora dibujo algo, para que se me quede grabada la posicion
        addMarker(point.latitude(), point.longitude());

        return true;
    }
}