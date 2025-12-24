package com.svalero.cinemav2.presenter;


import com.svalero.cinemav2.contract.ScreeningListContract;
import com.svalero.cinemav2.domain.Screening;
import com.svalero.cinemav2.model.ScreeningListModel;

import java.util.List;

public class ScreeningListPresenter implements ScreeningListContract.Presenter, ScreeningListContract.Model.OnLoadScreeningsListener {
    private ScreeningListContract.View view;
    private ScreeningListContract.Model model;

    public ScreeningListPresenter(ScreeningListContract.View view){
        //La vista viene creada por la propia activity aqui se la paso, y se la paso aqui como parametro
        this.view = view;
        //El model como no existe me lo creo
        model = new ScreeningListModel();
    }
    @Override
    public void loadScreenings() {
        model.loadScreenings(this);
    }

    @Override
    public void onLoadScreeningsSuccess(List<Screening> screeningList) {
        view.listScreenings(screeningList);

    }

    @Override
    public void onLoadScreeningsError(String message) {
        view.showErrorMessage(message);

    }
}
