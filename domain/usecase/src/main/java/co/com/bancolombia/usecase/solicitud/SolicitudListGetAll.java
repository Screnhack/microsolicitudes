package co.com.bancolombia.usecase.solicitud;

import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class SolicitudListGetAll {
    private final SolicitudRepository solicitudRepository;

    public Mono<List<Solicitud>> getPaginatedSolicitudes() {
        Mono<List<Solicitud>> paginatedList = solicitudRepository.findAll().collectList();
        return paginatedList;
    }

    public Mono<Long> getDataCount() {
        Mono<Long> totalCount = solicitudRepository.count();
        return totalCount;
    }
}
