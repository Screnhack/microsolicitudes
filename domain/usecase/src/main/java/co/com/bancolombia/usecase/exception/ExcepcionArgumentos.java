package co.com.bancolombia.usecase.exception;

public class ExcepcionArgumentos extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ExcepcionArgumentos(String mensaje) {
        super(mensaje);
    }
}
