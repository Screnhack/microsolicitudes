package co.com.bancolombia.api.solicitud;

import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.usecase.exception.ExcepcionArgumentos;
import co.com.bancolombia.usecase.exception.ExcepcionNoExisteTipoPrestamo;
import co.com.bancolombia.usecase.solicitud.SolicitudListGetAll;
import co.com.bancolombia.usecase.solicitud.SolicitudUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SolicitudHandler {

    private static final Logger log = LoggerFactory.getLogger(SolicitudUseCase.class);
    private final SolicitudUseCase solicitudUseCase;
    private final SolicitudListGetAll solicitudListGetAll;

    public Mono<ServerResponse> listenGETSolicitudUseCase(ServerRequest serverRequest) {

        int page = serverRequest.queryParam("page").map(Integer::parseInt).orElse(0);
        int size = serverRequest.queryParam("size").map(Integer::parseInt).orElse(10);

        Pageable pageable = PageRequest.of(page, size);
        Mono<Long> totalCount = solicitudListGetAll.getDataCount();
        Mono<List<Solicitud>> listSolicitudes = solicitudListGetAll.getPaginatedSolicitudes();
        Mono<Page<Solicitud>> listPaginate = Mono.zip(listSolicitudes, totalCount)
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));

        return listPaginate
                .flatMap(pageResult ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(pageResult));
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }

    @Operation(
            operationId = "saveSolicitud",
            summary = "Crear una nueva solicitud",
            description = "Guarda una nueva solicitud de préstamo. Se requiere un objeto Solicitud en el cuerpo de la petición.",
            tags = {"Solicitudes"},
            requestBody = @RequestBody(
                    description = "Datos de la solicitud a crear",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Solicitud.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Solicitud creada exitosamente",
                            content = @Content(schema = @Schema(implementation = Solicitud.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "La solicitud es inválida o el tipo de préstamo no existe.",
                            content = @Content(schema = @Schema(implementation = String.class))
                    )
            }
    )
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
