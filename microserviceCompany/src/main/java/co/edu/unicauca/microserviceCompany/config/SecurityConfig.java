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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private static final String KEYCLOAK_CLIENT = "sistema-desktop";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1) Seguridad general
                .csrf(csrf -> csrf
                                // Deshabilita CSRF solo para H2-console
                                .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                        // (si quieres CSRF global, elimina .disable() al final)
                )
                .headers(headers -> headers
                        // Para que la consola H2 funcione en iframe
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .authorizeHttpRequests(auth -> auth
                        // 2) Permitir H2 Console sin autenticación
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/register").permitAll() // Ya no requiere autenticación ni JWT


                        // 3) Tus endpoints protegidos
                        .requestMatchers(HttpMethod.GET,    "/{companyId}").authenticated()
                        .requestMatchers(HttpMethod.GET,    "/email/**").hasRole("coordinator")
                        .requestMatchers(HttpMethod.GET,    "/all").hasRole("coordinator")
                        .requestMatchers(HttpMethod.GET,    "/sector/**").permitAll()
                        .requestMatchers(HttpMethod.GET,    "/count").hasRole("coordinator")

                        .requestMatchers(HttpMethod.POST,   "/project/register").hasAnyRole("coordinator", "company")
                        .requestMatchers(HttpMethod.GET,    "/project/exists/{projectId}").hasAnyRole("coordinator", "company")
                        .requestMatchers(HttpMethod.GET,    "/project/{projectId}/company").hasAnyRole("coordinator", "company")
                        .requestMatchers(HttpMethod.GET,    "/project/{projectId}").hasAnyRole("coordinator", "company")
                        .requestMatchers(HttpMethod.PUT,    "/project/{projectId}").hasRole("company")


                        // Cualquier otro request requiere autenticación
                        .anyRequest().authenticated()
                )
                // 4) JWT Resource Server
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                // 5) Sin sesión
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthoritiesFromJwt);
        return converter;
    }

    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractAuthoritiesFromJwt(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null || !resourceAccess.containsKey(KEYCLOAK_CLIENT)) {
            return Collections.emptyList();
        }
        Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(KEYCLOAK_CLIENT);
        List<String> roles = (List<String>) clientAccess.get("roles");
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}
