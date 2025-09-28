package br.com.geeknizado.financial_management.adapters.rest.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CreateUserDTO(
        @NotEmpty String name,
        @NotEmpty @Email String email,
        @NotBlank String password
) {
}
