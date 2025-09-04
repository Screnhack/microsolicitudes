
package co.com.bancolombia.solicitudes.drivenadapters;

import co.com.bancolombia.model.autenticacion.gateways.AutenticacionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserAuthenticationWebClient implements AutenticacionRepository {

    private final WebClient webClient;

    public UserAuthenticationWebClient(@Value("${services.authentication-service.base-url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public Mono<Boolean> validacionCorreo(String correo) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/autenticacion/api/v1/usuario/correo")
                        .queryParam("correo", correo)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(e -> {
                    return Mono.error(new RuntimeException("Error al comunicarse con el servicio de autenticación", e));
                });
    }
}
