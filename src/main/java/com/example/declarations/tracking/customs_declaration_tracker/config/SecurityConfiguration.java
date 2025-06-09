package com.example.declarations.tracking.customs_declaration_tracker.config;

import com.example.declarations.tracking.customs_declaration_tracker.util.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/declarations").hasAuthority(Role.ROLE_DECLARANT)
                        .requestMatchers(HttpMethod.PATCH, "/declarations/**").hasAnyAuthority(Role.ROLE_INSPECTOR, Role.ROLE_ADMIN)
                        .requestMatchers(HttpMethod.GET, "/declarations/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().denyAll()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var declarant = User.withUsername("declarant")
                .password("{noop}password")
                .roles(Role.DECLARANT)
                .build();

        var inspector = User.withUsername("inspector")
                .password("{noop}password")
                .roles(Role.INSPECTOR)
                .build();

        var admin = User.withUsername("admin")
                .password("{noop}admin")
                .roles(Role.ADMIN)
                .build();

        return new InMemoryUserDetailsManager(declarant, inspector, admin);
    }
}