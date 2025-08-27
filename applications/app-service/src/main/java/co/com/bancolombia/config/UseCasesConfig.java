package co.com.bancolombia.config;

import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import co.com.bancolombia.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.bancolombia.usecase.solicitud.SolicitudUseCase;
import co.com.bancolombia.usecase.tipoprestamo.TipoPrestamoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.com.bancolombia.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    @Bean
    public SolicitudUseCase solicitudUseCase(SolicitudRepository solicitudRepository,
                                             TipoPrestamoRepository tipoPrestamoRepository) {
        return new SolicitudUseCase(solicitudRepository, tipoPrestamoRepository);
    }

    public TipoPrestamoUseCase tipoPrestamoUseCase() {
        return new TipoPrestamoUseCase();
    }

}
