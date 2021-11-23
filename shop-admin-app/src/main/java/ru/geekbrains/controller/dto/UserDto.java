package ru.geekbrains.controller.dto;

import lombok.*;
import ru.geekbrains.controller.dto.RoleDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Simple object for transfer business object User to UI
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank
    private String firstname;

    private String lastname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private String confirmPassword;

    private List<RoleDto> rolesDto;

    public UserDto(Long id, String firstname, String lastname, String email, List<RoleDto> rolesDto) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.rolesDto = rolesDto;
    }
}
