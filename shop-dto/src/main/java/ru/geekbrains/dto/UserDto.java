package ru.geekbrains.dto;

import lombok.*;
import ru.geekbrains.persist.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Simple object for transfer business object User to UI
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String id;

    @NotBlank
    private String firstname;

    private String lastname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private String confirmPassword;

    private Set<RoleDto> rolesDto;

    public UserDto(String id, String firstname, String lastname, String email, Set<RoleDto> rolesDto) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.rolesDto = rolesDto;
    }
    
    public User toUser() {
    	User user = new User();
    	if (id != null && !id.isBlank()) {
			user.setId(UUID.fromString(id));
		}
    	user.setFirstname(firstname);
    	user.setLastname(lastname);
    	user.setEmail(email);
    	user.setPassword(password);
    	user.setRoles(rolesDto.stream().map(RoleDto::toRole).collect(Collectors.toSet()));
    	return user;
    }
    
    public static UserDto fromUser(User user) {
    	UserDto userDto = new UserDto();
    	userDto.setId(user.getId().toString());
    	userDto.setFirstname(user.getFirstname());
    	userDto.setLastname(user.getLastname());
    	userDto.setEmail(user.getEmail());
    	userDto.setPassword(user.getPassword());
    	userDto.setRolesDto(user.getRoles().stream().map(RoleDto::fromRole).collect(Collectors.toSet()));
    	return userDto;
    }
}
