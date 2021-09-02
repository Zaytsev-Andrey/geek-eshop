package ru.geekbrains.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.controller.dto.RoleDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
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
