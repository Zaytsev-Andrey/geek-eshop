package ru.geekbrains.controller.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * Simple object for transfer business object Category to UI
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long id;

    @NotBlank
    private String title;

}
