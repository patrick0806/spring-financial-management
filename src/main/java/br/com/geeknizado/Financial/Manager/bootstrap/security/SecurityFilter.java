package br.com.geeknizado.Financial.Manager.bootstrap.security;

import br.com.geeknizado.Financial.Manager.adapters.datasource.UserDatasource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final ValidateToken validateToken;
    private final UserDatasource userDatasource;

    public SecurityFilter(ValidateToken validateToken, UserDatasource userDatasource) {
        this.validateToken = validateToken;
        this.userDatasource = userDatasource;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);

        if(!token.isEmpty()){
            var email = validateToken.execute(token);
            var user = userDatasource.findByEmail(email);

            if(user.isEmpty()) {
                throw new UsernameNotFoundException("User not found");
            }

            var authentication = new UsernamePasswordAuthenticationToken(user.get(), null, user.get().getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var token = request.getHeader("Authorization");
        if(!StringUtils.startsWith(token,"Bearer ")){
            return "";
        }
        return token.replace("Bearer ", "");
    }
}
