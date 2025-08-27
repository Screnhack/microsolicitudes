package co.com.bancolombia.usecase.solicitud;

import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import co.com.bancolombia.model.tipoprestamo.TipoPrestamo;
import co.com.bancolombia.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.bancolombia.usecase.exception.ExcepcionArgumentos;
import co.com.bancolombia.usecase.exception.ExcepcionNoExisteTipoPrestamo;
import co.com.bancolombia.usecase.validator.argumentValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudUseCase {

    public static final String EL_TIPO_DE_PRESTAMO_NO_ES_VALIDO = "El tipo de préstamo no es válido";
    public static final String EL_DOCUMENTO_DEL_CLIENTE_NO_PUEDE_SER_NULO = "El documento de identidad del cliente no puede ser nulo o vacio ";
    public static final String EL_MONTO_SOLICITADO_NO_PUEDE_SER_NULO = "El monto solicitado no puede ser nulo o vacio ";
    public static final String EL_PLAZO_SOLICITADO_NO_PUEDE_SER_NULO = "El plazo solicitado no puede ser nulo o vacio ";
    public static final String PENDIENTE_DE_REVISION = "Pendiente de revisión";

    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;

    public Mono<Solicitud> save(Solicitud solicitud) {
        try {
            argumentValidator.validarRequeridos(solicitud.getDocumentoIdentidad(), EL_DOCUMENTO_DEL_CLIENTE_NO_PUEDE_SER_NULO);
            argumentValidator.validarNumeroMayorACero(solicitud.getMonto(), EL_MONTO_SOLICITADO_NO_PUEDE_SER_NULO);
            argumentValidator.validarNumeroMayorACero(solicitud.getPlazo(), EL_PLAZO_SOLICITADO_NO_PUEDE_SER_NULO);
        } catch (ExcepcionArgumentos e) {
            return Mono.error(e);
        }
        return validacionTipoPrestamo(solicitud)
                .flatMap(tipoPrestamoExiste -> {
                    if (Boolean.TRUE.equals(tipoPrestamoExiste)) {
                        solicitud.setEstado(PENDIENTE_DE_REVISION);
                        return solicitudRepository.save(solicitud);
                    } else {
                        return Mono.error(new ExcepcionNoExisteTipoPrestamo(EL_TIPO_DE_PRESTAMO_NO_ES_VALIDO));
                    }
                });
    }

    private Mono<Boolean> validacionTipoPrestamo(Solicitud solicitud) {
        return tipoPrestamoRepository.findAllTipoPrestamo()
                .map(TipoPrestamo::getNombre)
                .any(nombre -> nombre.equals(solicitud.getTipoPrestamo()));
    }

}
