package co.com.bancolombia.api.solicitud;

import co.com.bancolombia.api.solicitud.testdatabuilder.SolicitudTestDataBuilder;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.usecase.exception.ExcepcionArgumentos;
import co.com.bancolombia.usecase.exception.ExcepcionNoExisteTipoPrestamo;
import co.com.bancolombia.usecase.solicitud.SolicitudUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest
@ContextConfiguration(classes =   {SolicitudRouterRest.class, SolicitudHandler.class})
public class SolicitudHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SolicitudUseCase solicitudUseCase;

    private SolicitudTestDataBuilder solicitudBuilder;

    @BeforeEach
    public void setUp() {
        solicitudBuilder = new SolicitudTestDataBuilder();
    }

    // --- Caso de prueba: Éxito ---
    @Test
    @DisplayName("Debería guardar una solicitud y devolver 200 OK")
    void shouldSaveSolicitudAndReturnOk() {
        // Arrange: Crea una solicitud y configura el mock para que la devuelva
        Solicitud solicitud = solicitudBuilder.build();
        when(solicitudUseCase.save(any(Solicitud.class))).thenReturn(Mono.just(solicitud));

        // Act & Assert: Simula la petición y valida la respuesta
        webTestClient.post().uri("/api/v1/solicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(solicitud)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Solicitud.class)
                .consumeWith(response -> {
                    // Valida que el objeto de la respuesta tenga los campos correctos
                    Solicitud returnedSolicitud = response.getResponseBody();
                    assertThat(returnedSolicitud.getId_solicitud()).isEqualTo(solicitud.getId_solicitud());
                    assertThat(returnedSolicitud.getMonto()).isEqualTo(solicitud.getMonto());
                    assertThat(returnedSolicitud.getEmail()).isEqualTo(solicitud.getEmail());
                });
    }

    // --- Caso de prueba: Excepción por argumentos inválidos ---
    @Test
    @DisplayName("Debería devolver 400 BAD_REQUEST para argumentos inválidos")
    void shouldReturnBadRequestForInvalidArguments() {
        // Arrange: Configura el mock para que lance la excepción esperada
        when(solicitudUseCase.save(any(Solicitud.class)))
                .thenReturn(Mono.error(new ExcepcionArgumentos("El monto solicitado no puede ser nulo o vacío")));

        // Act & Assert: Simula la petición y valida el error
        webTestClient.post().uri("/api/v1/solicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(solicitudBuilder.build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("El monto solicitado no puede ser nulo o vacío");
    }

    // --- Caso de prueba: Excepción de tipo de préstamo no existente ---
    @Test
    @DisplayName("Debería devolver 400 BAD_REQUEST para un tipo de préstamo inexistente")
    void shouldReturnBadRequestForNonExistingLoanType() {
        // Arrange: Configura el mock para que lance la excepción específica
        when(solicitudUseCase.save(any(Solicitud.class)))
                .thenReturn(Mono.error(new ExcepcionNoExisteTipoPrestamo("El tipo de préstamo no es válido")));

        // Act & Assert: Simula la petición y valida el mensaje de error
        webTestClient.post().uri("/api/v1/solicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(solicitudBuilder.build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("El tipo de préstamo no es válido");
    }
}