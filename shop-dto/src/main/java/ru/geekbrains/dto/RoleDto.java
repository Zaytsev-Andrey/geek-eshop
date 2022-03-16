package ru.geekbrains.dto;

import lombok.*;
import ru.geekbrains.persist.model.Role;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

/**
 * Simple object for transfer business object Role to UI
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private String id;

    @NotBlank
    private String role;
    
    public Role toRole() {
    	Role role = new Role();
    	if (id != null && !id.isBlank()) {
			role.setId(UUID.fromString(id));
		}
    	role.setRole(this.role);
    	return role;
    }
    
    public static RoleDto fromRole(Role role) {
    	RoleDto roleDto = new RoleDto();
    	roleDto.setId(role.getId().toString());
    	roleDto.setRole(role.getRole());
    	return roleDto;
    }
}
