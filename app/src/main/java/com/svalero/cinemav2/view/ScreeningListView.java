package com.svalero.cinemav2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.cinemav2.R;
import com.svalero.cinemav2.adapter.ScreeningAdapter;
import com.svalero.cinemav2.contract.ScreeningListContract;
import com.svalero.cinemav2.domain.Screening;
import com.svalero.cinemav2.presenter.ScreeningListPresenter;

import java.util.ArrayList;
import java.util.List;

public class ScreeningListView extends AppCompatActivity implements ScreeningListContract.View {
    private ScreeningAdapter screeningAdapter;
    private List<Screening> screeningList;
    private ScreeningListContract.Presenter presenter;

    private SharedPreferences myPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screening_list_view);
        setTitle(getString(R.string.tl_screenings));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        presenter = new ScreeningListPresenter(this);
//        presenter.loadScreenings();
        screeningList = new ArrayList<>();

        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        RecyclerView screeningsView = findViewById(R.id.screenings_view);
        screeningsView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((this));
        screeningsView.setLayoutManager(linearLayoutManager);

        screeningAdapter = new ScreeningAdapter(screeningList);
        screeningsView.setAdapter(screeningAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //la Activity viene del segundo plano: Refrescar la lista por si ha habido algún cambio
        screeningList.clear();
        presenter.loadScreenings();
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
    @Override
    public void listScreenings(List<Screening> screeningList) {
        //en mi lista de aqui privada añado lo que viene ahora que viene del model
        this.screeningList.addAll(screeningList);
        //le paso la lista a mi adapter para que la pinte
        screeningAdapter.notifyDataSetChanged();

    }

    @Override
    public void showErrorMessage(String message) {
        if (myPreferences.getBoolean("notifications", false)) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void showSuccesMessage(String message) {
        if (myPreferences.getBoolean("notifications", false)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

    }
}