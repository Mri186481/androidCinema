package com.svalero.cinemav2.model;

import com.svalero.cinemav2.api.ScreeningsApi;
import com.svalero.cinemav2.api.ScreeningsApiInterface;
import com.svalero.cinemav2.contract.ScreeningDetailContract;
import com.svalero.cinemav2.domain.Screening;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScreeningDetailModel implements ScreeningDetailContract.Model {
    @Override
    public void loadScreeningDetail(long screeningId, OnLoadScreeningDetailListener listener) {
        ScreeningsApiInterface apiInterface = ScreeningsApi.buildInstance();
        Call<Screening> callScreening = apiInterface.getScreening(screeningId);
        callScreening.enqueue(new Callback<Screening>() {
            @Override
            public void onResponse(Call<Screening> call, Response<Screening> response) {
                if (response.isSuccessful()) {
                    Screening screening = response.body();
                    if (screening != null) {
                        listener.onLoadScreeningDetailSuccess(screening);
                    } else {
                        listener.onLoadScreeningDetailError("Sesion no encontrada.");
                    }
                } else {
                    String errorMessage = "Error en la respuesta de la API: " + response.code();
                    listener.onLoadScreeningDetailError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Screening> call, Throwable t) {
                String errorMessage = "Error al conectar con la API: " + t.getMessage();
                listener.onLoadScreeningDetailError(errorMessage);
            }
        });


    }
}
