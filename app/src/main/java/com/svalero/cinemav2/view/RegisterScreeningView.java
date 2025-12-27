package com.svalero.cinemav2.view;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.cinemav2.R;
import com.svalero.cinemav2.contract.RegisterScreeningContract;
import com.svalero.cinemav2.domain.Screening;
import com.svalero.cinemav2.domain.ScreeningIn;
import com.svalero.cinemav2.presenter.RegisterScreeningPresenter;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;

public class RegisterScreeningView extends AppCompatActivity implements RegisterScreeningContract.View {
    private RegisterScreeningPresenter presenter;
    private long screeningId = -1;
    private Button registerScreeningButton;
    private Long SmovieId;
    private SharedPreferences myPreferences;

    // --- NUEVAS VARIABLES PARA GUARDAR FECHA Y HORA ---
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private boolean isDateSelected = false;

    // Referencia al EditText antiguo para tus pruebas
    private EditText screeningTimeEditTextLegacy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_screening_view);
        setTitle(getString(R.string.tl_add_screening));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        presenter = new RegisterScreeningPresenter(this);
        registerScreeningButton = findViewById(R.id.add_screening_button);

        // --- INICIALIZACIÓN DE COMPONENTES DE FECHA Y HORA ---
        CalendarView calendarView = findViewById(R.id.screening_add_day);
        EditText screeningHourEditText = findViewById(R.id.screening_add_hour);

        // Guardo Formato la referencia al campo antiguo para las pruebas
        screeningTimeEditTextLegacy = findViewById(R.id.screening_add_time);

        // Inicializo la fecha y hora con los valores actuales por defecto
        Calendar now = Calendar.getInstance();
        selectedYear = now.get(Calendar.YEAR);
        selectedMonth = now.get(Calendar.MONTH);
        selectedDay = now.get(Calendar.DAY_OF_MONTH);
        selectedHour = now.get(Calendar.HOUR_OF_DAY);
        selectedMinute = now.get(Calendar.MINUTE);
        isDateSelected = true;
        updateLegacyField(); // Actualizo el campo antiguo con la hora actual

        // 1. Listener para el CalendarView para capturar la FECHA
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedYear = year;
                selectedMonth = month;
                selectedDay = dayOfMonth;
                isDateSelected = true;
                updateLegacyField(); // Actualizo el campo antiguo
            }
        });

        //El nuevo EditText de la hora hago que no sea editable por teclado
        screeningHourEditText.setFocusable(false);
        screeningHourEditText.setClickable(true);

        // 2. Listener para el EditText de la hora para abrir el TimePickerDialog
        screeningHourEditText.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(RegisterScreeningView.this, (view, hourOfDay, minute) -> {
                selectedHour = hourOfDay;
                selectedMinute = minute;
                // Muestro la hora seleccionada en el nuevo EditText
                screeningHourEditText.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
                updateLegacyField(); // Actualizamos el campo antiguo
            }, selectedHour, selectedMinute, true); // true para formato 24h
            timePickerDialog.show();
        });

        Intent intent = getIntent();
        SmovieId = intent.getLongExtra("screeningMovieId", 0);

        // Lógica para modo ACTUALIZAR
        Screening screening = intent.getParcelableExtra("dataScreening");
        if (screening != null) {
            this.screeningId = screening.getId();
            setTitle(getString(R.string.tx_modificar_sesion));
            registerScreeningButton.setText(getString(R.string.tx_modificar));
            registerScreeningButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));

            // Relleno los campos normales
            EditText screeningTicketEditText = findViewById(R.id.screening_add_ticket);
            screeningTicketEditText.setText(String.valueOf(screening.getTicketPrice()));
            CheckBox subtitledCheckBox = findViewById(R.id.screening_add_subtitled);
            subtitledCheckBox.setChecked(screening.isSubtitled());
            EditText roomEditText = findViewById(R.id.screening_add_id_room);
            roomEditText.setText(String.valueOf(screening.getRoomId()));
            EditText movieEditText = findViewById(R.id.screening_add_id_movie);
            movieEditText.setText(String.valueOf(screening.getMovieId()));

            // --- CÓDIGO PARA RELLENAR FECHA Y HORA AL EDITAR ---
            try {
                LocalDateTime dateTime = LocalDateTime.parse(screening.getScreeningTime());
                selectedYear = dateTime.getYear();
                selectedMonth = dateTime.getMonthValue() - 1;
                selectedDay = dateTime.getDayOfMonth();
                selectedHour = dateTime.getHour();
                selectedMinute = dateTime.getMinute();
                isDateSelected = true;

                Calendar calendar = Calendar.getInstance();
                calendar.set(selectedYear, selectedMonth, selectedDay);
                calendarView.setDate(calendar.getTimeInMillis(), true, true);

                // Pongo la hora en el nuevo EditText
                screeningHourEditText.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));

                // Actualizamos el campo antiguo también
                updateLegacyField();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar2, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //

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

    // campo antiguo para pruebas, Legacy, al final lo voy a dejar, pero en una aplicacion final no estaria.
    private void updateLegacyField() {
        if (isDateSelected && screeningTimeEditTextLegacy != null) {
            String fullDateTimeString = String.format(Locale.ROOT, "%04d-%02d-%02dT%02d:%02d:00",
                    selectedYear, selectedMonth + 1, selectedDay, selectedHour, selectedMinute);
            screeningTimeEditTextLegacy.setText(fullDateTimeString);
        }
    }

    public void registerScreening(View view) {
        if (!isDateSelected) {
            showErrorMessage(getString(R.string.select_date));
            return;
        }

        // --- LÓGICA PARA CONSTRUIR LA CADENA DE FECHA Y HORA ---
        // Construyo la cadena a partir de nuestras variables, no del EditText antiguo
        String screeningTime = String.format(Locale.ROOT, "%04d-%02d-%02dT%02d:%02d:00",
                selectedYear, selectedMonth + 1, selectedDay, selectedHour, selectedMinute);

        //
        EditText screeningTicketEditText = findViewById(R.id.screening_add_ticket);
        double screeningTicket = Double.parseDouble(screeningTicketEditText.getText().toString());
        CheckBox subtitledCheckBox = findViewById(R.id.screening_add_subtitled);
        boolean subtitled = subtitledCheckBox.isChecked();
        EditText roomEditText = findViewById(R.id.screening_add_id_room);
        Long room = Long.parseLong(roomEditText.getText().toString());
        Long RmovieId = SmovieId;
        if (screeningId != -1) {
            EditText movieEditText = findViewById(R.id.screening_add_id_movie);
            RmovieId = Long.parseLong(movieEditText.getText().toString());
        }

        ScreeningIn screeningIn = new ScreeningIn(screeningTime, screeningTicket, subtitled, RmovieId, room);

        if (screeningId != -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.lb_esta_seguro_modificar_sesion)
                    .setPositiveButton(R.string.lb_si,
                            (dialog, which) -> { // Expresión Lambda para simplificar
                                // Modo edición
                                screeningIn.setId(screeningId);
                                presenter.updateScreening(screeningIn);
                            })
                    .setNegativeButton(R.string.lb_no,
                            (dialog, which) -> dialog.dismiss());
            builder.create().show();

        } else {
            presenter.registerScreening(screeningIn);
        }
    }

    @Override
    public void showErrorMessage(String message) {
        if (myPreferences.getBoolean("notifications", false)) {
            Snackbar.make(findViewById(R.id.add_screening_button), message, BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showSuccesMessage(String message) {
        if (myPreferences.getBoolean("notifications", false)) {
            Snackbar.make(findViewById(R.id.add_screening_button), message, BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdateScreeningSuccess() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedScreeningId", screeningId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}