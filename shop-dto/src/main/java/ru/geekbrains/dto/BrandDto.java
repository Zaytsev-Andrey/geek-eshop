package ru.geekbrains.dto;

import lombok.*;
import ru.geekbrains.persist.Brand;

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
public class BrandDto extends AbstractPersistentDto {

    @NotBlank
    private String title;

    public BrandDto(String id, String title) {
        super(id);
        this.title = title;
    }

    public Brand toBrand() {
    	Brand brand = new Brand();
    	if (this.getId() != null && !this.getId().isBlank()) {
			brand.setId(UUID.fromString(this.getId()));
		}
    	brand.setTitle(title);
    	return brand;
    }
    
    public static BrandDto fromBrand(Brand brand) {
    	return new BrandDto(brand.getId().toString(), brand.getTitle());
    }
    
}
