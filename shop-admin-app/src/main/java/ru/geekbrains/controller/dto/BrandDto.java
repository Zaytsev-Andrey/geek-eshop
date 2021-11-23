package ru.geekbrains.controller.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * Simple object for transfer business object Brand to UI
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandDto {

    private Long id;

    @NotBlank
    private String title;
}
