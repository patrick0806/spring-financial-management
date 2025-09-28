package br.com.geeknizado.financial_management.internal.interaction.auth;

import br.com.geeknizado.financial_management.bootstrap.exception.customException.GenerateTokenException;
import br.com.geeknizado.financial_management.bootstrap.exception.customException.UnauthorizedException;
import br.com.geeknizado.financial_management.internal.model.User;
import br.com.geeknizado.financial_management.internal.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class RefreshTokenUseCase {
    @Value("${financial-management.jwt.secret}")
    private String secret;

    @Value("${financial-management.jwt.expiration}")
    private long expiration;

    @Value("${financial-management.jwt.refresh-secret}")
    private String refreshSecret;

    @Value("${financial-management.jwt.refresh-expiration}")
    private long refreshExpiration;
    private final UserRepository repository;

    public RefreshTokenUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public Map<String, String> execute(String refreshToken){
        var userId = validateToken(refreshToken);
        var user = repository.findById(userId)
                .orElseThrow(()-> new UnauthorizedException("Cannot renew token"));

        var accessToken = generateAccessToken((User) user);
        var newRefreshToken = generateRefreshToken((User) user);

        return Map.of("accessToken", accessToken, "refreshToken", newRefreshToken);
    }

    private String validateToken(String token) {
        return JWT.require(Algorithm.HMAC256(refreshSecret))
                .withIssuer("financial-management")
                .build()
                .verify(token)
                .getSubject();
    }

    private String generateAccessToken(User user){
        try{
            return JWT.create().withIssuer("financial-management")
                    .withClaim("name", user.getName())
                    .withClaim("email", user.getEmail())
                    .withSubject(user.getId().toString())
                    .withExpiresAt(generateExpirationTime(expiration))
                    .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException exception) {
            throw new GenerateTokenException("Error creating token", exception);
        }
    }
    private String generateRefreshToken(User user){
        try{
            return JWT.create().withIssuer("financial-management")
                    .withSubject(user.getId().toString())
                    .withExpiresAt(generateExpirationTime(refreshExpiration))
                    .sign(Algorithm.HMAC256(refreshSecret));
        } catch (JWTCreationException exception) {
            throw new GenerateTokenException("Error creating token", exception);
        }
    }

    private Instant generateExpirationTime(long expiration) {
        return Instant.now().plusSeconds(expiration);
    }

}
