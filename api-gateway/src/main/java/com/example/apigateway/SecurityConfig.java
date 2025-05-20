package com.example.apigateway;


import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

// SecurityConfig.java en Gateway:
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/a/hello_all", "/b/hello_all").permitAll()
                        .pathMatchers("/a/hello_student", "/b/hello_student").hasRole("student")
                        .pathMatchers("/a/hello_coordinator", "/b/hello_coordinator").hasRole("coordinator")
                        .pathMatchers("/a/hello_company", "/b/hello_company").hasRole("company")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
        return http.build();
    }
}