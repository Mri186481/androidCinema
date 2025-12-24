package com.svalero.cinemav2.model;

import android.util.Log;

import com.svalero.cinemav2.api.ScreeningsApi;
import com.svalero.cinemav2.api.ScreeningsApiInterface;
import com.svalero.cinemav2.contract.RegisterScreeningContract;
import com.svalero.cinemav2.domain.ScreeningIn;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterScreeningModel implements RegisterScreeningContract.Model {
    @Override
    public void registerScreening(ScreeningIn screeningIn, OnRegisterScreeningListener listener) {
        ScreeningsApiInterface screeningsApi = ScreeningsApi.buildInstance();
        //Ahora preparo la llamada
        Call<ScreeningIn> callRegisterScreening = screeningsApi.addScreening(screeningIn);
        //Ahora HAGO la llamada, teclea new dentro del parentesis e intro
        callRegisterScreening.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ScreeningIn> call, Response<ScreeningIn> response) {
                switch (response.code()) {
                    case 201:
                        listener.onRegisterScreeningSuccess((response.body()));
                        break;
                    case 400:
                        //Hacer aqui lo de extraer los recursos de cadena para los literales
                        listener.onRegisterScreeningError("Error validando la peticion" + response.message());
                        break;
                    case 500:
                        listener.onRegisterScreeningError("Error interno en la API" +response.message());
                        break;
                    default:
                        listener.onRegisterScreeningError("Error invocando a la API" +response.message());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ScreeningIn> call, Throwable t) {
                //Si va mal se ejecutara este otro, que es que me pasan el mensaje de error
                Log.e("API_FAILURE", "Error de la API: " + t.getMessage(), t);
                listener.onRegisterScreeningError("No se puede conectar con el origen de los datos " + t.getMessage());
            }
        });

    }

    @Override
    public void updateScreening(ScreeningIn screeningIn, OnRegisterScreeningListener listener) {
        ScreeningsApiInterface screeningsApi = ScreeningsApi.buildInstance();
        //Ahora preparo la llamada
        Call<ScreeningIn> callUpdateScreening = screeningsApi.putScreening(screeningIn.getId(),screeningIn);
        //Ahora HAGO la llamada, teclea new dentro del parentesis e intro
        callUpdateScreening.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ScreeningIn> call, Response<ScreeningIn> response) {
                switch (response.code()) {
                    case 200:
                        listener.onUpdateScreeningSuccess((response.body()));
                        break;
                    case 204:
                        listener.onUpdateScreeningSuccess((response.body()));
                        break;
                    case 400:
                        //Hacer aqui lo de extraer los recursos de cadena para los literales
                        listener.onUpdateScreeningError("Error validando la peticion" + response.message());
                        break;
                    case 404:
                        listener.onUpdateScreeningError("Sesion no encontrada. No se pudo actualizar. Id: " + screeningIn.getId());
                        break;
                    case 500:
                        listener.onUpdateScreeningError("Error interno en la API" +response.message());
                        break;
                    default:
                        listener.onUpdateScreeningError("Error invocando a la API" +response.message());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ScreeningIn> call, Throwable t) {
                //Si va mal se ejecutara este otro, que es que me pasan el mensaje de error
                Log.e("API_FAILURE", "Error de la API: " + t.getMessage(), t);
                listener.onUpdateScreeningError("No se puede conectar con el origen de los datos " + t.getMessage());
            }
        });

    }


}
