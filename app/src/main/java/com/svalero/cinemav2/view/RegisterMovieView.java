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

    // Declarar aquí para que esté disponible en toda la clase
    private RegisterMoviePresenter presenter;

    private MapView mapView;

    private AnnotationManager pointAnnotationManager;
    //Para inizilizar los gestos
    private GesturesPlugin gesturesPlugin;
    //para capturar el punto que selecciona el usuario, utilizo currentpoint
    private Point currentPoint;

    private long movieId = -1;

    private Button registerButton;

    private SharedPreferences myPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movie);
        setTitle(getString(R.string.tl_add_movie));
        // le paso al presnter el view
        presenter = new RegisterMoviePresenter(this);
        //Referencio el boton
        registerButton = findViewById(R.id.add_movie_button);

        //Inicializaciomos tb todos los elemntos que vamos a necesitar con el mapa
        initializeMapView();
        pointAnnotationManager = MapUtil.initializePointAnnotationManager(mapView);
        initializeGesturesPlugin();

        // Para reutilizar codigo para la edicion/ asi relleno los datos cuando es un update
        Intent intent = getIntent();
        if (intent != null) {
            Movie movie = intent.getParcelableExtra("dataMovie");

            if (movie != null) {
                // Almacenamos el ID de la película que se está editando
                this.movieId = movie.getId();
                setTitle("Modificar Movie");
                registerButton.setText("Modificar");
                registerButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));

                // Rellenamos los campos de la interfaz con los datos de la película
                EditText movieTitleEditText = findViewById(R.id.movie_title);
                movieTitleEditText.setText(movie.getMovieTitle());

                EditText genreEditText = findViewById(R.id.genre);
                genreEditText.setText(movie.getGenre());

                EditText durationMinutesEditText = findViewById(R.id.duration_minutes);
                durationMinutesEditText.setText(String.valueOf(movie.getDurationMinutes()));

                EditText releaseDateEditText = findViewById(R.id.release_date);
                // Asegúrate de que tu utilidad DateUtil pueda formatear la fecha a String
                if (movie.getReleaseDate() != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    String releaseDate = dateFormat.format(movie.getReleaseDate());
                    releaseDateEditText.setText(releaseDate);
                }

                CheckBox currentlyShowingCheckBox = findViewById(R.id.currently_showing);
                currentlyShowingCheckBox.setChecked(movie.isCurrentlyShowing());

                // Actualizamos el punto en el mapa y dibujamos el marcador
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
        //Aqui programo algo, como solo hay dos programo con un if

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
        //En cualquier caso si he gestionado el caso devuelvo un true
        return true;

    }

    //Tengo que asignar en el diseño un metodo al boton add_movie_buttom en el onclick
    public void register(View view){
        //recogo datos y llamo al presenter, nada mas, solo tareas de vista no puedo comprobar aqui..
        //en el presenter si que se pueden comprobar campos..
        if (currentPoint == null) {
            if (myPreferences.getBoolean("notifications", false)) {
                String preferencesName = myPreferences.getString("your_name","");
                Toast.makeText(this, preferencesName + getString(R.string.select_site), Toast.LENGTH_LONG).show();
                return;
            } else {
                //Prueba para comprobar que las notificaciones estan desactivadas, de momento lo mantengo, para prueba de autoria
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
            int durationMinutes = Integer.parseInt(durationMinutesEditText.getText().toString());
            // Inicio de proceso para validacion de fecha...Para comprobar que no esta vacia debo de trabajar con el presenter
            EditText releaseDateEditText = findViewById((R.id.release_date));
            //
            String releaseDateText = releaseDateEditText.getText().toString();
            // Inicializar la variable de fecha a null
            Date releaseDate = null;

            //Si el texto no esta vacio no la parseo y envia un null al presenter que no le dejara seguir
            if (!releaseDateText.isEmpty()) {
                releaseDate = DateUtil.format(releaseDateEditText.getText().toString());
            }
            //Fin de validacion de la fecha

            CheckBox currentlyShowingCheckBox = findViewById(R.id.currently_showing);
            boolean currentlyShowing = currentlyShowingCheckBox.isChecked();

            Movie movie = new Movie(movieTitle,genre,durationMinutes,currentPoint.latitude(), currentPoint.longitude(),releaseDate,currentlyShowing);

            if (movieId != -1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.lb_esta_seguro_modificar)
                        .setPositiveButton(R.string.lb_si,
                                (dialog, which) -> { // Expresión Lambda para simplificar
                                    // Modo edición
                                    movie.setId(movieId);
                                    presenter.updateMovie(movie);
                                })
                        .setNegativeButton(R.string.lb_no,
                                (dialog, which) -> dialog.dismiss());
                builder.create().show();

            } else {
                // Modo nuevo registro
                presenter.registerMovie(movie);
            }


        } catch (ParseException pe) {
            pe.printStackTrace();
        }

    }

    @Override
    public void showErrorMessage(String message) {
        //Solo se muestra mensaje si se permiten notificaciones
        if (myPreferences.getBoolean("notifications", false)) {
            Snackbar.make(findViewById((R.id.add_movie_button)), message, BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showSuccesMessage(String message) {
        //Solo se muestra mensaje si se permiten notificaciones
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
            //puedo incluso reutilizar la misma variable, y como son strings se aprovecha
            mapType = Style.MAPBOX_STREETS;
        } else if (mapType.equals("Satelite")) {
            mapType = Style.SATELLITE;
        }
        mapView.getMapboxMap().loadStyleUri(mapType, this);
    }
    @Override
    public void onStyleLoaded(@NonNull Style style) {

    }
    //Inicializo para interactuar con el mapa
    private void initializeGesturesPlugin() {
        gesturesPlugin = GesturesUtils.getGestures(mapView);
        //Hacemos un listener para que se pueda hacer un click en el mapa
        //al poner le this me tiene que extender tb de una interface que coloco arriba
        gesturesPlugin.addOnMapClickListener(this);
    }
    //cazo el punto, y no me interesa el texto como en la otra, asi que lo quito
    private void addMarker(double latitude, double longitude) {
        PointAnnotationOptions marker = new PointAnnotationOptions()
                .withIconImage(BitmapFactory.decodeResource(getResources(), R.mipmap.red_marker))
                // 1. CAMBIO DE COLOR DE TEXTO
                .withTextColor(Color.RED)
                // Opcional: Borde blanco para que se lea mejor sobre el mapa
                .withTextHaloColor(Color.WHITE)
                .withTextHaloWidth(2.0)
                // 2. SOLUCIÓN ICONO NEGRO (Evitar tinte al seleccionar)
                .withIconSize(1.0)
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
    //Cuando la actualizacion se registre debo de enviar un resultado de vuelta para
    //que se vea que la actualizacion se ha hecho correctamente, este metodo lo he puesto
    //nuevo en el contract, en principio no hace falta pero queda mas consistente la app
    public void onUpdateMovieSuccess() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedMovieId", movieId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }


}