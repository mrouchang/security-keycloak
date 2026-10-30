package org.security.keycloak.springgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableAspectJAutoProxy
public class SecurityConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        // Replace with your actual Keycloak issuer URI
        return JwtDecoders.fromIssuerLocation("http://localhost:8888/realms/minwei_keycloak");
    }

    // <1>
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Build the JWT authentication mechanism
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

        http.csrf(withDefaults())
            .authorizeHttpRequests(
                auth -> auth.requestMatchers("/actuator/**").permitAll()
                                .requestMatchers("/api/public/**").permitAll() // Open endpoints
                                .requestMatchers("/api/admin/**").hasRole("admin") // Restricted to Keycloak admin role
                                .anyRequest().authenticated() // Everything else requires a valid token
            )
            .oauth2Login(withDefaults())  // Enables OAuth2 login
            .oauth2Client(withDefaults()) // Enables OAuth2 client
            .csrf(csrf -> csrf.disable())  // Disable CSRF for APIs
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(withDefaults())
            .oauth2ResourceServer( // Configure the OAuth2 Resource Server Filter
                    oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        return http.build();
    }

    // <2>
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(
                "http://auth.nsa2.com:9000",  // Keycloak
                "http://gateway.nsa2.com:8080" // Spring Cloud Gateway
        ));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
