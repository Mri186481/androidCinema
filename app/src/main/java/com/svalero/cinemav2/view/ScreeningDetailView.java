package com.svalero.cinemav2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

import com.svalero.cinemav2.R;
import com.svalero.cinemav2.api.ScreeningsApi;
import com.svalero.cinemav2.api.ScreeningsApiInterface;
import com.svalero.cinemav2.contract.ScreeningDetailContract;
import com.svalero.cinemav2.domain.Screening;
import com.svalero.cinemav2.model.ScreeningDetailModel;
import com.svalero.cinemav2.presenter.ScreeningDetailPresenter;
import com.svalero.cinemav2.util.DateTimeUtil;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScreeningDetailView extends AppCompatActivity implements ScreeningDetailContract.View {
    private Screening screening;
    private ScreeningDetailPresenter presenter;
    private static final int UPDATE_SCREENING_REQUEST = 1;

    private SharedPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screening_detail_view);
        setTitle(getString(R.string.tl_detalle_sesion));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        presenter = new ScreeningDetailPresenter(this, new ScreeningDetailModel());

        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Intent intent = getIntent();
        Long screeningId = intent.getLongExtra("screeningId", -1);

        if (screeningId != -1) {
            presenter.loadScreeningDetail(screeningId);
        } else {
            showErrorMessage("ID de screening no válido.");
        }
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

    @Override
    public void showScreeningDetail(Screening screening) {
        this.screening = screening;
        ((TextView) findViewById(R.id.det_screening_id)).setText("ID: " + screening.getId());
        ((TextView) findViewById(R.id.det_screening_movie_title)).setText(screening.getMovieTitle());
        String screeningTimeString = screening.getScreeningTime();
        String timePart1 = screeningTimeString;
        String timePart2 = screeningTimeString;
        LocalDateTime screeningDateTime = DateTimeUtil.stringToLocalDateTime(screeningTimeString);
        if (screeningDateTime != null) {
            timePart1 = String.valueOf(screeningDateTime.getDayOfMonth()) + "-" + String.valueOf(screeningDateTime.getMonth()) + "-" + String.valueOf(screeningDateTime.getYear());
            timePart2 =  screeningDateTime.getHour() + ":" + screeningDateTime.getMinute();
        }

        ((TextView) findViewById(R.id.det_screening_time_part1)).setText(timePart1);
        ((TextView) findViewById(R.id.det_screening_time_part2)).setText(timePart2);
        ((TextView) findViewById(R.id.det_screening_ticket_price)).setText(String.valueOf(screening.getTicketPrice()));
        ((CheckBox) findViewById(R.id.det_screening_subtitled)).setChecked(screening.isSubtitled());
    }

    @Override
    public void showErrorMessage(String message) {
        if (myPreferences.getBoolean("notifications", false)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showSuccessMessage(String message) {
        if (myPreferences.getBoolean("notifications", false)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void delScreening(View view) {
        String preferencesName = myPreferences.getString("your_name","");
        if (screening == null) {
            if (myPreferences.getBoolean("notifications", false)) {
                Toast.makeText(this, preferencesName + " Aún no se han cargado los datos de la sesion.", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.lb_esta_seguro_sesion)
                .setPositiveButton(R.string.lb_si,
                        (dialog, which) -> {
                            long screeningIdToDelete = this.screening.getId();

                            ScreeningsApiInterface apiInterface = ScreeningsApi.buildInstance();
                            Call<Void> callDeleteScreening = apiInterface.deleteScreening(screeningIdToDelete);

                            callDeleteScreening.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        if (myPreferences.getBoolean("notifications", false)) {
                                            Toast.makeText(ScreeningDetailView.this, preferencesName + getString(R.string.delete_screening_right), Toast.LENGTH_SHORT).show();
                                        }
                                        finish();
                                    } else {
                                        if (myPreferences.getBoolean("notifications", false)) {
                                            Toast.makeText(ScreeningDetailView.this, preferencesName + " Error al eliminar. Código: " + response.code(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    if (myPreferences.getBoolean("notifications", false)) {
                                        Toast.makeText(ScreeningDetailView.this, preferencesName + " Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        })
                .setNegativeButton(R.string.lb_no,
                        (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    public void updateScreening(View view){
        Intent intent = new Intent(this, RegisterScreeningView.class);
        intent.putExtra("dataScreening", screening);
        startActivityForResult(intent, UPDATE_SCREENING_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_SCREENING_REQUEST && resultCode == RESULT_OK) {
            Long updatedScreeningId = data.getLongExtra("updatedScreeningId", -1);
            if (updatedScreeningId != -1) {
                presenter.loadScreeningDetail(updatedScreeningId);
            }
        }
    }
}