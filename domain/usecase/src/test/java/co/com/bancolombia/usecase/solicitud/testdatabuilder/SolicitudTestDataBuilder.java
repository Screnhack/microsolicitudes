package co.com.bancolombia.usecase.solicitud.testdatabuilder;

import co.com.bancolombia.model.solicitud.Solicitud;

public class SolicitudTestDataBuilder {

    private Long id_solicitud;
    private Long monto;
    private String documentoIdentidad;
    private Integer plazo;
    private String email;
    private String estado;
    private String tipoPrestamo;

    public SolicitudTestDataBuilder() {
        this.id_solicitud = 1L;
        this.monto = 5000000L;
        this.documentoIdentidad = "1012345678";
        this.plazo = 36;
        this.email = "prueba@example.com";
        this.estado = "PENDIENTE";
        this.tipoPrestamo = "PRESTAMO_PERSONAL";
    }

    public SolicitudTestDataBuilder withId(Long id_solicitud) {
        this.id_solicitud = id_solicitud;
        return this;
    }

    public SolicitudTestDataBuilder withMonto(Long monto) {
        this.monto = monto;
        return this;
    }

    public SolicitudTestDataBuilder withDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
        return this;
    }

    public SolicitudTestDataBuilder withPlazo(Integer plazo) {
        this.plazo = plazo;
        return this;
    }

    public SolicitudTestDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public SolicitudTestDataBuilder withEstado(String estado) {
        this.estado = estado;
        return this;
    }

    public SolicitudTestDataBuilder withTipoPrestamo(String tipoPrestamo) {
        this.tipoPrestamo = tipoPrestamo;
        return this;
    }

    public Solicitud build() {
        return new Solicitud(
                this.id_solicitud,
                this.monto,
                this.documentoIdentidad,
                this.plazo,
                this.email,
                this.estado,
                this.tipoPrestamo
        );
    }
}
