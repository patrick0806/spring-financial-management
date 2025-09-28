package br.com.geeknizado.financial_management.adapters.rest.mapper;

import br.com.geeknizado.financial_management.adapters.rest.dtos.UserDTO;
import br.com.geeknizado.financial_management.internal.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    public UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "isActive", target = "isActive")
    UserDTO map(User user);
}
