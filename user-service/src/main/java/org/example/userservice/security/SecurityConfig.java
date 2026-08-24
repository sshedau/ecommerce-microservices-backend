package org.example.userservice.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Data
@AllArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter ;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                // Authentication
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers("/api/v1/users/hello").permitAll()

                                // USER
                                .requestMatchers("/api/v1/users/me").hasAnyRole("USER", "ADMIN")

                                // ADMIN operations
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/users/**").hasRole("ADMIN")

                                // Everything else
                                .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) ;
        return httpSecurity.build() ;
    }

//    @Bean
//    UserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder) {
//        UserDetails admin = User.withUsername("Admin")
//                .roles("ADMIN")
//                .password(passwordEncoder.encode("pass123"))
//                .build() ;
//        UserDetails user = User.withUsername("Sujal")
//                .roles("USER")
//                .password(passwordEncoder.encode("user123"))
//                .build() ;
//        return new InMemoryUserDetailsManager(user, admin) ;
//    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
        return authConfig.getAuthenticationManager() ;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder() ;
    }

}
