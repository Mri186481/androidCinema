package com.svalero.cinemav2.model;

import android.util.Log;

import com.svalero.cinemav2.api.ScreeningsApi;
import com.svalero.cinemav2.api.ScreeningsApiInterface;
import com.svalero.cinemav2.contract.ScreeningListContract;
import com.svalero.cinemav2.domain.Screening;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScreeningListModel implements ScreeningListContract.Model {
    @Override
    public void loadScreenings(OnLoadScreeningsListener listener) {
        ScreeningsApiInterface screeningsApi = ScreeningsApi.buildInstance();
        Call<List<Screening>> getScreeningsCall = screeningsApi.getScreenings();
        getScreeningsCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Screening>> call, Response<List<Screening>> response) {
                if (response.code() == 200) {
                    listener.onLoadScreeningsSuccess(response.body());
                } else if (response.code() == 500) {
                    listener.onLoadScreeningsError("La API no se encuentra disponible en este momento. Prueba de nuevo");
                } else {
                    listener.onLoadScreeningsError(String.valueOf(response.code()));
                }
            }
            @Override
            public void onFailure(Call<List<Screening>> call, Throwable t) {
                Log.e("API_FAILURE", "Error de la API: " + t.getMessage(), t);
                listener.onLoadScreeningsError(t.getMessage());
            }
        });


    }
}
