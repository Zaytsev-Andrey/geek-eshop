package ru.geekbrains.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    @NotBlank
    private String title;

    @Positive
    private BigDecimal cost;

    private String description;

    @NotNull
    private CategoryDto categoryDto;

    @NotNull
    private BrandDto brandDto;
}
