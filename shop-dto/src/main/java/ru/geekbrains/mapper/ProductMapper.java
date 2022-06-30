package ru.geekbrains.mapper;

import org.modelmapper.ModelMapper;
import ru.geekbrains.dto.BrandDto;
import ru.geekbrains.dto.CategoryDto;
import ru.geekbrains.dto.ProductDto;
import ru.geekbrains.persist.Brand;
import ru.geekbrains.persist.Category;
import ru.geekbrains.persist.Picture;
import ru.geekbrains.persist.Product;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductMapper<E extends Product, D extends ProductDto> extends AbstractMapper<E, D> {

    private final Mapper<Brand, BrandDto> brandMapper;

    private final Mapper<Category, CategoryDto> categoryMapper;

    public ProductMapper(ModelMapper modelMapper,
                         Class<E> entityClass,
                         Class<D> dtoClass,
                         Mapper<Brand, BrandDto> brandMapper,
                         Mapper<Category, CategoryDto> categoryMapper) {
        super(modelMapper, entityClass, dtoClass);
        this.brandMapper = brandMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public void mapEntitySpecificFields(Product source, ProductDto destination) {
        destination.setId(source.getId().toString());
        destination.setBrandDto(brandMapper.toDto(source.getBrand()));
        destination.setCategoryDto(categoryMapper.toDto(source.getCategory()));
        destination.setPictures(source.getPictures().stream()
                .map(Picture::getId)
                .collect(Collectors.toSet()));
    }

    @Override
    public void mapDtoSpecificFields(ProductDto source, Product destination) {
        String id = source.getId();
        if (!Objects.isNull(id) && !id.isBlank()) {
            destination.setId(UUID.fromString(id));
        }
        destination.setBrand(brandMapper.toEntity(source.getBrandDto()));
        destination.setCategory(categoryMapper.toEntity(source.getCategoryDto()));
    }

    @PostConstruct
    public void initMapper() {
        this.getModelMapper().createTypeMap(this.getEntityClass(), this.getDtoClass())
                .addMappings(m -> m.skip(ProductDto::setId))
                .addMappings(m -> m.skip(ProductDto::setBrandDto))
                .addMappings(m -> m.skip(ProductDto::setCategoryDto))
                .addMappings(m -> m.skip(ProductDto::setPictures))
                .setPostConverter(toDtoConverter());
        this.getModelMapper().createTypeMap(this.getDtoClass(), this.getEntityClass())
                .addMappings(m -> m.skip(Product::setId))
                .addMappings(m -> m.skip(Product::setBrand))
                .addMappings(m -> m.skip(Product::setCategory))
                .setPostConverter(toEntityConverter());
    }
}
