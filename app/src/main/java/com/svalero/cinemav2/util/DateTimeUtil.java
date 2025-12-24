package com.svalero.cinemav2.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtil {
    // El formateador que coincide exactamente con el string de tu API: "2025-10-30T22:45:00"
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Convierte un String con formato ISO a un objeto LocalDateTime.
     *
     * @param dateTimeString El string recibido de la API.
     * @return un objeto LocalDateTime, o null si el string es inválido.
     */
    public static LocalDateTime stringToLocalDateTime(String dateTimeString) {
        if (dateTimeString == null) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeString, formatter);
        } catch (DateTimeParseException e) {
            // Opcional: imprimir el error para depuración
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Convierte un objeto LocalDateTime al formato String que espera la API.
     * Esto es lo que necesitas para tus peticiones PUT o POST.
     *
     * @param localDateTime El objeto fecha/hora de tu app.
     * @return un String formateado para la API, o null si la entrada es null.
     */
    public static String localDateTimeToString(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(formatter);
    }
}
//ejemplos de uso:
// EJEMPLO 1:
    // En tu Activity o ViewModel, después de una llamada a la API exitosa
    //public void displayScreening(ScreeningOutDto screening) {
        // 1. Obtienes el String directamente del objeto
    //    String screeningTimeString = screening.getScreeningTime();
    //    System.out.println("Fecha recibida de la API: " + screeningTimeString);

        // 2. Lo conviertes a un objeto LocalDateTime para poder manipularlo
    //    LocalDateTime screeningDateTime = DateTimeUtil.stringToLocalDateTime(screeningTimeString);

        // 3. Ahora puedes trabajar con él de forma segura
//        if (screeningDateTime != null) {
//            String infoToShow = "La película '" + screening.getMovieTitle() +
//                    "' es el día " + screeningDateTime.getDayOfMonth() +
//                    " a las " + screeningDateTime.getHour() + ":" + screeningDateTime.getMinute();
//
//            // textView.setText(infoToShow);
//            System.out.println(infoToShow);
//        }
//EJEMPLO 2:
        // En tu Activity o ViewModel, cuando el usuario quiere actualizar una proyección

            // 1. Imagina que obtienes una nueva fecha y hora de un selector (DatePicker/TimePicker)--->estudiar esto, debe de ser un elemento ya hecho
           // LocalDateTime newScreeningTime = LocalDateTime.of(2025, 11, 15, 20, 0); // 15 de Nov de 2025 a las 20:00

            // 2. Conviertes ese objeto LocalDateTime al formato String que la API necesita
            //String formattedStringForApi = DateTimeUtil.localDateTimeToString(newScreeningTime);









