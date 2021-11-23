package ru.geekbrains.controller.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * Simple object for transfer business object Role to UI
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private Long id;

    @NotBlank
    private String role;
}
