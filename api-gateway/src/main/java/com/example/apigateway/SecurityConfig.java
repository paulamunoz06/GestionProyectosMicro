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
                        //TODO: verificar roles permitidos para cada endpoint

                        // COMPANY MicroService
                        .pathMatchers("/company/register").permitAll()
                        .pathMatchers("/company/{companyId}").authenticated()
                        .pathMatchers("/company/email/**").hasRole("coordinator")
                        .pathMatchers("/company/all").hasRole("coordinator")
                        .pathMatchers("/company/sector/**").permitAll()
                        .pathMatchers("/company/count").hasRole("coordinator")
                        .pathMatchers("/company/**").authenticated()

                        .pathMatchers("/company/project/register").hasAnyRole("coordinator", "company")
                        .pathMatchers("/company/project/exists/**").hasAnyRole("coordinator", "company")
                        .pathMatchers("/company/project/*/company").hasAnyRole("coordinator", "company")
                        .pathMatchers("/company/project/*").hasAnyRole("coordinator", "company")
                        // COORDINATOR MicroService
                        .pathMatchers("/coordinator/projects").hasRole("coordinator")
                        .pathMatchers("/coordinator/projects/count-by-status/**").hasRole("coordinator")
                        .pathMatchers("/coordinator/projects/count-total").hasRole("coordinator")
                        .pathMatchers("/coordinator/projects/update-status").hasRole("coordinator")
                        .pathMatchers("/coordinator/project/**").hasRole("coordinator")

                        // STUDENT MicroService
                        .pathMatchers("/student/{idStudent}").permitAll()
                        .pathMatchers("/student/{studentId}/project/{projectId}").permitAll()
                        .pathMatchers("/student/{studentId}/projects").permitAll()




                        .anyExchange().authenticated() // Todas las rutas deben estar autenticadas
                )
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
        return http.build();
    }
}

