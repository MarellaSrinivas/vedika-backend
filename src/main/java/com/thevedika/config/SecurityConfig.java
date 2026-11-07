
package com.thevedika.config;

import com.thevedika.security.JwtAuthenticationFilter;

import jakarta.servlet.DispatcherType;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public SecurityConfig(
                        JwtAuthenticationFilter jwtAuthenticationFilter) {
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())

                                .cors(Customizer.withDefaults())

                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))

                                .formLogin(form -> form.disable())
                                .httpBasic(basic -> basic.disable())

                                .authorizeHttpRequests(auth -> auth

                                                // Allow Spring error dispatches.
                                                .dispatcherTypeMatchers(DispatcherType.ERROR)
                                                .permitAll()

                                                // Allow public authentication endpoints.
                                                .requestMatchers(
                                                                "/health",
                                                                "/error",
                                                                "/auth/otp/**")
                                                .permitAll()

                                                // Public farmhouse GET endpoints.
                                                .requestMatchers(
                                                                HttpMethod.GET,
                                                                "/farmhouses",
                                                                "/farmhouses/",
                                                                "/farmhouses/search",
                                                                "/farmhouses/*",
                                                                "/farmhouses/*/availability",
                                                                "/farmhouses/*/images",
                                                                "/farmhouses/*/amenities",
                                                                "/farmhouses/*/addons",
                                                                "/amenities")
                                                .permitAll()

                                                // Protect all remaining endpoints.
                                                .anyRequest().authenticated())

                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
