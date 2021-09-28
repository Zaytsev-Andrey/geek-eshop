package ru.geekbrains.controller.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long id;

    private String title;

}
