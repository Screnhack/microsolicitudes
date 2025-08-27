package co.com.bancolombia.model.tipoprestamo.gateways;

import co.com.bancolombia.model.tipoprestamo.TipoPrestamo;
import reactor.core.publisher.Flux;

public interface TipoPrestamoRepository {
    Flux<TipoPrestamo> findAllTipoPrestamo();
}
