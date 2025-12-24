package com.svalero.cinemav2.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.cinemav2.R;
import com.svalero.cinemav2.contract.RegisterScreeningContract;
import com.svalero.cinemav2.domain.Screening;
import com.svalero.cinemav2.domain.ScreeningIn;
import com.svalero.cinemav2.presenter.RegisterScreeningPresenter;

public class RegisterScreeningView extends AppCompatActivity implements RegisterScreeningContract.View {
    private RegisterScreeningPresenter presenter;
    //Variables para una futura modificacion
    private long screeningId = -1;
    private Button registerScreeningButton;

    private Long SmovieId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_screening_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // le paso al presnter el view
        presenter = new RegisterScreeningPresenter(this);
        //Referencio el boton
        registerScreeningButton = findViewById(R.id.add_screening_button);
        //Capturo la pelicula de la que voy a generar o modificar una sesion
        Intent intent = getIntent();
        //Registrar
        String SmovieTitle = intent.getStringExtra("screeningMovieTitle");
        SmovieId = intent.getLongExtra("screeningMovieId", 0);
        //Acualizar
        Screening screening = null;
        screening = intent.getParcelableExtra("dataScreening");
        if (screening != null) {
            // Almacenamos el ID de la screening que se está editando
            this.screeningId = screening.getId();
            registerScreeningButton.setText("Modificar");
            registerScreeningButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));

            // Rellenamos los campos de la interfaz con los datos de la película
            EditText screeningTimeEditText = findViewById((R.id.screening_add_time));
            screeningTimeEditText.setText(screening.getScreeningTime());
            //
            EditText screeningTicketEditText = findViewById((R.id.screening_add_ticket));
            screeningTicketEditText.setText(String.valueOf(screening.getTicketPrice()));
            //
            CheckBox subtitledCheckBox = findViewById(R.id.screening_add_subtitled);
            subtitledCheckBox.setChecked(screening.isSubtitled());
            //
            EditText roomEditText = findViewById((R.id.screening_add_id_room));
            roomEditText.setText(String.valueOf(screening.getRoomId()));
            //
            EditText movieEditText = findViewById((R.id.screening_add_id_movie));
            movieEditText.setText(String.valueOf(screening.getMovieId()));
        }





    }

    public void registerScreening(View view){
        //recogo datos y llamo al presenter, nada mas, solo tareas de vista no puedo comprobar aqui..
        //en el presenter si que se pueden comprobar campos..


            EditText screeningTimeEditText = findViewById((R.id.screening_add_time));
            String screeningTime = screeningTimeEditText.getText().toString();
            //
            EditText screeningTicketEditText = findViewById((R.id.screening_add_ticket));
            double screeningTicket = Double.parseDouble(screeningTicketEditText.getText().toString());
            //
            CheckBox subtitledCheckBox = findViewById(R.id.screening_add_subtitled);
            boolean subtitled = subtitledCheckBox.isChecked();
            //
            EditText roomEditText = findViewById((R.id.screening_add_id_room));
            Long room = Long.parseLong(roomEditText.getText().toString());
            Long RmovieId = SmovieId;
            if (screeningId != -1) {
                EditText movieEditText = findViewById((R.id.screening_add_id_movie));
                RmovieId = Long.parseLong(movieEditText.getText().toString());
            }

            //Fin de validacion de la fecha


            ScreeningIn screeningIn = new ScreeningIn(screeningTime,screeningTicket,subtitled,RmovieId, room);

        if (screeningId != -1) {
            // Modo edición
            screeningIn.setId(screeningId);
            presenter.updateScreening(screeningIn);
        } else {
            // Modo nuevo registro
            presenter.registerScreening(screeningIn);
        }


    }


    @Override
    public void showErrorMessage(String message) {
        Snackbar.make(findViewById((R.id.add_screening_button)), message, BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccesMessage(String message) {
        Snackbar.make(findViewById((R.id.add_screening_button)), message, BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateScreeningSuccess() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedScreeningId", screeningId);
        setResult(RESULT_OK, resultIntent);
        finish();

    }


}