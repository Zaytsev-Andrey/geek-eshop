package ru.geekbrains.dto;

import lombok.*;
import ru.geekbrains.persist.model.Category;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Simple object for transfer business object Category to UI
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private String id;

    @NotBlank
    private String title;

    public Category toCategory() {
    	Category category = new Category();
    	if (id != null && !id.isBlank()) {
			category.setId(UUID.fromString(id));
		}
    	category.setTitle(title);
    	return category;
    }
    
    public static CategoryDto fromCategory(Category category) {
    	return new CategoryDto(category.getId().toString(), category.getTitle());
    }

}
