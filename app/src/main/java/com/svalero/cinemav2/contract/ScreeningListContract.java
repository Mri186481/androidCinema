package com.svalero.cinemav2.contract;


import com.svalero.cinemav2.domain.Screening;

import java.util.List;

public interface ScreeningListContract {

    interface Model {
        interface OnLoadScreeningsListener{
            void onLoadScreeningsSuccess(List<Screening> screeningList);
            void onLoadScreeningsError(String message);
        }
        void loadScreenings(OnLoadScreeningsListener listener);
    }

    interface View {
        void listScreenings(List<Screening> screeningList);
        void showErrorMessage(String message);
        void showSuccesMessage(String message);
    }
    interface Presenter {
        void loadScreenings();
    }
}
