package ru.geekbrains.controller.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@Getter
@Setter
@NoArgsConstructor
public class ProductDto {

    private Long id;

    private String title;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
    @JsonSubTypes({@JsonSubTypes.Type(name = "BIG_DECIMAL", value = BigDecimal.class)})
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

    public ProductDto(Long id, String title, BigDecimal cost, String description) {
        this.id = id;
        this.title = title;
        this.cost = cost;
        this.description = description;
    }
}
