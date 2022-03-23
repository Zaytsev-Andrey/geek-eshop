package ru.geekbrains.mapper;

import org.modelmapper.ModelMapper;
import ru.geekbrains.dto.RoleDto;
import ru.geekbrains.dto.UserDto;
import ru.geekbrains.persist.Role;
import ru.geekbrains.persist.User;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserMapper<E extends User, D extends UserDto> extends AbstractMapper<E, D> {

    private Mapper<Role, RoleDto> roleMapper;

    public UserMapper(ModelMapper modelMapper,
                      Class<E> entityClass,
                      Class<D> dtoClass,
                      Mapper<Role, RoleDto> roleMapper) {
        super(modelMapper, entityClass, dtoClass);
        this.roleMapper = roleMapper;
    }

    @Override
    public void mapSpecificFields(User source, UserDto destination) {
        destination.setId(source.getId().toString());
        destination.setRolesDto(source.getRoles().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toSet()));
    }

    @Override
    public void mapSpecificFields(UserDto source, User destination) {
        String id = source.getId();
        if (!Objects.isNull(id) && !id.isBlank()) {
            destination.setId(UUID.fromString(id));
        }
        destination.setRoles(source.getRolesDto().stream()
                .map(roleMapper::toEntity)
                .map(role -> {
                    role.setUsers(Set.of(destination));
                    return role;
                })
                .collect(Collectors.toSet()));
    }

    @PostConstruct
    public void initMapper() {
        this.getModelMapper().createTypeMap(this.getEntityClass(), this.getDtoClass())
                .addMappings(m -> m.skip(UserDto::setId))
                .addMappings(m -> m.skip(UserDto::setPassword))
                .addMappings(m -> m.skip(UserDto::setRolesDto))
                .setPostConverter(toDtoConverter());
        this.getModelMapper().createTypeMap(this.getDtoClass(), this.getEntityClass())
                .addMappings(m -> m.skip(User::setId))
                .addMappings(m -> m.skip(User::setPassword))
                .addMappings(m -> m.skip(User::setRoles))
                .setPostConverter(toEntityConverter());
    }
}
