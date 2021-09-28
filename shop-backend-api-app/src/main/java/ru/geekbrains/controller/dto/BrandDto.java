package ru.geekbrains.controller.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandDto {

    private Long id;

    private String title;
}
