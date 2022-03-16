package ru.geekbrains.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.geekbrains.dto.CategoryDto;

@Component
public class StringToCategoryDtoConverter implements Converter<String, CategoryDto> {

    @Override
    public CategoryDto convert(String s) {
        CategoryDto categoryDto = null;

        if (!s.isBlank()) {
            String[] arr = s.split(";");
            categoryDto = new CategoryDto(arr[0], arr[1]);
        }
        return categoryDto;
    }
}
