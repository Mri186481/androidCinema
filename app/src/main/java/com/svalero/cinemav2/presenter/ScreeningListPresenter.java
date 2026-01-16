package com.svalero.cinemav2.presenter;


import com.svalero.cinemav2.contract.ScreeningListContract;
import com.svalero.cinemav2.domain.Screening;
import com.svalero.cinemav2.model.ScreeningListModel;

import java.util.List;

public class ScreeningListPresenter implements ScreeningListContract.Presenter, ScreeningListContract.Model.OnLoadScreeningsListener {
    private ScreeningListContract.View view;
    private ScreeningListContract.Model model;

    public ScreeningListPresenter(ScreeningListContract.View view){
        this.view = view;
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
