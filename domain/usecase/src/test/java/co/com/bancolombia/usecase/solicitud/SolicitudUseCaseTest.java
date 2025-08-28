package co.com.bancolombia.usecase.solicitud;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import co.com.bancolombia.model.tipoprestamo.TipoPrestamo;
import co.com.bancolombia.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.bancolombia.usecase.exception.ExcepcionArgumentos;
import co.com.bancolombia.usecase.exception.ExcepcionNoExisteTipoPrestamo;
import co.com.bancolombia.usecase.solicitud.testdatabuilder.SolicitudTestDataBuilder;
import co.com.bancolombia.usecase.tipoprestamo.testdatabuilder.TipoPrestamoTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private TipoPrestamoRepository tipoPrestamoRepository;

    @InjectMocks
    private SolicitudUseCase solicitudUseCase;

    private SolicitudTestDataBuilder solicitudBuilder;
    private TipoPrestamoTestDataBuilder tipoPrestamoBuilder;

    @BeforeEach
    void setUp() {
        solicitudBuilder = new SolicitudTestDataBuilder();
        tipoPrestamoBuilder = new TipoPrestamoTestDataBuilder();
    }

    @Test
    @DisplayName("Debería guardar una solicitud exitosamente con tipo de préstamo válido")
    void saveSolicitud_success() {
        // Arrange
        Solicitud solicitud = solicitudBuilder.withTipoPrestamo("PRESTAMO_PERSONAL").build();
        TipoPrestamo tipoPrestamo = tipoPrestamoBuilder.withNombre("PRESTAMO_PERSONAL").build();

        when(tipoPrestamoRepository.findAllTipoPrestamo()).thenReturn(Flux.just(tipoPrestamo));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(Mono.just(solicitud));

        // Act
        Mono<Solicitud> result = solicitudUseCase.save(solicitud);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(savedSolicitud ->
                        savedSolicitud.getEstado().equals("Pendiente de revisión") &&
                                savedSolicitud.getDocumentoIdentidad().equals(solicitud.getDocumentoIdentidad()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionArgumentos si el documento de identidad es nulo")
    void saveSolicitud_documentoIdentidadNulo_shouldThrowException() {
        // Arrange
        Solicitud solicitud = solicitudBuilder.withDocumentoIdentidad(null).build();

        // Act
        Mono<Solicitud> result = solicitudUseCase.save(solicitud);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof ExcepcionArgumentos &&
                        e.getMessage().equals("El documento de identidad del cliente no puede ser nulo o vacio "))
                .verify();
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionArgumentos si el monto es cero o menor")
    void saveSolicitud_montoNoValido_shouldThrowException() {
        // Arrange
        Solicitud solicitud = solicitudBuilder.withMonto(0L).build();

        // Act
        Mono<Solicitud> result = solicitudUseCase.save(solicitud);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof ExcepcionArgumentos &&
                        e.getMessage().equals("El monto solicitado no puede ser nulo o vacio "))
                .verify();
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionArgumentos si el plazo es cero o menor")
    void saveSolicitud_plazoNoValido_shouldThrowException() {
        // Arrange
        Solicitud solicitud = solicitudBuilder.withPlazo(0).build();

        // Act
        Mono<Solicitud> result = solicitudUseCase.save(solicitud);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof ExcepcionArgumentos &&
                        e.getMessage().equals("El plazo solicitado no puede ser nulo o vacio "))
                .verify();
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionNoExisteTipoPrestamo si el tipo de préstamo no es válido")
    void saveSolicitud_tipoPrestamoNoValido_shouldThrowException() {
        // Arrange
        Solicitud solicitud = solicitudBuilder.withTipoPrestamo("NO_EXISTE").build();

        when(tipoPrestamoRepository.findAllTipoPrestamo()).thenReturn(Flux.empty()); // Simula que no se encontró el tipo de préstamo

        // Act
        Mono<Solicitud> result = solicitudUseCase.save(solicitud);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof ExcepcionNoExisteTipoPrestamo &&
                        e.getMessage().equals("El tipo de préstamo no es válido"))
                .verify();
    }
}