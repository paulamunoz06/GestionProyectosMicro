package co.edu.unicauca.microservicecoordinator.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    // private static final String KEYCLOAK_RESOURCE_CLIENT_ID = "sistema-desktop"; // No se usa directamente abajo
    private static final String CLIENT_ID_FOR_ROLES = "sistema-desktop"; // Usar una constante clara
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Si estos endpoints realmente deben ser públicos, permítelos.
                        // Para protegerlos, cambia permitAll() a .hasAuthority("coordinator") o .authenticated()
                        // y usa @PreAuthorize en los métodos del CoordinatorController si es necesario.
                        .requestMatchers(HttpMethod.GET, "/projects").permitAll()
                        .requestMatchers(HttpMethod.GET, "/projects/count-by-status/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/projects/count-total").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/projects/update-status").permitAll() // Asumo que es /projects/update-status y no con /**
                        .requestMatchers(HttpMethod.GET, "/project/{idProject}").permitAll() // Asumo que es /project/{idProject}

                        // Cualquier otra petición requiere autenticación.
                        // La autorización específica de roles se puede manejar con @PreAuthorize.
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthoritiesFromJwt);
        return converter;
    }

    private Collection<GrantedAuthority> extractAuthoritiesFromJwt(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        if (claims == null) {
            logger.warn("COORDINATOR-MS: JWT claims son nulos.");
            return Collections.emptyList();
        }

        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null || !resourceAccess.containsKey(CLIENT_ID_FOR_ROLES)) {
            logger.warn("COORDINATOR-MS: No se encontró 'resource_access' o el cliente '{}' en las claims.", CLIENT_ID_FOR_ROLES);
            // Considera un fallback a realm_access si es necesario.
            return Collections.emptyList();
        }

        Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(CLIENT_ID_FOR_ROLES);
        if (clientAccess == null || !clientAccess.containsKey("roles")) {
            logger.warn("COORDINATOR-MS: No se encontró la claim 'roles' dentro de '{}' en 'resource_access'.", CLIENT_ID_FOR_ROLES);
            return Collections.emptyList();
        }

        @SuppressWarnings("unchecked") // Roles deberían ser una lista de Strings
        List<String> roles = (List<String>) clientAccess.getOrDefault("roles", Collections.emptyList());

        if (roles.isEmpty()) {
            logger.warn("COORDINATOR-MS: La lista de roles para el cliente '{}' está vacía.", CLIENT_ID_FOR_ROLES);
            return Collections.emptyList();
        }

        logger.debug("COORDINATOR-MS: Extrayendo Client Roles de '{}': {}", CLIENT_ID_FOR_ROLES, roles);
        return roles.stream()
                // MODIFICADO: Crear SimpleGrantedAuthority directamente con el nombre del rol
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
