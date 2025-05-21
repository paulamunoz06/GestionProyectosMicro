package com.example.apigateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges

                        // COMPANY Service
                        .pathMatchers("/company/register").hasRole("company")
                        .pathMatchers("/company/{companyId}").authenticated()
                        .pathMatchers("/company/email/**").hasRole("coordinator")
                        .pathMatchers("/company/all").hasRole("coordinator")
                        .pathMatchers("/company/sector/**").permitAll()
                        .pathMatchers("/company/count").hasRole("coordinator")
                        .pathMatchers("/company/**").authenticated()

                        // STUDENT Service (Ejemplos)
                        // .pathMatchers(HttpMethod.POST, "/student/register").hasAnyRole("STUDENT", "ADMIN_APP")
                        // .pathMatchers("/student/profile/{studentId}").access( (authentication, context) ->
                        //    hasAccess(authentication, context.getVariables().get("studentId").toString(), "STUDENT")
                        // )
                        // .pathMatchers("/student/**").hasAnyRole("COORDINATOR", "ADMIN_APP")

                        // COORDINATOR Service (Ejemplos)
                        // .pathMatchers("/coordinator/projects/approve").hasRole("COORDINATOR")
                        // .pathMatchers("/coordinator/**").hasRole("COORDINATOR")

                        .anyExchange().authenticated() // Todas las demás rutas no especificadas requieren autenticación
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor()))
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable); // Deshabilitar CSRF para APIs stateless
        return http.build();
    }

    // Convertidor para extraer roles del JWT
    Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverterWebFlux());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}

// Convertidor de roles de Keycloak para Realm Roles
class KeycloakRealmRoleConverterWebFlux implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        final Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().getOrDefault("realm_access", Map.of());
        final List<String> roles = (List<String>) realmAccess.getOrDefault("roles", List.of());

        return roles.stream()
                .map(roleName -> "ROLE_" + roleName.toUpperCase()) // Spring Security espera el prefijo ROLE_
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}