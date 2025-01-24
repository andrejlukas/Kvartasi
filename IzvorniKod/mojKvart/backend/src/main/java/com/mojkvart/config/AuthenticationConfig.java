package com.mojkvart.config;

import com.mojkvart.repos.AdministratorRepository;
import com.mojkvart.repos.KupacRepository;
import com.mojkvart.repos.ModeratorRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationConfig {
    @Autowired
    private KupacRepository kupacRepository;

    @Autowired
    private TrgovinaRepository trgovinaRepository;

    @Autowired
    private ModeratorRepository moderatorRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            if(kupacRepository.findByKupacEmail(username).isPresent())
                return kupacRepository.findByKupacEmail(username).get();
            else if(trgovinaRepository.findByTrgovinaEmail(username).isPresent())
                return trgovinaRepository.findByTrgovinaEmail(username).get();
            else if(moderatorRepository.findByModeratorEmail(username).isPresent())
                return moderatorRepository.findByModeratorEmail(username).get();
            return administratorRepository.findByAdministratorEmail(username).orElseThrow(NotFoundException::new);
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}