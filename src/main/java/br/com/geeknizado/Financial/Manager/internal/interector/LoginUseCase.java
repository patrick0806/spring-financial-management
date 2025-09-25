package br.com.geeknizado.Financial.Manager.internal.interector;

import br.com.geeknizado.Financial.Manager.bootstrap.exception.customException.GenerateTokenException;
import br.com.geeknizado.Financial.Manager.internal.model.User;
import br.com.geeknizado.Financial.Manager.internal.repository.UserRepository;
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

@Service
public class LoginUseCase implements UserDetailsService {
    @Value("${financial-management.jwt.secret}")
    private String secret;

    @Value("${financial-management.jwt.expiration}")
    private long expiration;


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public LoginUseCase(@Lazy/*see this circle after*/ AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public String execute(String email, String password){
        var usernamePassword = new UsernamePasswordAuthenticationToken(email,password);

        authenticationManager.authenticate(usernamePassword);

        final var user = this.loadUserByUsername(email);

        return generateToken((User) user);
    }

    private String generateToken(User user){
        try{
            return JWT.create().withIssuer("financial-management")
                    .withClaim("name", user.getName())
                    .withClaim("email", user.getEmail())
                    .withSubject(user.getId().toString())
                    .withExpiresAt(generateExpirationTime())
                    .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException exception) {
            throw new GenerateTokenException("Error creating token", exception);
        }
    }

    private Instant generateExpirationTime() {
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