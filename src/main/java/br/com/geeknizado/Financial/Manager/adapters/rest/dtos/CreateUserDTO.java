package br.com.geeknizado.Financial.Manager.adapters.rest.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record CreateUserDTO(
        @NotEmpty String name,
        @Email String email,
        @NotEmpty String password
) {
}
