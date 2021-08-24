package ru.geekbrains.controller;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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

    private List<Long> pictures;

    private MultipartFile[] newPictures;

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
