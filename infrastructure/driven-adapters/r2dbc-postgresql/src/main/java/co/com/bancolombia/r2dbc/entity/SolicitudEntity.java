package co.com.bancolombia.r2dbc.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "solicitud")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_solicitud;
    private String documentoIdentidad;
    private Long monto;
    private Integer plazo;
    private String email;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_tipo_prestamo")
    private TipoPrestamoEntity tipoPrestamo;
}
