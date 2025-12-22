package com.svalero.cinemav2.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.svalero.cinemav2.R;

public class RegisterScreeningView extends AppCompatActivity {

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

        Intent intent = getIntent();

        Long SmovieId = intent.getLongExtra("screeningMovieId", 0);
        String SmovieTitle = intent.getStringExtra("screeningMovieTitle");


        printScreening(SmovieId,SmovieTitle );
    }

    private void printScreening(Long id, String title) {
        ((TextView) findViewById(R.id.screening_movie_id)).setText(String.valueOf(id));
        ((TextView) findViewById(R.id.screening_movie_title)).setText(title);

    }
}