package ru.geekbrains.dto;

import lombok.*;
import ru.geekbrains.persist.Category;

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
public class CategoryDto extends AbstractPersistentDto {

    @NotBlank
    private String title;

    public CategoryDto(String id, String title) {
        super(id);
        this.title = title;
    }

    public Category toCategory() {
    	Category category = new Category();
    	if (this.getId() != null && !this.getId().isBlank()) {
			category.setId(UUID.fromString(this.getId()));
		}
    	category.setTitle(title);
    	return category;
    }
    
    public static CategoryDto fromCategory(Category category) {
    	return new CategoryDto(category.getId().toString(), category.getTitle());
    }

}
