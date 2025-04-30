package com.example.library.config;

import com.example.library.service.ServicioUsuario;
import com.example.library.util.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Esta clase configura la seguridad de Spring





@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ServicioUsuario customUserDetailsService;

    @Bean
    public SecurityFilterChain filtroCadenaSeguirdad(HttpSecurity http) throws Exception {
        http
        // como no usamos sesiones o cookies, pues desabilitamos csrf ya que n oes necesario aqui 
            .csrf(csrf -> csrf.disable())
            // aqui definimos a cual no necesitamos autentificacio y cuales si
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/login", 
                    "/swagger-ui/**", 
                    "/v3/api-docs/**"
                ).permitAll()
                .anyRequest().authenticated()
                // Aqui abajo es para decir que no usamos sesiones 
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(provedorAutenticacion())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Aqui es para decirle como agarrar usuarios de la base de datos 
    @Bean
    public AuthenticationProvider provedorAutenticacion() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(contraCifrar());
        return provider;
    }

    // Aqui regresamos el authenticationmanager que se usara en el AuthController
    @Bean
    public AuthenticationManager manejadorAutenticacion(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Aqui es para definir que algoritmo de encriptacion usaremos
    @Bean
    public PasswordEncoder contraCifrar() {
        return new BCryptPasswordEncoder();
    }
}
