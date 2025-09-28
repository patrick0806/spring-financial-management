package br.com.geeknizado.financial_management.bootstrap.security;

import br.com.geeknizado.financial_management.bootstrap.exception.customException.UnauthorizedException;
import br.com.geeknizado.financial_management.internal.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class SecurityUtils {
    public static Optional<User> getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User)) {
            return Optional.empty();
        }
        return Optional.of((User) auth.getPrincipal());
    }

    public static UUID getCurrentUserId() {
        return getCurrentUser()
                .map(User::getId)
                .orElseThrow(() -> new UnauthorizedException("No authenticated user found"));
    }
}
