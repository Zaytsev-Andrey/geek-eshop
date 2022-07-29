package ru.geekbrains.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.geekbrains.dto.BrandDto;
import ru.geekbrains.persist.Brand;

//@Component
public class BrandMapper extends IdUUIDMapper<Brand, BrandDto> {

    @Autowired
    public BrandMapper(ModelMapper modelMapper) {
        super(modelMapper, Brand.class, BrandDto.class);
    }

}
