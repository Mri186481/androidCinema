package com.svalero.cinemav2.contract;


import com.svalero.cinemav2.domain.Screening;


public interface ScreeningDetailContract {
        interface Model {
            interface OnLoadScreeningDetailListener {

                void onLoadScreeningDetailSuccess(Screening screening);

                void onLoadScreeningDetailError(String message);
            }
            void loadScreeningDetail(long screeningId, OnLoadScreeningDetailListener listener);
        }

        interface View {
            void showScreeningDetail(Screening screening);

            void showErrorMessage(String message);
            void showSuccessMessage(String message);
        }

        interface Presenter {
            void loadScreeningDetail(long screeningId);
        }
    }

