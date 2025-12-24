package com.svalero.cinemav2.contract;


import com.svalero.cinemav2.domain.Screening;

import java.util.List;

public interface ScreeningListContract {


    interface Model {
        interface OnLoadScreeningsListener{
            //Si va bien este metodo(Succes) recibira la lista de movies
            void onLoadScreeningsSuccess(List<Screening> screeningList);
            //Si va makl este fallara y devovera un mensaje de error
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
