package com.svalero.cinemav2.presenter;

import com.svalero.cinemav2.contract.ScreeningDetailContract;
import com.svalero.cinemav2.domain.Screening;

public class ScreeningDetailPresenter implements ScreeningDetailContract.Presenter, ScreeningDetailContract.Model.OnLoadScreeningDetailListener{
    private ScreeningDetailContract.View view;
    private ScreeningDetailContract.Model model;

    public ScreeningDetailPresenter(ScreeningDetailContract.View view, ScreeningDetailContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void loadScreeningDetail(long screeningId) {

        model.loadScreeningDetail(screeningId, this);

    }

    @Override
    public void onLoadScreeningDetailSuccess(Screening screening) {
        view.showScreeningDetail(screening);
        view.showSuccessMessage("Detalles de la sesion cargados correctamente.");

    }

    @Override
    public void onLoadScreeningDetailError(String message) {
        view.showErrorMessage(message);

    }
}
