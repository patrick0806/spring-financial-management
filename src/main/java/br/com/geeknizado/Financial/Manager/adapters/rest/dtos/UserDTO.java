package br.com.geeknizado.Financial.Manager.adapters.rest.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class UserDTO {
    private UUID id;
    private String name;
    private String email;
    private Boolean isActive;
}
