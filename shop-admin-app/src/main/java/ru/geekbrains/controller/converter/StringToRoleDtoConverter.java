package ru.geekbrains.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.geekbrains.dto.RoleDto;

@Component
public class StringToRoleDtoConverter implements Converter<String, RoleDto> {

    @Override
    public RoleDto convert(String s) {
        String[] arr = s.split(";");
        return new RoleDto(arr[0], arr[1]);
    }
}
