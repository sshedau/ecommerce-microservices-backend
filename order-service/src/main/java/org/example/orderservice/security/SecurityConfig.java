package org.example.orderservice.security;

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
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/v1/orders/hello").permitAll()
                        .requestMatchers("/api/v1/orders/instance").permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/orders"
                        ).hasAnyRole("USER", "ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/orders/my",
                                "/api/v1/orders/my/**"
                        ).hasAnyRole("USER", "ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/orders"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                "/api/v1/orders/user/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/orders/*"
                        ).hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
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
