package ru.geekbrains.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {

    private Long id;

    private String title;

    private BigDecimal cost;

    private String description;

    private CategoryDto categoryDto;

    private BrandDto brandDto;

    private List<Long> pictures;

    public ProductDto(Long id, String title, BigDecimal cost, String description, CategoryDto categoryDto,
                      BrandDto brandDto, List<Long> pictures) {
        this.id = id;
        this.title = title;
        this.cost = cost;
        this.description = description;
        this.categoryDto = categoryDto;
        this.brandDto = brandDto;
        this.pictures = pictures;
    }
}
