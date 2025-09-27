package br.com.geeknizado.financial_management.bootstrap.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ValidateToken {
    @Value("${financial-management.jwt.secret}")
    private String secret;

    public String execute(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("financial-management")
                .build()
                .verify(token)
                .getSubject();
    }
}