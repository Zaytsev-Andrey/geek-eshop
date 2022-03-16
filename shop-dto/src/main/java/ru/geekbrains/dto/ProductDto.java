package ru.geekbrains.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.persist.model.Picture;
import ru.geekbrains.persist.model.Product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Simple object for transfer business object Product to UI
 */
@Getter
@Setter
@NoArgsConstructor
public class ProductDto {

    private String id;

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
        this.id = id;
        this.title = title;
        this.cost = cost;
        this.description = description;
        this.categoryDto = categoryDto;
        this.brandDto = brandDto;
        this.pictures = pictures;
    }
    
    public Product toProduct() {
    	Product product = new Product();
    	if (id != null && !id.isBlank()) {
			product.setId(UUID.fromString(id));
		}
    	product.setTitle(title);
    	product.setCost(new BigDecimal(cost));
    	product.setDescription(description);
    	product.setCategory(categoryDto.toCategory());
    	product.setBrand(brandDto.toBrand());
    	return product;
    }
    
    public static ProductDto fromProduct(Product product) {
    	ProductDto productDto = new ProductDto();
    	productDto.setId(product.getId().toString());
    	productDto.setTitle(product.getTitle());
    	productDto.setCost(product.getCost().toString());
    	productDto.setDescription(product.getDescription());
    	productDto.setCategoryDto(CategoryDto.fromCategory(product.getCategory()));
    	productDto.setBrandDto(BrandDto.fromBrand(product.getBrand()));
    	productDto.setPictures(product.getPictures().stream()
    			.map(Picture::getId)
    			.collect(Collectors.toSet()));
    	return productDto;
    }
}
