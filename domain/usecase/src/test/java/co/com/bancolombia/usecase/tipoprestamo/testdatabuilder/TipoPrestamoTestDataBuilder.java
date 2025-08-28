package co.com.bancolombia.usecase.tipoprestamo.testdatabuilder;


import co.com.bancolombia.model.tipoprestamo.TipoPrestamo;

public class TipoPrestamoTestDataBuilder {

    private Long id_tipo_prestamo;
    private String nombre;
    private Long monto_minimo;
    private Long monto_maximo;
    private Double tasa_interes;
    private Boolean validacion_automatica;

    public TipoPrestamoTestDataBuilder() {
        this.id_tipo_prestamo = 1L;
        this.nombre = "Crédito de Libranza";
        this.monto_minimo = 1000000L;
        this.monto_maximo = 50000000L;
        this.tasa_interes = 0.015;
        this.validacion_automatica = true;
    }

    public TipoPrestamoTestDataBuilder withId(Long id_tipo_prestamo) {
        this.id_tipo_prestamo = id_tipo_prestamo;
        return this;
    }

    public TipoPrestamoTestDataBuilder withNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public TipoPrestamoTestDataBuilder withMontoMinimo(Long monto_minimo) {
        this.monto_minimo = monto_minimo;
        return this;
    }

    public TipoPrestamoTestDataBuilder withMontoMaximo(Long monto_maximo) {
        this.monto_maximo = monto_maximo;
        return this;
    }

    public TipoPrestamoTestDataBuilder withTasaInteres(Double tasa_interes) {
        this.tasa_interes = tasa_interes;
        return this;
    }

    public TipoPrestamoTestDataBuilder withValidacionAutomatica(Boolean validacion_automatica) {
        this.validacion_automatica = validacion_automatica;
        return this;
    }

    public TipoPrestamo build() {
        return new TipoPrestamo(
                this.id_tipo_prestamo,
                this.nombre,
                this.monto_minimo,
                this.monto_maximo,
                this.tasa_interes,
                this.validacion_automatica
        );
    }
}
