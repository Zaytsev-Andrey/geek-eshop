package ru.geekbrains.dto;

import lombok.*;

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

}
