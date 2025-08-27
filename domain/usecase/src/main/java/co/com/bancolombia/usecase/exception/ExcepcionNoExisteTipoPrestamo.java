package co.com.bancolombia.usecase.exception;

public class ExcepcionNoExisteTipoPrestamo extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ExcepcionNoExisteTipoPrestamo(String mensaje) {
        super(mensaje);
    }
}
