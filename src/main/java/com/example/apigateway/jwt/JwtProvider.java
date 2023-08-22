package com.example.apigateway.jwt;

import java.util.Date;
import io.jsonwebtoken.Claims;
import java.util.function.Function;

public interface JwtProvider {

    /**
     * Verifica si la claim que deseas se encuentra en el token
     *
     * @param token Token JWT
     * @param name  Nombre de la claim
     */
    Boolean hasClaim(String token, String name);

    <T> T extractClaim(String token, Function<Claims, T> resolver);

    String cleanBearerToken(String authToken) throws Exception;

    /**
     * Recupera la fecha de caducidad del token
     *
     * @param token Token JWT
     */
    Date extractExpired(String token);

    Claims extractAllClaims(String token);

    /**
     * Verifica que el token sea valido
     *
     * @param token       Token JWT
     * @return
     */
    Boolean isTokenValid(String token);

    /**
     * Verifica si el token ha caducado
     *
     * @param token Token JWT
     */
    Boolean isTokenExpired(String token);
}
