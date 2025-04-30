package com.example.library.controller;
import java.util.Map;

import com.example.library.dto.LoginRequest;
import com.example.library.repository.UserRepository;
import com.example.library.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;


// El rest controller es para regresar jsons 
@RestController
@RequestMapping("/auth")
public class AuthController {


   
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;


    // Esto es lo de swagger para poner la descripcion
    @Operation(summary = "Con las credenciales correctas, obtenemos un key")

    // Lo de swagger para decir que desplegar en caso de exito o error
     @ApiResponses({

         @ApiResponse(responseCode = "200", description = "Credenciales buenas"),
        @ApiResponse(responseCode = "404", description = "Credenciales malas")

    })
   
    // Este de security es para indicar que no necesitamos el JWT para usarlo 
    @SecurityRequirements 
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Esto es para autorizar el usuario, es es que esta correcto
            Authentication autenticacion = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // se crea el token 
            String token = jwtUtil.crearToken(request.getUsername());
            return ResponseEntity.ok().body(Map.of("access_token", token));

            
            // Si no esta correcto entonces es lo de aqui abajo
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("No autorizado");
        }
    }
}
