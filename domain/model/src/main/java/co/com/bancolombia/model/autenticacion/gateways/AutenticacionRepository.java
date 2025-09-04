package co.com.bancolombia.model.autenticacion.gateways;

import reactor.core.publisher.Mono;

public interface AutenticacionRepository {
    /**
     * Permite validar si el
     * @param correo
     * @return
     */
    Mono<Boolean> validacionCorreo(String correo);
}
