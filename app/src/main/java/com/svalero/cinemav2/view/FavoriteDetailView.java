package com.svalero.cinemav2.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;
import androidx.room.Room;

import com.svalero.cinemav2.R;
import com.svalero.cinemav2.db.AppDatabase;
import com.svalero.cinemav2.domain.MovieDb;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class FavoriteDetailView extends AppCompatActivity {
    private MovieDb movieDb;
    private SharedPreferences myPreferences;
    private String preferencesName;

    private ImageView movieImageView;

    private  String imageUriDb = "";

    private static final int READ_MEDIA_IMAGES_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite_detail_view);
        setTitle(getString(R.string.tl_detalle_favoritos));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesName = myPreferences.getString("your_name","");
        Intent intent = getIntent();
        Long IdDBFavorite = intent.getLongExtra("IdBd", -1);
        movieImageView = findViewById(R.id.det_bd_movie_image);
        movieImageView.setImageResource(R.drawable.sin_imagen_bd);

        if (IdDBFavorite != -1) {
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
                        if (myPreferences.getBoolean("notifications", false)) {
                            Toast.makeText(this, preferencesName + getString(R.string.movie_not_found_bd), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            });
        } else {
            // Si la ID es inválida, mmuestro el Toast directamente
            if (myPreferences.getBoolean("notifications", false)) {
                Toast.makeText(this, preferencesName + getString(R.string.movie_not_valid_bd), Toast.LENGTH_SHORT).show();
            }
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

        cargarImagen();

    }

    // Tratamiento de Imagenes
    private void cargarImagen() {
        if (movieDb == null) return;

        String imagePath = movieDb.getMovieImageDb();

        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Uri imageUri = Uri.parse(imagePath);
                movieImageView.setImageURI(imageUri);
            } catch (Exception e) {
                //
                Log.e("FavoriteDetailView", "Error al cargar la imagen con URI: " + imagePath, e);
                // pongo una imagen por defecto si falla la carga
                movieImageView.setImageResource(R.drawable.tron_identidad);
            }
        } else {
            // Si no hay una URI de imagen en la base de datos, muestro la imagen por defecto
            movieImageView.setImageResource(R.drawable.tron_identidad);
        }
    }

    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Uri image_uri = result.getData().getData();
                    if (image_uri != null) {
                        final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        getContentResolver().takePersistableUriPermission(image_uri, takeFlags);
                        movieImageView.setImageURI(image_uri);
                        imageUriDb = image_uri.toString();
                    }
                }
            }
    );

    public void selectImage(View view) {
        //Obtencion de permisos persistentes
        Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryActivityResultLauncher.launch(galleryIntent);
    }

    public void updateBdFavorite(View view) {
        if (this.movieDb == null) {
            if (myPreferences.getBoolean("notifications", false)) {
                Toast.makeText(this, preferencesName + getString(R.string.movie_not_load_bd), Toast.LENGTH_SHORT).show();
            }
            return;
        }

        //Recoger los datos de los campos de la interfaz
        EditText movieTitleEditText = findViewById(R.id.det_bd_movie_title);
        EditText genreEditText = findViewById(R.id.det_bd_genre);
        EditText durationMinutesEditText = findViewById(R.id.det_bd_duration_minutes);
        EditText releaseDateEditText = findViewById(R.id.det_bd_release_date);
        CheckBox currentlyShowingCheckBox = findViewById(R.id.det_bd_currently_showing);

        //Actualizar el objeto movieDb con los nuevos datos
        this.movieDb.setMovieTitleDb(movieTitleEditText.getText().toString());
        this.movieDb.setGenreDb(genreEditText.getText().toString());
        try {
            this.movieDb.setDurationMinutesDb(Integer.parseInt(durationMinutesEditText.getText().toString()));
        } catch (NumberFormatException nfe) {
            if (myPreferences.getBoolean("notifications", false)) {
                Toast.makeText(this, preferencesName + getString(R.string.number_must_be_valid), Toast.LENGTH_SHORT).show();
            }
            return;
        }
        this.movieDb.setReleaseDateDb(releaseDateEditText.getText().toString());
        this.movieDb.setCurrentlyShowingDb(currentlyShowingCheckBox.isChecked());
        //iamge
        this.movieDb.setMovieImageDb(imageUriDb);


        //Ejecutar la actualización en un hilo secundario

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.lb_esta_seguro_modificar)
                .setPositiveButton(R.string.lb_si,
                        (dialog, which) -> { // Expresión Lambda para simplificar
                            // Modo edición
                            Executor executor = Executors.newSingleThreadExecutor();
                            Handler handler = new Handler(Looper.getMainLooper());

                            executor.execute(() -> {
                                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "moviedb.db").build();
                                db.movieDbDao().updateMovieDb(this.movieDb);

                                handler.post(() -> {
                                    if (myPreferences.getBoolean("notifications", false)) {
                                        Toast.makeText(this, preferencesName + getString(R.string.update_favorite_right), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            });

                        })
                .setNegativeButton(R.string.lb_no,
                        (dialog, which) -> dialog.dismiss());
        builder.create().show();


    }

    public void deleteBdFavorite(View view) {
        if (this.movieDb == null) {
            if (myPreferences.getBoolean("notifications", false)) {
                Toast.makeText(this, preferencesName + getString(R.string.movie_not_load_bd), Toast.LENGTH_SHORT).show();
            }
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.lb_esta_seguro)
                .setPositiveButton(R.string.lb_si,
                        (dialog, which) -> {
                            Executor executor = Executors.newSingleThreadExecutor();
                            Handler handler = new Handler(Looper.getMainLooper());

                            executor.execute(() -> {
                                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "moviedb.db").build();
                                db.movieDbDao().deleteMovieDb(this.movieDb);

                                handler.post(() -> {
                                    if (myPreferences.getBoolean("notifications", false)) {
                                        Toast.makeText(this, preferencesName + getString(R.string.delete_favorite_right), Toast.LENGTH_SHORT).show();
                                    }
                                    finish();
                                });
                            });
                        })
                .setNegativeButton(R.string.lb_no,
                        (dialog, which) -> dialog.dismiss());
        builder.create().show();


    }


}