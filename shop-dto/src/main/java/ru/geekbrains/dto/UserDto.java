package ru.geekbrains.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * Simple object for transfer business object User to UI
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends AbstractPersistentDto{

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

}
