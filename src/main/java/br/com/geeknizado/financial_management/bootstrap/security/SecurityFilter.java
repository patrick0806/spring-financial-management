package br.com.geeknizado.financial_management.bootstrap.security;

import br.com.geeknizado.financial_management.adapters.datasource.UserDatasource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final ValidateToken validateToken;
    private final UserDatasource userDatasource;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public SecurityFilter(ValidateToken validateToken,
                          UserDatasource userDatasource,
                          AuthenticationEntryPoint authenticationEntryPoint) {
        this.validateToken = validateToken;
        this.userDatasource = userDatasource;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       try{
           var token = this.recoverToken(request);

           if(!token.isEmpty()){
               var userId = validateToken.execute(token);
               var user = userDatasource.findById(userId)
                       .orElseThrow(() -> new UsernameNotFoundException("User not found"));

               var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
               SecurityContextHolder.getContext().setAuthentication(authentication);
           }

           filterChain.doFilter(request, response);
       }catch (Exception e) {
           SecurityContextHolder.clearContext();
           authenticationEntryPoint.commence(request, response,
                   new org.springframework.security.core.AuthenticationException(e.getMessage()) {});
       }
    }

    private String recoverToken(HttpServletRequest request){
        var token = request.getHeader("Authorization");
        if(!StringUtils.startsWith(token,"Bearer ")){
            return "";
        }
        return token.replace("Bearer ", "");
    }
}
