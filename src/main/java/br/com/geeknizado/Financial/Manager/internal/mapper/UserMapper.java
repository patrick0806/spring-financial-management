package br.com.geeknizado.Financial.Manager.internal.mapper;

import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.UserDTO;
import br.com.geeknizado.Financial.Manager.internal.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    public UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO map(User user);
}
