package com.example.ashutosh.watchlist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher; // Not strictly needed for current config, but kept.

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        System.out.println("WebSecurityConfig: Initializing BCryptPasswordEncoder bean.");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder()); // Ensure the same passwordEncoder bean is used
        System.out.println("WebSecurityConfig: Initializing DaoAuthenticationProvider with UserDetailsService and PasswordEncoder.");
        return authProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("WebSecurityConfig: Configuring SecurityFilterChain.");
        http
            .authorizeHttpRequests(auth -> auth
                // Publicly accessible paths
                .requestMatchers("/", "/home", "/index", "/login", "/register", "/register/**", "/css/**", "/js/**", "/images/**").permitAll()
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/watchlist", true) // Always redirect to /watchlist on successful login
                .failureUrl("/login?error") // Redirect to /login?error on login failure
                .permitAll() // Allow everyone to access the login page
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/") // Redirect to home page after successful logout
                .invalidateHttpSession(true) // Invalidate the HTTP session
                .clearAuthentication(true) // Clear authentication from SecurityContextHolder
                .permitAll() // Allow everyone to access the logout endpoint
            );
        
        // CSRF is enabled by default in Spring Security.
        // For Thymeleaf, th:action and _csrf hidden input handles this automatically for POST forms.
        // No explicit .csrf() configuration needed unless disabling or customizing it.

        return http.build();
    }

}
