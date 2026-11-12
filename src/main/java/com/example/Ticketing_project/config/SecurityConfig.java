package com.example.Ticketing_project.config;

import com.example.Ticketing_project.security.CustomUserDetailsService;
import com.example.Ticketing_project.security.JwtAuthenticationFilter;
// ... standard org.springframework imports remain the same
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Marks this as a Spring configuration class
@EnableWebSecurity // Enables Spring Security's web security support
@EnableMethodSecurity // Enables method-level security (e.g. @PreAuthorize annotations on controllers)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    // 1. Password Encoder Bean: Registers BCrypt so Spring can hash and verify passwords securely
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Authentication Provider Bean: Connects our UserDetailsService and PasswordEncoder together
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // Pass the userDetailsService directly into the constructor (New Spring Security standard)
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);

        // Set the password encoder using the setter
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    // 3. Authentication Manager Bean: Used later in our Auth Controller to verify user login credentials
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 4. Security Filter Chain: Defines HTTP security rules, URL permissions, and stateless sessions
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (Cross-Site Request Forgery) because JWT tokens are immune to CSRF (no cookies used)
                .csrf(csrf -> csrf.disable())

                // Configure session management as STATELESS (forces server never to create HttpSession)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configure endpoint authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()    // Allow public access to login/register
                        .requestMatchers("/api/events/public/**").permitAll() // Allow public access to event catalog
                        .anyRequest().authenticated()                    // All other endpoints require a valid JWT token
                );

        // Register our custom authentication provider
        http.authenticationProvider(authenticationProvider());

        // Insert our JwtAuthenticationFilter BEFORE Spring's default username/password filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}