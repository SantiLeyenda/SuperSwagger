
package com.example.library.util;

import com.example.library.service.ServicioUsuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    // Metodo que ayuda a extraer y validar token JWT
    private final JwtUtil jwtUtil;

    // Para cargar el usuario
    @Autowired
    private ServicioUsuario userDetailsService;

    // El constructor 
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Aqui declaramos que aqui no necesitar hacer el filter                                
        String path = request.getRequestURI();
        if (
            path.startsWith("/swagger-ui") ||
            path.startsWith("/v3/api-docs") ||
            path.startsWith("/auth/login") ||
            path.startsWith("/h2-console") ||
            path.equals("/")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Filtro activado");

        String cabezaAutenticacino = request.getHeader("Authorization");

        // aqui es para si no hay token o no es de Bearer
        if (cabezaAutenticacino == null || !cabezaAutenticacino.startsWith("Bearer ")) {
            System.out.println("Error al leer token, formato incorrecto ");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            // Aqui esa para el token y quita el Bearer
            String token = cabezaAutenticacino.substring(7);
            //sacamos el usuario
            String usuario = jwtUtil.extraerUsuario(token);

            if (usuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(usuario);

                // Si es valido el token, se hace un AuthenticationToken y lo registra 
                if (jwtUtil.tokenValidado(token, usuario)) {
                    UsernamePasswordAuthenticationToken tokenAut =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                    tokenAut.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(tokenAut);
                } else {
                    System.out.println("Token fallo prueba de validacion");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
            // Aqui es cuando intentan con uno token que no es 
        } catch (Exception e) {
            System.out.println("JWT invalido !!!!! NO TIENE ACCESSO A ESTO ENDPOINT" + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
