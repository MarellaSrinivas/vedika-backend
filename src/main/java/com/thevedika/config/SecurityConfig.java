
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

        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())
                                .cors(Customizer.withDefaults())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .formLogin(form -> form.disable())
                                .httpBasic(basic -> basic.disable())

                                .authorizeHttpRequests(auth -> auth

                                                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()

                                                // Public authentication endpoints
                                                .requestMatchers(
                                                                "/health",
                                                                "/error",
                                                                "/auth/otp/**",
                                                                "/auth/token/refresh")
                                                .permitAll()

                                                .requestMatchers("/auth/reviewer/login").permitAll()

                                                // Admin APIs
                                                .requestMatchers("/admin/**").hasRole("ADMIN")

                                                // Owner APIs
                                                .requestMatchers("/owner/**").hasRole("OWNER")

                                                // Public farmhouse browsing
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

                                                .requestMatchers(
                                                                HttpMethod.GET,
                                                                "/amenities")
                                                .permitAll()

                                                .requestMatchers(
                                                                HttpMethod.POST,
                                                                "/amenities")
                                                .hasRole("ADMIN")

                                                .requestMatchers(
                                                                HttpMethod.GET,
                                                                "/uploads/**")
                                                .permitAll()

                                                // Everything else requires login
                                                .anyRequest().authenticated())

                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
