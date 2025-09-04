package co.com.bancolombia.api.solicitud;

import co.com.bancolombia.model.solicitud.Solicitud;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SolicitudRouterRest {

    @Bean
    @RouterOperations({

            @RouterOperation(
                    path = "/api/v1/solicitud",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = SolicitudHandler.class,
                    beanMethod = "listenSaveSolicitudPOSTUseCase"
            ),
            @RouterOperation(
                    path = "/api/solicitud/get",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = SolicitudHandler.class,
                    beanMethod = "listenGETUseCase",
                    operation = @Operation(
                            summary = "Obtener solicitudes",
                            tags = {"Solicitudes"}
                    )
            ),
            @RouterOperation(
                    path = "/api/solicitud/get-other",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = SolicitudHandler.class,
                    beanMethod = "listenGETOtherUseCase",
                    operation = @Operation(
                            summary = "Obtener otras solicitudes",
                            tags = {"Solicitudes"}
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(SolicitudHandler handler) {
        return route(POST("/api/v1/solicitud").and(accept(MediaType.APPLICATION_JSON)), handler::listenSaveSolicitudPOSTUseCase)
                .andRoute(GET("/api/solicitud/get"), handler::listenGETUseCase)
                .andRoute(GET("/api/solicitud/get-other"), handler::listenGETOtherUseCase);
    }
}
