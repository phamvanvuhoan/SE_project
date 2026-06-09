package com.restaurant.pos.config;

import com.restaurant.pos.security.JwtAuthenticationEntryPoint;
import com.restaurant.pos.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers("/api/v1/employees/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/membership-levels/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/membership-levels/**").hasRole("MANAGER")
                        .requestMatchers("/api/v1/promotions/**").hasRole("MANAGER")
                        .requestMatchers("/api/v1/reports/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/categories/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/categories/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/categories/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/menu-items/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/menu-items/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/menu-items/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/tables/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/tables/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/tables/**").hasRole("MANAGER")
                        .requestMatchers("/api/v1/orders/**").hasAnyRole("STAFF", "MANAGER")
                        .requestMatchers("/api/v1/payments/**").hasAnyRole("STAFF", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/customers/**").hasAnyRole("STAFF", "MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/customers/**").hasAnyRole("STAFF", "MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/customers/**").hasAnyRole("STAFF", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/membership-levels/**").hasAnyRole("STAFF", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").hasAnyRole("STAFF", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/menu-items/**").hasAnyRole("STAFF", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/tables/**").hasAnyRole("STAFF", "MANAGER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/tables/*/status").hasAnyRole("STAFF", "MANAGER")
                        .anyRequest().hasRole("MANAGER")
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
