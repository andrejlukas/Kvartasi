package com.mojkvart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( registry -> {
                    registry.requestMatchers("/api/administrators/**").permitAll();
                    registry.requestMatchers("/api/atributs/**").permitAll();
                    registry.requestMatchers("/api/dogadajs/**").permitAll();
                    registry.requestMatchers("/api/kupacDogadajs/**").permitAll();
                    registry.requestMatchers("/api/kupacPonudaPopusts/**").permitAll();
                    registry.requestMatchers("/api/kupacProizvods/**").permitAll();
                    registry.requestMatchers("/api/kupacs/**").permitAll();
                    registry.requestMatchers("/api/kupacTrgovinaRecenzijas/**").permitAll();
                    registry.requestMatchers("/api/moderators/**").permitAll();
                    registry.requestMatchers("/api/ocjenaProizvodKupacs/**").permitAll();
                    registry.requestMatchers("/api/ponudaPopusts/**").permitAll();
                    registry.requestMatchers("/api/ponudas/**").permitAll();
                    registry.requestMatchers("/api/popusts/**").permitAll();
                    registry.requestMatchers("/api/proizvods/**").permitAll();
                    registry.requestMatchers("/api/racuns/**").permitAll();
                    registry.requestMatchers("/api/recenzijas/**").permitAll();
                    registry.requestMatchers("/api/trgovinas/**").permitAll();
                })
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}