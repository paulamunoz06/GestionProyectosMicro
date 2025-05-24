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

// ... (imports sin cambios)

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true) // jsr250Enabled es para @RolesAllowed, securedEnabled para @Secured
public class SecurityConfig {

    private static final String KEYCLOAK_CLIENT_ID_WITH_USER_ROLES = "sistema-desktop"; // Asegúrate que sea igual que en el gateway
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/public/register-new-company").permitAll()
                        // MODIFICADO: hasRole a hasAuthority
                        .requestMatchers(HttpMethod.POST, "/register").hasAuthority("company")
                        .requestMatchers(HttpMethod.GET, "/{companyId}").authenticated() // @PreAuthorize se encargará de la autorización fina
                        .requestMatchers(HttpMethod.GET, "/email/**").hasAuthority("coordinator")
                        .requestMatchers(HttpMethod.GET, "/all").hasAuthority("coordinator")
                        .requestMatchers(HttpMethod.GET, "/sector/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/count").hasAuthority("coordinator")
                        // .requestMatchers("/**").authenticated() // Podría ser redundante o demasiado general

                        // Reglas para ProjectController (ejemplos)
                        .requestMatchers(HttpMethod.POST, "/project/register").authenticated() // o hasAnyAuthority si aplica
                        .requestMatchers(HttpMethod.GET, "/project/{projectId}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/project/exists/{projectId}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/project/{projectId}/company").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/project/{projectId}").authenticated()
                        .requestMatchers("/project/**").authenticated() // Regla general para project

                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().denyAll() // Denegar todo lo no especificado explícitamente
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                        .disable()
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
            logger.warn("MICROSERVICIO: JWT claims son nulos.");
            return Collections.emptyList();
        }

        Map<String, Object> resourceAccess = (Map<String, Object>) claims.get("resource_access");
        if (resourceAccess != null && resourceAccess.containsKey(KEYCLOAK_CLIENT_ID_WITH_USER_ROLES)) {
            Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(KEYCLOAK_CLIENT_ID_WITH_USER_ROLES);
            if (clientAccess != null && clientAccess.containsKey("roles")) {
                @SuppressWarnings("unchecked")
                List<String> clientRoles = (List<String>) clientAccess.getOrDefault("roles", Collections.emptyList());
                if (clientRoles != null && !clientRoles.isEmpty()) {
                    logger.debug("MICROSERVICIO: Extrayendo Client Roles de '{}': {}", KEYCLOAK_CLIENT_ID_WITH_USER_ROLES, clientRoles);
                    return clientRoles.stream()
                            // MODIFICADO: Crear SimpleGrantedAuthority directamente con el nombre del rol
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                }
            }
        }

        Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            @SuppressWarnings("unchecked")
            List<String> realmRoles = (List<String>) realmAccess.getOrDefault("roles", Collections.emptyList());
            if (realmRoles != null && !realmRoles.isEmpty()) {
                logger.debug("MICROSERVICIO: Extrayendo Realm Roles: {}", realmRoles);
                return realmRoles.stream()
                        // MODIFICADO: Crear SimpleGrantedAuthority directamente con el nombre del rol
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }
        }
        logger.warn("MICROSERVICIO: No se encontraron roles ni en resource_access para el cliente '{}' ni en realm_access.", KEYCLOAK_CLIENT_ID_WITH_USER_ROLES);
        return Collections.emptyList();
    }
}