package ru.geekbrains.mapper;

import org.modelmapper.ModelMapper;
import ru.geekbrains.dto.CartItemDto;
import ru.geekbrains.persist.CartItem;

import javax.annotation.PostConstruct;
import java.util.UUID;

public class SessionEntityMapper<E extends CartItem, D extends CartItemDto>
        extends AbstractMapper<E, D> {

    public SessionEntityMapper(ModelMapper modelMapper, Class<E> entityClass, Class<D> dtoClass) {
        super(modelMapper, entityClass, dtoClass);
    }

    @Override
    public void mapEntitySpecificFields(E source, D destination) {
        destination.setChangeGiftWrap(source.getGiftWrap());
    }

    @Override
    public void mapDtoSpecificFields(D source, E destination) {
        destination.setId(UUID.fromString(source.getId()));
    }

    @PostConstruct
    public void initMapper() {
        this.getModelMapper().createTypeMap(this.getEntityClass(), this.getDtoClass())
                .setPostConverter(toDtoConverter());
        this.getModelMapper().createTypeMap(this.getDtoClass(), this.getEntityClass())
                .addMappings(m -> m.skip(CartItem::setId))
                .setPostConverter(toEntityConverter());
    }
}
