package fpc.app.config;

import java.util.List;

import fpc.app.security.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  /**
   * Returns the authentication manager, what is the component that manages user's authentication
   * It's used to autenticate users by UsernamePasswordAuthenticationToken
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }

  /**
   * Defines the password encoder to be used in the application BCryptPasswordEncoder is a password
   * encoder that uses the BCrypt strong hashing function
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Disables CSRF and session management because the application is stateless and every request
   * includes the authentication token Defines which endpoints are public and which are protected
   * Builds the security configuration and returns it
   */
  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, JwtRequestFilter jwtRequestFilter) throws Exception {
    http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/bus/v3/api-docs/**")
                    .permitAll()
                    .requestMatchers("/club-admin/**")
                    .hasRole("CLUB_ADMIN")
                    .requestMatchers("/su/**")
                    .hasRole("SUPERUSER")
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  private CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("Content-Type", "Authorization"));
    config.setExposedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
