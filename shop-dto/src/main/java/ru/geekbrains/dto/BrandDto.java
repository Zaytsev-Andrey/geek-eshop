package ru.geekbrains.dto;

import lombok.*;
import ru.geekbrains.persist.model.Brand;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Simple object for transfer business object Brand to UI
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandDto {

    private String id;

    @NotBlank
    private String title;
    
    public Brand toBrand() {
    	Brand brand = new Brand();
    	if (id != null && !id.isBlank()) {
			brand.setId(UUID.fromString(id));
		}
    	brand.setTitle(title);
    	return brand;
    }
    
    public static BrandDto fromBrand(Brand brand) {
    	return new BrandDto(brand.getId().toString(), brand.getTitle());
    }
    
}
