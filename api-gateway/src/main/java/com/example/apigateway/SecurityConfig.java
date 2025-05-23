package com.example.apigateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    // CADENA DE FILTROS #1: Para el endpoint público de registro de empresa
    @Bean
    @Order(1) // Se evalúa PRIMERO
    public SecurityWebFilterChain publicCompanyRegistrationFilterChain(ServerHttpSecurity http) {
        http
                .securityMatcher( // Aplicar esta cadena SOLO a esta ruta y método específicos
                        new PathPatternParserServerWebExchangeMatcher("/company/public/register-new-company", HttpMethod.POST)
                )
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll() // Si coincide el matcher, permitir todo
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable);
        // IMPORTANTE: NO hay .oauth2ResourceServer() aquí, para que no intente validar JWT
        return http.build();
    }

    // CADENA DE FILTROS #2: Para otras rutas públicas de /company/sector (ejemplo)
    @Bean
    @Order(2) // Se evalúa DESPUÉS de la anterior
    public SecurityWebFilterChain publicCompanySectorFilterChain(ServerHttpSecurity http) {
        http
                .securityMatcher( // Aplicar esta cadena SOLO a esta ruta y método
                        new PathPatternParserServerWebExchangeMatcher("/company/sector/**", HttpMethod.GET)
                )
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll()
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable);
        // NO .oauth2ResourceServer()
        return http.build();
    }


    // CADENA DE FILTROS #3: Para el resto de las rutas (protegidas con JWT)
    @Bean
    @Order(3) // Se evalúa DESPUÉS de las públicas
    public SecurityWebFilterChain protectedEndpointsFilterChain(ServerHttpSecurity http) {
        http
                // Esta cadena se aplica a todas las peticiones que NO coincidieron con los matchers anteriores
                .authorizeExchange(exchanges -> exchanges
                        // Reglas para rutas protegidas de /company/**
                        .pathMatchers(HttpMethod.POST, "/company/register").hasRole("COMPANY") // Roles en MAYÚSCULAS
                        .pathMatchers(HttpMethod.GET, "/company/{companyId}").authenticated()
                        .pathMatchers(HttpMethod.GET, "/company/email/**").hasRole("COORDINATOR")
                        .pathMatchers(HttpMethod.GET, "/company/all").hasRole("COORDINATOR")
                        .pathMatchers(HttpMethod.GET, "/company/count").hasRole("COORDINATOR")
                        // .pathMatchers("/company/**").authenticated() // Ya cubierto por anyExchange o más específico arriba

                        // Reglas para rutas de /project/** (ejemplos)
                        .pathMatchers(HttpMethod.POST, "/project/register").hasAnyRole("COMPANY", "COORDINATOR")
                        .pathMatchers("/project/**").authenticated()

                        // Reglas para rutas de /coordinator/** (ejemplos)
                        .pathMatchers("/coordinator/**").hasRole("COORDINATOR")

                        // Reglas para rutas de /student/** (ejemplos, permitAll aquí es solo un placeholder)
                        .pathMatchers("/student/**").permitAll() // O .authenticated() si deben ser protegidas

                        // Cualquier otra petición no definida explícitamente arriba, debe estar autenticada.
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2 // SÍ activar validación JWT para esta cadena
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor()))
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }

    // Convertidor para extraer roles del JWT (usado por protectedEndpointsFilterChain)
    Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverterWebFlux());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}

// La clase KeycloakRealmRoleConverterWebFlux ahora debería ser así:
class KeycloakRealmRoleConverterWebFlux implements Converter<Jwt, Collection<GrantedAuthority>> {
    private static final String KEYCLOAK_CLIENT_ID_WITH_USER_ROLES = "sistema-desktop";
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(KeycloakRealmRoleConverterWebFlux.class);


    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        if (claims == null) {
            return Collections.emptyList();
        }

        // Priorizar Client Roles de "sistema-desktop"
        Map<String, Object> resourceAccess = (Map<String, Object>) claims.get("resource_access");
        if (resourceAccess != null && resourceAccess.containsKey(KEYCLOAK_CLIENT_ID_WITH_USER_ROLES)) {
            Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(KEYCLOAK_CLIENT_ID_WITH_USER_ROLES);
            if (clientAccess != null && clientAccess.containsKey("roles")) {
                List<String> clientRoles = (List<String>) clientAccess.getOrDefault("roles", Collections.emptyList());
                if (!clientRoles.isEmpty()) {
                    logger.debug("GATEWAY: Extrayendo Client Roles de '{}': {}", KEYCLOAK_CLIENT_ID_WITH_USER_ROLES, clientRoles);
                    return clientRoles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                            .collect(Collectors.toList());
                }
            }
        }

        // Fallback a Realm Roles (si también los usas o como alternativa)
        Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            List<String> realmRoles = (List<String>) realmAccess.getOrDefault("roles", Collections.emptyList());
            if (!realmRoles.isEmpty()) {
                logger.debug("GATEWAY: Extrayendo Realm Roles: {}", realmRoles);
                return realmRoles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                        .collect(Collectors.toList());
            }
        }
        logger.warn("GATEWAY: No se encontraron roles ni en resource_access para '{}' ni en realm_access.", KEYCLOAK_CLIENT_ID_WITH_USER_ROLES);
        return Collections.emptyList();
    }
}