package com.mojkvart.config;

import com.mojkvart.service.JwtService;
import com.mojkvart.service.Oauth2KorisnikService;
import com.mojkvart.util.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;
    private final Oauth2KorisnikService oauth2KorisnikService;

    @Value("${frontend.uri}")
    private String frontendUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> { registry
                        .requestMatchers("/api/tokens/expiration").permitAll()
                        .requestMatchers(("/api/tokens/claims")).permitAll()

                        .requestMatchers("/api/kupacs/signup").permitAll()
                        .requestMatchers("/api/kupacs/sendVerificationMail").permitAll()
                        .requestMatchers("/api/kupacs/verification").permitAll()
                        .requestMatchers("/api/kupacs/login").permitAll()

                        .requestMatchers("/api/trgovinas/signup").permitAll()
                        .requestMatchers("/api/moderators/signup").permitAll()
                        .anyRequest().authenticated(); })
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(endpoint -> endpoint.userService(oauth2KorisnikService))
                        .successHandler((request, response, authentication) -> {
                            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                            oauth2KorisnikService.authenticateKorisnik(oauth2User);
                            HashMap<String, Object> claims = oauth2KorisnikService.getClaims(oauth2User);
                            String role = (String) claims.get("role");
                            String token = jwtService.generateToken(claims, oauth2User.getAttribute("email"));
                            role = role.equals("KUPAC") ? "" : role.toLowerCase();
                            if(oauth2KorisnikService.getStatus().equals("V"))
                                response.sendRedirect(String.format("%s%s/home?token=%s", frontendUri, role, token));
                            else if(oauth2KorisnikService.getStatus().equals("N"))
                                response.sendRedirect(frontendUri + "/notVerifiedYet");
                            else
                                response.sendRedirect(frontendUri + "/suspended");
                        }));
        return http.build();
    }
}