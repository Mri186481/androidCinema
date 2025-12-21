package com.svalero.cinemav2.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesApi {
    public static MoviesApiInterface buildInstance() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        //el metodo que va permitir instanciar esa api, o esos metodos/interfaces java que llaman a la API
        //Le estoy diciendo que hay una API que esta escuchando en esa URL y que tiene las operaciones
        //definidas em MoviesApiInterface
        Retrofit retrofit = new Retrofit.Builder()
                //como la estoy ejecutando en un emulador, la ip de mi ordenador no le vale,
                //ya que no la puede ver ya que el emulador y el ordenador no estan en la misma red
                //Hay una IP especifica para el emulador que significa el ordenador donde estoy funcionando
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(MoviesApiInterface.class);
    }
}