package com.edu.hcmuaf.springserver.config;

import com.edu.hcmuaf.springserver.auth.AuthenticationSuccessHandler;
import com.edu.hcmuaf.springserver.controller.AuthenticationController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Map;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                                .requestMatchers("/login-goolge").permitAll()

//                      Security
//                        .requestMatchers(HttpMethod.GET,"/api/category", "api/locations/all").permitAll()
////                      Movie
//                        .requestMatchers(HttpMethod.POST, "/api/movies/").hasAnyAuthority("admin")
//                                .requestMatchers(HttpMethod.PUT, "/api/movies/{id}").hasAnyAuthority("admin")
//                                .requestMatchers(HttpMethod.DELETE, "/api/movies/{id}").hasAnyAuthority("admin")
//                                .requestMatchers(HttpMethod.GET, "/api/movies/**").permitAll()
////                                .requestMatchers(HttpMethod.GET, "/api/movies/{id}").permitAll()
////                                .requestMatchers(HttpMethod.GET, "/api/movies/search").permitAll()
////                      Show times
//                        .requestMatchers(HttpMethod.GET, "/api/shows/**").permitAll()
//                                .requestMatchers(HttpMethod.DELETE,"/api/shows/{id}").hasAnyAuthority("admin")
//                                .requestMatchers(HttpMethod.PUT,"/api/shows/{id}").hasAnyAuthority("admin")
//                                .requestMatchers(HttpMethod.POST,"/api/shows/").hasAnyAuthority("admin")
//
////                      Theatre
//                                .requestMatchers(HttpMethod.GET, "/api/theatres/**").permitAll()
//                                .requestMatchers(HttpMethod.POST, "/api/theatres/").hasAnyAuthority("admin")
//                                .requestMatchers(HttpMethod.PUT, "/api/theatres/{id}").hasAnyAuthority("admin")
//                                .requestMatchers(HttpMethod.DELETE, "/api/theatres/{id}").hasAnyAuthority("admin")
//
////                      Ticket
//                        .requestMatchers(HttpMethod.GET, "/api/tickets/**").hasAnyAuthority("admin")
//                                .requestMatchers(HttpMethod.GET, "/api/tickets/get/{userId}").hasAnyAuthority("admin", "user")
//                                .requestMatchers(HttpMethod.POST, "/api/tickets/").hasAnyAuthority("admin")
//                                .requestMatchers(HttpMethod.PUT, "/api/tickets/{id}").hasAnyAuthority("admin")
//                                .requestMatchers(HttpMethod.DELETE, "/api/tickets/{id}").hasAnyAuthority("admin")
////                      User
//                        .requestMatchers(HttpMethod.GET, "/api/users/profile").permitAll()
//                                .requestMatchers(HttpMethod.GET, "/api/users/all").hasAnyAuthority("admin")
//                                .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyAuthority("admin")
//                                .requestMatchers(HttpMethod.POST, "/api/users/edit").hasAnyAuthority("admin")
//                                .requestMatchers(HttpMethod.POST, "/api/users/admin_create").hasAnyAuthority("admin")
//                                .requestMatchers(HttpMethod.DELETE, "/api/users/").hasAnyAuthority("admin")
////                                .requestMatchers(HttpMethod.GET, "/api/users").hasAnyAuthority("admin")
//
//
////                      Payment
//                        .requestMatchers(HttpMethod.POST, "/api/payment/pay").hasAnyAuthority("admin","user")
//                                .requestMatchers(HttpMethod.GET, "/api/payment-callback").hasAnyAuthority("admin", "user")
////                      Seat
//                        .requestMatchers(HttpMethod.GET, "/api/seats/get/**").hasAnyAuthority("admin","user")

//                        .requestMatchers("/api/**").permitAll()

                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(new AuthenticationSuccessHandler())
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}