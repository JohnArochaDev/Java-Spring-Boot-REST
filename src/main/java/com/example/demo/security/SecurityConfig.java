package com.example.demo.security;

import com.example.demo.filter.JwtAuthFilter;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserService userService;

    @Value("${custom.property}")
    private String customProperty;

    // Reduced whitelist for simplicity
    private static final String[] WHITE_LIST_URL = {
        "/api/v1/auth/login",
        "/api/v1/auth/register",
        "/users/login",
//        "/users",
//        "/users/*",
//        "/credentials/*"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF protection
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(WHITE_LIST_URL).permitAll() // Whitelist login and register routes
                .anyRequest().authenticated() // All other requests must be authenticated
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session management
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Registering our JwtAuthFilter
            .cors(withDefaults()); // Enable CORS with default settings

        return http.build();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService((UserDetailsService) userService); // Setting our custom user details service
        // Remove the password encoder
        return provider;
    }

    // CORS Configuration
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "chrome-extension://mikigodoelkfkbbdejkipipgndfolffj",  // Your Chrome extension
                "http://localhost:5173",  // Local frontend URL (React app)
                "http://localhost:8080",  // Local backend API URL
                "http://localhost:3000"   // Postman
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
            .allowCredentials(true);  // Enable credentials like cookies
    }

    public String getCustomProperty() {
        return customProperty;
    }
}