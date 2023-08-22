package com.example.apigateway.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtProviderImpl implements JwtProvider{
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Override
    public Boolean hasClaim(String token, String name) {
        return this.extractAllClaims(token).get(name) != null;
    }


    @Override
    public String cleanBearerToken(String authToken) throws Exception{
        if (!StringUtils.hasText(authToken) || !authToken.startsWith("Bearer ") || authToken.split(" ").length != 2) {
            throw new Exception("Incorrect authorization structure");
        }

        return authToken.substring(7);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = this.extractAllClaims(token);
        return resolver.apply(claims);
    }

    @Override
    public Date extractExpired(String token) {
        return this.extractClaim(token, Claims::getExpiration);
    }

    @Override
    public Boolean isTokenValid(String token) {
        return null;
    }

    @Override
    public Boolean isTokenExpired(String token) {
        return this.extractExpired(token).before(new Date());
    }

    /**
     * Recupera todas las claims del token
     *
     * @param token Token JWT
     */
    @Override
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(this.getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Recupera la llave de cifrado del token
     */
    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }
}
