package br.com.geeknizado.financial_management.adapters.rest.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
       @Email String email,
       @NotBlank String password
) {
}
