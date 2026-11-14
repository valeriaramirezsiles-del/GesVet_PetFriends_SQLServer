package com.gesvet.petfriends.config;

import com.gesvet.petfriends.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
            .userDetailsService(userDetailsService)
            .authorizeHttpRequests(auth -> auth
                // Recursos estáticos
                .requestMatchers(
                    "/estilos.css", "/gesvet.css", "/gesvet-icon.svg", "/js/**",
                    "/*.png", "/*.jpg", "/*.avif", "/*.ico", "/favicon.ico"
                ).permitAll()

                // Páginas públicas del sitio web
                .requestMatchers(
                    "/", "/servicios", "/contacto", "/registro", "/citas", "/login", "/error"
                ).permitAll()

                // APIs públicas usadas por el sitio web
                .requestMatchers(
                    "/api/servicios", "/api/contacto", "/api/registrar",
                    "/api/citas", "/api/citas/**",
                    "/api/mascotas", "/api/mascotas/**"
                ).permitAll()

                // Módulos privados de gestión interna para personal autorizado
                .requestMatchers("/panel", "/api/panel/**").hasAnyRole("ADMIN", "VETERINARIO")

                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/panel", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?salida")
                .permitAll()
            );

        return http.build();
    }
}
