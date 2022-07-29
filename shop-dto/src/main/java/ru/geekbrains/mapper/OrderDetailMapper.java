package ru.geekbrains.mapper;

import org.modelmapper.ModelMapper;
import ru.geekbrains.dto.OrderDetailDto;
import ru.geekbrains.dto.ProductDto;
import ru.geekbrains.persist.OrderDetail;
import ru.geekbrains.persist.Product;

import javax.annotation.PostConstruct;
import java.util.UUID;

public class OrderDetailMapper<E extends OrderDetail, D extends OrderDetailDto> extends AbstractMapper<E, D> {

    private final Mapper<Product, ProductDto> productMapper;

    public OrderDetailMapper(ModelMapper modelMapper,
                             Class<E> entityClass,
                             Class<D> dtoClass,
                             Mapper<Product, ProductDto> productMapper) {
        super(modelMapper, entityClass, dtoClass);
        this.productMapper = productMapper;
    }

    @Override
    public void mapEntitySpecificFields(E source, D destination) {
        destination.setId(source.getId().toString());
        destination.setProductDto(productMapper.toDto(source.getProduct()));
    }

    @Override
    public void mapDtoSpecificFields(D source, E destination) {
        destination.setId(UUID.fromString(source.getId()));
        destination.setProduct(productMapper.toEntity(source.getProductDto()));
    }

    @PostConstruct
    public void initMapper() {
        this.getModelMapper().createTypeMap(this.getEntityClass(), this.getDtoClass())
                .addMappings(m -> m.skip(OrderDetailDto::setId))
                .addMappings(m -> m.skip(OrderDetailDto::setProductDto))
                .setPostConverter(toDtoConverter());
        this.getModelMapper().createTypeMap(this.getDtoClass(), this.getEntityClass())
                .addMappings(m -> m.skip(OrderDetail::setId))
                .addMappings(m -> m.skip(OrderDetail::setProduct))
                .setPostConverter(toEntityConverter());
    }

}
