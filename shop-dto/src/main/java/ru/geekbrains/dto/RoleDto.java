package ru.geekbrains.dto;

import lombok.*;
import ru.geekbrains.persist.Role;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

/**
 * Simple object for transfer business object Role to UI
 */
@Getter
@Setter
@NoArgsConstructor
public class RoleDto extends AbstractPersistentDto {

	public RoleDto(String id, String role) {
		super(id);
		this.role = role;
	}

	@NotBlank
    private String role;
    
    public Role toRole() {
    	Role role = new Role();
    	if (this.getId() != null && !this.getId().isBlank()) {
			role.setId(UUID.fromString(this.getId()));
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
