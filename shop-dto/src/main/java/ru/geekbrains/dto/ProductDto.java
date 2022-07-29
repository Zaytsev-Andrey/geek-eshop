package ru.geekbrains.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;
import java.util.UUID;

/**
 * Simple object for transfer business object Product to UI
 */
@Getter
@Setter
@NoArgsConstructor
public class ProductDto extends AbstractPersistentDto {

    @NotBlank
    private String title;

    @Positive
    private String cost;

    private String description;

    @NotNull
    private CategoryDto categoryDto;

    @NotNull
    private BrandDto brandDto;

    private Set<UUID> pictures;

    private MultipartFile[] newPictures;

    public ProductDto(String id, String title, String cost, String description, CategoryDto categoryDto,
                      BrandDto brandDto, Set<UUID> pictures) {
        this.setId(id);
        this.title = title;
        this.cost = cost;
        this.description = description;
        this.categoryDto = categoryDto;
        this.brandDto = brandDto;
        this.pictures = pictures;
    }
    
}
