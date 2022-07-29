package ru.geekbrains.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.geekbrains.dto.BrandDto;

@Component
public class StringToBrandDtoConverter implements Converter<String, BrandDto> {

    @Override
    public BrandDto convert(String s) {
        BrandDto brandDto = null;

        if (!s.isBlank()) {
            String[] arr = s.split(";");
            brandDto = new BrandDto(arr[0], arr[1]);
        }

        return brandDto;
    }
}
