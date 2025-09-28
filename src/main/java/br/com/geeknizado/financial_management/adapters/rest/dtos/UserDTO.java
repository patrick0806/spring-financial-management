package br.com.geeknizado.financial_management.adapters.rest.dtos;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String name,
        String email,
        Boolean isActive
) {
}
