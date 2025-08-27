package co.com.bancolombia.usecase.validator;

import co.com.bancolombia.usecase.exception.ExcepcionArgumentos;

public class argumentValidator {

    private argumentValidator() {

    }

    public static void validarRequeridos(Object valor, String mensaje) {
        if (valor == null) {
            throw new ExcepcionArgumentos(mensaje);
        }

        if (valor instanceof String && ((String) valor).isEmpty()) {
            throw new ExcepcionArgumentos(mensaje);
        }
    }

    public static <T extends Number & Comparable<T>> void validarNumeroMayorACero(T valor, String mensaje) {
        if (valor == null || valor.compareTo((T) Long.valueOf(0)) <= 0) {
            throw new ExcepcionArgumentos(mensaje);
        }
    }

}
