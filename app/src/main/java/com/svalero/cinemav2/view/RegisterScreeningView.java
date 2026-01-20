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
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private boolean isDateSelected = false;

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

        CalendarView calendarView = findViewById(R.id.screening_add_day);
        EditText screeningHourEditText = findViewById(R.id.screening_add_hour);

        screeningTimeEditTextLegacy = findViewById(R.id.screening_add_time);

        Calendar now = Calendar.getInstance();
        selectedYear = now.get(Calendar.YEAR);
        selectedMonth = now.get(Calendar.MONTH);
        selectedDay = now.get(Calendar.DAY_OF_MONTH);
        selectedHour = now.get(Calendar.HOUR_OF_DAY);
        selectedMinute = now.get(Calendar.MINUTE);
        isDateSelected = true;
        updateLegacyField();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedYear = year;
                selectedMonth = month;
                selectedDay = dayOfMonth;
                isDateSelected = true;
                updateLegacyField();
            }
        });

        screeningHourEditText.setFocusable(false);
        screeningHourEditText.setClickable(true);

        screeningHourEditText.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(RegisterScreeningView.this, (view, hourOfDay, minute) -> {
                selectedHour = hourOfDay;
                selectedMinute = minute;
                screeningHourEditText.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
                updateLegacyField();
            }, selectedHour, selectedMinute, true);
            timePickerDialog.show();
        });

        Intent intent = getIntent();
        SmovieId = intent.getLongExtra("screeningMovieId", 0);
        EditText movieEditText = findViewById(R.id.screening_add_id_movie);
        if (SmovieId != null && SmovieId > 0) {
            movieEditText.setText(String.valueOf(SmovieId));
        }

        // Lógica para modo ACTUALIZAR
        Screening screening = intent.getParcelableExtra("dataScreening");
        if (screening != null) {
            this.screeningId = screening.getId();
            setTitle(getString(R.string.tx_modificar_sesion));
            registerScreeningButton.setText(getString(R.string.tx_modificar));
            registerScreeningButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));

            EditText screeningTicketEditText = findViewById(R.id.screening_add_ticket);
            screeningTicketEditText.setText(String.valueOf(screening.getTicketPrice()));
            CheckBox subtitledCheckBox = findViewById(R.id.screening_add_subtitled);
            subtitledCheckBox.setChecked(screening.isSubtitled());
            EditText roomEditText = findViewById(R.id.screening_add_id_room);
            roomEditText.setText(String.valueOf(screening.getRoomId()));
            movieEditText.setText(String.valueOf(screening.getMovieId()));

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
                screeningHourEditText.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
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

        String screeningTime = String.format(Locale.ROOT, "%04d-%02d-%02dT%02d:%02d:00",
                selectedYear, selectedMonth + 1, selectedDay, selectedHour, selectedMinute);

        //
        EditText screeningTicketEditText = findViewById(R.id.screening_add_ticket);
        String ticketText = screeningTicketEditText.getText().toString();
        double screeningTicket = 0.0;
        if (!ticketText.isEmpty()) {
            try {
                screeningTicket = Double.parseDouble(ticketText);
            } catch (NumberFormatException e) {
                screeningTicket = 0.0;
            }
        }
        //
        CheckBox subtitledCheckBox = findViewById(R.id.screening_add_subtitled);
        boolean subtitled = subtitledCheckBox.isChecked();
        //
        EditText roomEditText = findViewById(R.id.screening_add_id_room);
        String roomText = roomEditText.getText().toString();
        Long room = 0L;
        if (!roomText.isEmpty()) {
            try {
                room = Long.parseLong(roomText);
            } catch (NumberFormatException e) {
                room = 0L;
            }
        }
        //
        EditText movieEditText = findViewById(R.id.screening_add_id_movie);
        String movieText = movieEditText.getText().toString();
        Long RmovieId = 0L;
        if (!movieText.isEmpty()) {
            try {
                RmovieId = Long.parseLong(movieText);
            } catch (NumberFormatException e) {
                RmovieId = 0L;
            }
        }

        if (RmovieId == 0 && SmovieId != null && SmovieId > 0) {
            RmovieId = SmovieId;
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