package es.albavm.tfg.trifly.dto.User;

import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import es.albavm.tfg.trifly.Model.User;


@Mapper(componentModel = "spring")
public interface UserMapper {
    
    UserDto toDto(User user);
    List<UserDto> toDTOs(Collection<User> users);

    @Mapping(target = "id", ignore = true) 
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true) 

    User toDomain(NewUserDto newUserDto);
    void updateUserFromDto(UserUpdateDto dto, @MappingTarget User user);
}
