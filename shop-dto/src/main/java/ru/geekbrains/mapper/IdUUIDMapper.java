package ru.geekbrains.mapper;

import org.modelmapper.ModelMapper;
import ru.geekbrains.dto.AbstractPersistentDto;
import ru.geekbrains.persist.AbstractPersistentEntity;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.UUID;

public class IdUUIDMapper<E extends AbstractPersistentEntity, D extends AbstractPersistentDto>
        extends AbstractMapper<E, D> {

    public IdUUIDMapper(ModelMapper modelMapper, Class<E> entityClass, Class<D> dtoClass) {
        super(modelMapper, entityClass, dtoClass);
    }

    @Override
    public void mapEntitySpecificFields(AbstractPersistentEntity source, AbstractPersistentDto destination) {
        destination.setId(source.getId().toString());
    }

    @Override
    public void mapDtoSpecificFields(AbstractPersistentDto source, AbstractPersistentEntity destination) {
        String id = source.getId();
        if (!Objects.isNull(id) && !id.isBlank()) {
            destination.setId(UUID.fromString(id));
        }
    }

    @PostConstruct
    public void initMapper() {
        this.getModelMapper().createTypeMap(this.getEntityClass(), this.getDtoClass())
                .addMappings(m -> m.skip(AbstractPersistentDto::setId))
                .setPostConverter(toDtoConverter());
        this.getModelMapper().createTypeMap(this.getDtoClass(), this.getEntityClass())
                .addMappings(m -> m.skip(AbstractPersistentEntity::setId))
                .setPostConverter(toEntityConverter());
    }
}
