package co.com.bancolombia.api.solicitud;

import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.usecase.exception.ExcepcionArgumentos;
import co.com.bancolombia.usecase.exception.ExcepcionNoExisteTipoPrestamo;
import co.com.bancolombia.usecase.solicitud.SolicitudUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SolicitudHandler {

    private static final Logger log = LoggerFactory.getLogger(SolicitudUseCase.class);
    private final SolicitudUseCase solicitudUseCase;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenSaveSolicitudPOSTUseCase(ServerRequest serverRequest) {
        log.info("Petición recibida para guardar una nueva Solicitud");
        return serverRequest.bodyToMono(Solicitud.class)
                .doOnNext(solicitud -> log.debug("Procesando la petición para la Solicitud con email: {}", solicitud.getEmail()))
                .flatMap(solicitudUseCase::save)
                .flatMap(solicitud -> {
                    log.info("Solicitud guardada exitosamente con ID: {}", solicitud.getId_solicitud());
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(solicitud);
                })
                .onErrorResume(ExcepcionArgumentos.class, e -> {
                    log.error("La validación fallo: {}", e.getMessage());
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(e.getMessage());
                }).onErrorResume(ExcepcionNoExisteTipoPrestamo.class, e -> {
                    log.error("La validación de la Solicitud falló: {}", e.getMessage());
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(e.getMessage());
                });
    }
}
