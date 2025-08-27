package co.com.bancolombia.model.tipoprestamo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TipoPrestamo {
    private Long id_tipo_prestamo;
    private String nombre;
    private Long monto_minimo;
    private Long monto_maximo;
    private Double tasa_interes;
    private Boolean validacion_automatica;
}
