package com.svalero.cinemav2.contract;

import com.svalero.cinemav2.domain.ScreeningIn;

public interface RegisterScreeningContract {


    interface Model {
        interface OnRegisterScreeningListener{
            void onRegisterScreeningSuccess(ScreeningIn registeredScreening);
            void onRegisterScreeningError(String message);
            //Metodos actualizacion
            void onUpdateScreeningSuccess(ScreeningIn updatedScreening);
            void onUpdateScreeningError(String message);

        }
        void registerScreening(ScreeningIn screeningIn, OnRegisterScreeningListener listener);
        void updateScreening(ScreeningIn screeningIn, OnRegisterScreeningListener listener);


    }

    interface View {

        void showErrorMessage(String message);
        void showSuccesMessage(String message);
        //metodo para manejar el resultado de una actualizacion exitosa
        void onUpdateScreeningSuccess();




    }

    interface Presenter {
        void registerScreening(ScreeningIn screeningIn);
        // para actualizar una película
        void updateScreening(ScreeningIn screeningIn);


    }
}
