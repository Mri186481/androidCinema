package com.svalero.cinemav2.api;

import com.svalero.cinemav2.domain.Screening;
import com.svalero.cinemav2.domain.ScreeningIn;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ScreeningsApiInterface {

    // son como los query methods del repository de spring-boot pero sin la barra /
    @GET("screenings")
    Call<List<Screening>> getScreenings();

    @GET("screenings/{id}")
    Call<Screening> getScreening(@Path("id") long id);

    @POST("screenings")
    Call<ScreeningIn> addScreening(@Body ScreeningIn screeningIn);

    @DELETE("screenings/{id}")
    Call<Void> deleteScreening(@Path("id") long id);

    //
    @PUT("screenings/{id}")
    Call<ScreeningIn> putScreening(@Path("id") long screeningId, @Body ScreeningIn screeningIn);



}
