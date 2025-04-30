
package com.example.library.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    //  llave secreta
    private final Key key = Keys.hmacShaKeyFor("estaEsLaLLaveMaestraQueNosLLevanaLugaresQUeNaideSabe".getBytes());

    // Create un JWT token que tiene el usuario
    public String crearToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // saca el usuario
    public String extraerUsuario(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // checa si el token si es valido
    public boolean tokenValidado(String token, String username) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject().equals(username) && claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}