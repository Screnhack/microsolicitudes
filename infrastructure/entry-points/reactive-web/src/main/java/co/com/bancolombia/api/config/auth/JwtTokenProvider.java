package co.com.bancolombia.api.config.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    public Mono<Authentication> getAuthentication(String token) {
        return Mono.fromCallable(() -> {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            String role = claims.get("role", String.class);


            String formattedRole = Optional.ofNullable(role)
                    .filter(r -> !r.isBlank())
                    .map(r -> "ROLE_" + r.toUpperCase())
                    .orElseThrow(() -> new IllegalArgumentException("Rol se encuentra vacio JWT token"));

            var authorities = List.of(new SimpleGrantedAuthority(formattedRole));

            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        });
    }

    public Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            return true;
        }).onErrorReturn(false);
    }
}


