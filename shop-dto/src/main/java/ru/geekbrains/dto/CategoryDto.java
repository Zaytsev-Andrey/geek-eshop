package ru.geekbrains.dto;

import lombok.*;

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

}
