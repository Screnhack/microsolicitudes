package co.com.bancolombia.model.solicitud;

import co.com.bancolombia.model.tipoprestamo.TipoPrestamo;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud {
    private Long id_solicitud;
    private Integer monto;
    private String documentoIdentidad;
    private Integer plazo;
    private String email;
    private String estado;
    private TipoPrestamo tipoPrestamo;
}
