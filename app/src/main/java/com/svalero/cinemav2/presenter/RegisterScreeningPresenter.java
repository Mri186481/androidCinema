package com.svalero.cinemav2.presenter;

import com.svalero.cinemav2.contract.RegisterScreeningContract;
import com.svalero.cinemav2.domain.ScreeningIn;
import com.svalero.cinemav2.model.RegisterScreeningModel;

public class RegisterScreeningPresenter implements RegisterScreeningContract.Presenter, RegisterScreeningContract.Model.OnRegisterScreeningListener {
    private RegisterScreeningContract.Model model;
    private RegisterScreeningContract.View view;
    //Ahora el costructor..
    public RegisterScreeningPresenter(RegisterScreeningContract.View view){
        model = new RegisterScreeningModel();
        this.view = view;

    }

    @Override
    public void registerScreening(ScreeningIn screeningin) {
        //Aqui puedo comprobar los campos, no en la vista
        if (screeningin.getScreeningTime().isEmpty()){
            view.showErrorMessage("La fecha de la sesion no puede estar vacia");
            return;
        }

        if (screeningin.getTicketPrice() <= 0){
            view.showErrorMessage("El precio de la entrada tiene que ser mayor que 0");
            return;
        }
        if (screeningin.getRoomId() <= 0) {
            view.showErrorMessage("Debe de seleccionar un numero de sala positivo");
            return;
        }

        model.registerScreening(screeningin, this);

    }

    @Override
    public void updateScreening(ScreeningIn screeningIn) {
        if (screeningIn.getScreeningTime().isEmpty()){
            view.showErrorMessage("La fecha de la sesion no puede estar vacia");
            return;
        }

        if (screeningIn.getTicketPrice() <= 0){
            view.showErrorMessage("El precio de la entrada tiene que ser mayor que 0");
            return;
        }
        model.updateScreening(screeningIn, this);


    }


    @Override
    public void onRegisterScreeningSuccess(ScreeningIn registeredScreening) {
        view.showSuccesMessage("Sesion registrada correctamente");
    }

    @Override
    public void onRegisterScreeningError(String message) {
        view.showErrorMessage(message);

    }

    @Override
    public void onUpdateScreeningSuccess(ScreeningIn updatedScreening) {
        view.showSuccesMessage("Película actualizada correctamente con el identificador " + updatedScreening.getId());
        // Medodo para actualizar la vista
        ((RegisterScreeningContract.View) view).onUpdateScreeningSuccess();


    }

    @Override
    public void onUpdateScreeningError(String message) {
        view.showErrorMessage(message);
    }
}
