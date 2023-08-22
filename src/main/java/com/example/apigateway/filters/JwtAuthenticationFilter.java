package com.example.apigateway.filters;

import com.example.apigateway.jwt.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final JwtProvider jwtProvider;

    @Autowired
    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        super(Config.class);
        this.jwtProvider = jwtProvider;
    }


    @Override
    public GatewayFilter apply(JwtAuthenticationFilter.Config config) {
        // Custom Pre Filter. Suppose we can extract JWT and perform Authentication
        return (exchange, chain) -> {
            log.debug("Request in JWT Filter: {}", exchange.getRequest());

            //Si la request no tiene token, arroja una excepcion
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("Missing authorization information");
            }

            //Recupera el AUTHORIZATION header
            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            //Validar el token
            try {
                //Verifica que sea Bearer y lo saca
                String token = jwtProvider.cleanBearerToken(authHeader);
                //Verifica que este echo con la misma Secret Key
                jwtProvider.extractAllClaims(token);
                //Valida que no este expirado
                if(jwtProvider.isTokenExpired(token)){
                    throw new Exception("The Token is already expired");
                }
                //Verifica que tenga los datos correctos
                if(!jwtProvider.hasClaim(token, "id") || !jwtProvider.hasClaim(token, "authority")){
                    throw new Exception("The token structure is invalid");
                }

            } catch (Exception ex) {
                log.error("INVALID_TOKEN");
                log.error("Exception: {}",ex.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

                return exchange.getResponse().setComplete();
            }

            log.info("End pre filter jwt");
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Put the configuration properties
    }

}
