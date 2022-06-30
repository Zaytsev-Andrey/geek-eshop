package ru.geekbrains.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.geekbrains.dto.*;
import ru.geekbrains.persist.*;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return modelMapper;
    }

    @Bean
    public Mapper<Brand, BrandDto> brandMapper() {
        return new IdUUIDMapper<>(modelMapper(), Brand.class, BrandDto.class);
    }

    @Bean
    public Mapper<Category, CategoryDto> categoryMapper() {
        return new IdUUIDMapper<>(modelMapper(), Category.class, CategoryDto.class);
    }

    @Bean
    public Mapper<Product, ProductDto> productMapper() {
        return new ProductMapper<>(modelMapper(), Product.class, ProductDto.class, brandMapper(), categoryMapper());
    }

    @Bean
    public Mapper<Role, RoleDto> roleMapper() {
        return new IdUUIDMapper<>(modelMapper(), Role.class, RoleDto.class);
    }

    @Bean
    public Mapper<User, UserDto> userMapper() {
        return new UserMapper<>(modelMapper(), User.class, UserDto.class, roleMapper());
    }

    @Bean
    public Mapper<CartItem, CartItemDto> cartItemMapper() {
        return new SessionEntityMapper<>(modelMapper(), CartItem.class, CartItemDto.class);
    }

    @Bean
    public Mapper<OrderDetail, OrderDetailDto> orderDetailMapper() {
        return new OrderDetailMapper<>(modelMapper(), OrderDetail.class, OrderDetailDto.class, productMapper());
    }

    @Bean
    public Mapper<Order, OrderDto> orderMapper() {
        return new IdUUIDMapper<>(modelMapper(), Order.class, OrderDto.class);
    }
}
