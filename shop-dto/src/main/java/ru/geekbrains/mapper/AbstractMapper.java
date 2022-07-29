package ru.geekbrains.mapper;

import lombok.Getter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.util.Objects;

@Getter
public abstract class AbstractMapper<E, D>
        implements Mapper<E, D> {

    private final ModelMapper modelMapper;

    private final Class<E> entityClass;

    private final Class<D> dtoClass;

    public AbstractMapper(ModelMapper modelMapper, Class<E> entityClass, Class<D> dtoClass) {
        this.modelMapper = modelMapper;
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    @Override
    public E toEntity(D dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, entityClass);
    }

    @Override
    public D toDto(E entity) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, dtoClass);
    }

    public Converter<E, D> toDtoConverter() {
        return context -> {
            E source = context.getSource();
            D destination = context.getDestination();
            mapEntitySpecificFields(source, destination);
            return context.getDestination();
        };
    }

    public Converter<D, E> toEntityConverter() {
        return context -> {
            D source = context.getSource();
            E destination = context.getDestination();
            mapDtoSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    public abstract void mapEntitySpecificFields(E source, D destination);

    public abstract void mapDtoSpecificFields(D source, E destination);

}
