package ru.geekbrains.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * Simple object for transfer business object Role to UI
 */
@Getter
@Setter
@NoArgsConstructor
public class RoleDto extends AbstractPersistentDto {

	@NotBlank
	private String role;

	public RoleDto(String id, String role) {
		super(id);
		this.role = role;
	}

}
