package co.edu.unicauca.microserviceCompany.config;

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

    private static final String KEYCLOAK_RESOURCE_CLIENT_ID = "sistema-desktop";
    private static final String KEYCLOAK_CLIENT_ID_WITH_USER_ROLES = "sistema-desktop";
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // El endpoint público para registrar empresa y usuario
                        .requestMatchers(HttpMethod.POST, "/public/register-new-company").permitAll()
                        // El endpoint para que una empresa autenticada actualice su perfil
                        .requestMatchers(HttpMethod.POST, "/register").hasRole("COMPANY") // Rol en MAYÚSCULAS
                        .requestMatchers(HttpMethod.GET, "/{companyId}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/email/**").hasRole("COORDINATOR")
                        .requestMatchers(HttpMethod.GET, "/all").hasRole("COORDINATOR")
                        .requestMatchers(HttpMethod.GET, "/sector/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/count").hasRole("COORDINATOR")
                        .requestMatchers("/**").authenticated()
                        // Para ProjectController (ejemplos, ajusta según tus necesidades)
                        .requestMatchers(HttpMethod.POST, "/project/register").authenticated()
                        .requestMatchers(HttpMethod.GET, "/project/{projectId}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/project/exists/{projectId}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/project/{projectId}/company").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/project/{projectId}").authenticated()
                        .requestMatchers("/project/**").authenticated()
                        .requestMatchers("/h2-console/**").permitAll() // Para H2 console
                        .anyRequest().denyAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // Para H2 Console si usas frames
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**") // Ignorar CSRF para H2 console
                        .disable() // Considera si necesitas deshabilitarlo globalmente o solo para ciertas rutas
                );
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthoritiesFromJwt);
        return converter;
    }

    private Collection<GrantedAuthority> extractAuthoritiesFromJwt(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        if (claims == null) {
            return Collections.emptyList();
        }

        // Priorizar Client Roles del cliente especificado (ej. "sistema-desktop")
        Map<String, Object> resourceAccess = (Map<String, Object>) claims.get("resource_access");
        if (resourceAccess != null && resourceAccess.containsKey(KEYCLOAK_CLIENT_ID_WITH_USER_ROLES)) {
            Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(KEYCLOAK_CLIENT_ID_WITH_USER_ROLES);
            if (clientAccess != null && clientAccess.containsKey("roles")) {
                @SuppressWarnings("unchecked")
                List<String> clientRoles = (List<String>) clientAccess.get("roles");
                if (clientRoles != null && !clientRoles.isEmpty()) {
                    logger.debug("Extrayendo Client Roles de '{}': {}", KEYCLOAK_CLIENT_ID_WITH_USER_ROLES, clientRoles);
                    return clientRoles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                            .collect(Collectors.toList());
                }
            }
        }

        // Como fallback, intentar extraer de Realm Roles
        Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            @SuppressWarnings("unchecked")
            List<String> realmRoles = (List<String>) realmAccess.get("roles");
            if (realmRoles != null && !realmRoles.isEmpty()) {
                logger.debug("Extrayendo Realm Roles: {}", realmRoles);
                return realmRoles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                        .collect(Collectors.toList());
            }
        }
        logger.warn("No se encontraron roles ni en resource_access para el cliente '{}' ni en realm_access.", KEYCLOAK_CLIENT_ID_WITH_USER_ROLES);
        return Collections.emptyList();
    }
}