package co.com.bancolombia.r2dbc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tipo_prestamo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoPrestamoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_tipo_prestamo;

    private String nombre;
    private Long monto_minimo;
    private Long monto_maximo;
    private Double tasa_interes;
    private Boolean validacion_automatica;
}
