package com.svalero.cinemav2.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screening_detail_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        presenter = new ScreeningDetailPresenter(this, new ScreeningDetailModel());

        Intent intent = getIntent();
        Long screeningId = intent.getLongExtra("screeningId", -1);

        // Llamamos al presentador para que inicie la carga de datos
        if (screeningId != -1) {
            presenter.loadScreeningDetail(screeningId);
        } else {
            showErrorMessage("ID de screening no válido.");
        }







    }

    @Override
    public void showScreeningDetail(Screening screening) {
        this.screening = screening;
        // Asignamos los datos a los TextViews correspondientes
        ((TextView) findViewById(R.id.det_screening_id)).setText("ID: " + screening.getId());
        ((TextView) findViewById(R.id.det_screening_movie_title)).setText(screening.getMovieTitle());
        //Formateo de fecha
        String screeningTimeString = screening.getScreeningTime();
        String timePart1 = screeningTimeString;
        String timePart2 = screeningTimeString;
        LocalDateTime screeningDateTime = DateTimeUtil.stringToLocalDateTime(screeningTimeString);
        // 3. Ahora puedes trabajar con él de forma segura
        if (screeningDateTime != null) {
            timePart1 = String.valueOf(screeningDateTime.getDayOfMonth()) + "-" + String.valueOf(screeningDateTime.getMonth()) + "-" + String.valueOf(screeningDateTime.getYear());
            timePart2 =  screeningDateTime.getHour() + ":" + screeningDateTime.getMinute();
        }


        ((TextView) findViewById(R.id.det_screening_time_part1)).setText(timePart1);
        ((TextView) findViewById(R.id.det_screening_time_part2)).setText(timePart2);
        ((TextView) findViewById(R.id.det_screening_ticket_price)).setText(String.valueOf(screening.getTicketPrice()));

        // Establecemos el estado del CheckBox
        ((CheckBox) findViewById(R.id.det_screening_subtitled)).setChecked(screening.isSubtitled());

    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    public void delScreening(View view) {
        //Antes de mostrar el diálogo, comprobamos si tenemos una película cargada ---
        if (screening == null) {
            Toast.makeText(this, "Aún no se han cargado los datos de la sesion.", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.lb_esta_seguro)
                .setPositiveButton(R.string.lb_si,
                        (dialog, which) -> { // Expresión Lambda para simplificar
                            // Obtenemos el ID directamente de nuestra variable de clase
                            long screeningIdToDelete = this.screening.getId();

                            ScreeningsApiInterface apiInterface = ScreeningsApi.buildInstance();
                            Call<Void> callDeleteScreening = apiInterface.deleteScreening(screeningIdToDelete);

                            callDeleteScreening.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(ScreeningDetailView.this, "Sesion eliminada correctamente", Toast.LENGTH_SHORT).show();
                                        // Cerramos la vista de detalle porque la película ya no existe
                                        finish();
                                    } else {
                                        Toast.makeText(ScreeningDetailView.this, "Error al eliminar. Código: " + response.code(), Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(ScreeningDetailView.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
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

        // Verificamos si la respuesta viene de RegisterMovieView y la operación fue exitosa
        if (requestCode == UPDATE_SCREENING_REQUEST && resultCode == RESULT_OK) {
            // Obtenemos el ID de la película que se actualizó
            Long updatedScreeningId = data.getLongExtra("updatedScreeningId", -1);
            if (updatedScreeningId != -1) {
                // Recargamos los datos de la película para actualizar la vista
                presenter.loadScreeningDetail(updatedScreeningId);
            }
        }
    }


}