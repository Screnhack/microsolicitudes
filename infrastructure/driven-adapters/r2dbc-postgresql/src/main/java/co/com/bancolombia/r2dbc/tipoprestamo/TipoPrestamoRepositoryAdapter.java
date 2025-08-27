package co.com.bancolombia.r2dbc.tipoprestamo;

import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import co.com.bancolombia.model.tipoprestamo.TipoPrestamo;
import co.com.bancolombia.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.bancolombia.r2dbc.entity.SolicitudEntity;
import co.com.bancolombia.r2dbc.entity.TipoPrestamoEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class TipoPrestamoRepositoryAdapter extends ReactiveAdapterOperations<
        TipoPrestamo/* change for domain model */,
        TipoPrestamoEntity/* change for adapter model */,
        Long,
        TipoPrestamoUseCasesRepository
        > implements TipoPrestamoRepository {
    public TipoPrestamoRepositoryAdapter(TipoPrestamoUseCasesRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, TipoPrestamo.class/* change for domain model */));
    }


    @Override
    public Flux<TipoPrestamo> findAllTipoPrestamo() {
        return findAll();
    }
}
