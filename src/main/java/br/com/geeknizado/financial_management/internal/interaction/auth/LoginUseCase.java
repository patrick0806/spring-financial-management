package br.com.geeknizado.financial_management.internal.interaction.auth;

import br.com.geeknizado.financial_management.bootstrap.exception.customException.GenerateTokenException;
import br.com.geeknizado.financial_management.internal.model.User;
import br.com.geeknizado.financial_management.internal.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class LoginUseCase implements UserDetailsService {
    @Value("${financial-management.jwt.secret}")
    private String secret;

    @Value("${financial-management.jwt.expiration}")
    private long expiration;

    @Value("${financial-management.jwt.refresh-secret}")
    private String refreshSecret;

    @Value("${financial-management.jwt.refresh-expiration}")
    private long refreshExpiration;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public LoginUseCase(@Lazy/*see this circle after*/ AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public Map<String,String> execute(String email, String password){
        var usernamePassword = new UsernamePasswordAuthenticationToken(email,password);

        authenticationManager.authenticate(usernamePassword);

        final var user = this.loadUserByUsername(email);

        var accessToken = generateAccessToken((User) user);
        var refreshToken = generateRefreshToken((User) user);

        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final var user = userRepository.findByEmail(email);

        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        return user.get();
    }
}
