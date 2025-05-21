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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/company/register").hasRole("company")
                        .requestMatchers(HttpMethod.GET, "/company/{companyId}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/company/email/**").hasRole("coordinator")
                        .requestMatchers(HttpMethod.GET, "/company/all").hasRole("coordinator")
                        .requestMatchers(HttpMethod.GET, "/company/sector/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/company/count").hasRole("coordinator")
                        .requestMatchers("/company/**").authenticated()

                        // Denegar todo lo demás que no haya sido explícitamente permitido o autenticado
                        .anyRequest().denyAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // Este bean es responsable de tomar el JWT y convertirlo en un objeto Authentication
    // que Spring Security pueda usar, incluyendo las GrantedAuthorities (roles).
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        // Le decimos al convertidor CÓMO extraer las autoridades (roles) del JWT
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthoritiesFromJwt);
        return converter;
    }

    // Este método privado es llamado por jwtAuthenticationConverter().
    // Aquí defines la lógica para extraer los roles del token JWT.
    // ADAPTADO para ser flexible: primero busca Realm Roles, luego Client Roles.
    private Collection<GrantedAuthority> extractAuthoritiesFromJwt(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        if (claims == null) {
            return Collections.emptyList();
        }

        // Opción 1: Intentar extraer de Realm Roles (ej. si tus roles son globales en el realm)
        // Los roles de realm en Keycloak están bajo "realm_access": { "roles": ["role1", "role2"] }
        Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            @SuppressWarnings("unchecked")
            List<String> realmRoles = (List<String>) realmAccess.get("roles");
            if (realmRoles != null && !realmRoles.isEmpty()) {
                return realmRoles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())) // ej., "ROLE_COMPANY"
                        .collect(Collectors.toList());
            }
        }

        // Opción 2: Intentar extraer de Client Roles (como en el ejemplo de tu profesor)
        // Los roles de cliente en Keycloak están bajo "resource_access": { "client-id": { "roles": ["roleA", "roleB"] } }
        Map<String, Object> resourceAccess = (Map<String, Object>) claims.get("resource_access");
        if (resourceAccess != null && resourceAccess.containsKey(KEYCLOAK_RESOURCE_CLIENT_ID)) {
            Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(KEYCLOAK_RESOURCE_CLIENT_ID);
            if (clientAccess != null && clientAccess.containsKey("roles")) {
                @SuppressWarnings("unchecked")
                List<String> clientRoles = (List<String>) clientAccess.get("roles");
                if (clientRoles != null && !clientRoles.isEmpty()) {
                    return clientRoles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                            .collect(Collectors.toList());
                }
            }
        }

        // Si no se encontraron roles ni en realm_access ni en resource_access para el cliente especificado
        return Collections.emptyList();
    }
}