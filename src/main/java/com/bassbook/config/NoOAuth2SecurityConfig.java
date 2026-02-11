package com.bassbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("!oauth2")
public class NoOAuth2SecurityConfig {

    @Bean
    public SecurityFilterChain noOAuth2FilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/h2-console/**").permitAll()
                .requestMatchers("/api/videos/songs", "/api/videos/songs/*/videos", "/api/artists", "/api/enums/**").permitAll()
                .requestMatchers("/api/user/info", "/api/auth/config", "/api/auth/status").permitAll()
                .requestMatchers("/auth").permitAll()
                .requestMatchers("/form.html", "/api/videos", "/api/artists/**").denyAll()
                .anyRequest().permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            .headers(headers -> headers
                .frameOptions().disable()
            );

        return http.build();
    }
}