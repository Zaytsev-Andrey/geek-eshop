package ru.geekbrains.service;

import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import ru.geekbrains.dto.*;
import ru.geekbrains.mapper.SessionEntityMapper;
import ru.geekbrains.persist.CartItem;
import ru.geekbrains.persist.Product;
import ru.geekbrains.repository.CartRepository;
import ru.geekbrains.repository.CartRepositoryImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartServiceTest {

    private static CartService cartService;

    private static final UUID productId = UUID.randomUUID();

    @BeforeAll
    public static void init() {
        SessionEntityMapper<CartItem, CartItemDto> cartItemMapper =
                new SessionEntityMapper<>(new ModelMapper(), CartItem.class, CartItemDto.class);
        cartItemMapper.initMapper();

        CartRepository cartRepository = new CartRepositoryImpl(new ArrayList<>());

        ProductService productService = mock(ProductServiceImpl.class);
        when(productService.findProductByIdIn(Set.of(productId)))
                .thenReturn(List.of(new Product(
                        productId,
                        "LG 27UP850",
                        new BigDecimal(500),
                        "",
                        null,
                        null
                )));

        cartService = new CartServiceImpl(cartRepository, productService, cartItemMapper);
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    public void ifNewCartIsEmptyTest() {
        assertNotNull(cartService.getCartItems());
        assertEquals(0, cartService.getCartItems().size());
        assertEquals("0", cartService.getCartDto().getSubtotal());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void addProductTest() {
        CartItemDto expectedCartItemDto = new CartItemDto(
                productId.toString(),
                "LG 27UP850",
                true,
                true,
                "500.00",
                "1000.00",
                2
        );

        cartService.addToCart(expectedCartItemDto);
        cartService.addToCart(expectedCartItemDto);

        AllCartDto allCartDto = cartService.getCartDto();
        assertNotNull(allCartDto.getLineItems());
        assertEquals(1, allCartDto.getLineItems().size());
        assertEquals("2000", allCartDto.getSubtotal());

        CartItemDto cartItemDto = allCartDto.getLineItems().get(0);
        assertEquals("LG 27UP850", cartItemDto.getProductTitle());
        assertEquals("500", cartItemDto.getCost());
        assertTrue(cartItemDto.getGiftWrap());
        assertEquals(4, cartItemDto.getQty());
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void updateCartTest() {
        CartItemDto expectedCartItemDto = new CartItemDto(
                productId.toString(),
                "LG 27UP850",
                true,
                true,
                "500.00",
                "1000.00",
                2
        );

        cartService.updateCart(expectedCartItemDto);

        AllCartDto allCartDto = cartService.getCartDto();
        assertNotNull(allCartDto.getLineItems());
        assertEquals(1, allCartDto.getLineItems().size());
        assertEquals("1000", allCartDto.getSubtotal());

        CartItemDto cartItemDto = allCartDto.getLineItems().get(0);
        assertEquals("LG 27UP850", cartItemDto.getProductTitle());
        assertEquals("500", cartItemDto.getCost());
        assertTrue(cartItemDto.getGiftWrap());
        assertEquals(2, cartItemDto.getQty());
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    public void deleteItemTest() {
        CartItemDto expectedCartItemDto = new CartItemDto(
                productId.toString(),
                "LG 27UP850",
                true,
                true,
                "500.00",
                "1000.00",
                2
        );

        cartService.deleteItem(expectedCartItemDto);

        AllCartDto allCartDto = cartService.getCartDto();
        assertNotNull(allCartDto.getLineItems());
        assertEquals(0, allCartDto.getLineItems().size());
        assertEquals("0", allCartDto.getSubtotal());
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    public void clearTest() {
        CartItemDto expectedCartItemDto = new CartItemDto(
                productId.toString(),
                "LG 27UP850",
                true,
                true,
                "500.00",
                "1000.00",
                2
        );

        cartService.addToCart(expectedCartItemDto);

        AllCartDto allCartDto = cartService.getCartDto();
        assertNotNull(allCartDto.getLineItems());
        assertEquals(1, allCartDto.getLineItems().size());
        assertEquals("1000", allCartDto.getSubtotal());

        cartService.clear();

        allCartDto = cartService.getCartDto();
        assertNotNull(allCartDto.getLineItems());
        assertEquals(0, allCartDto.getLineItems().size());
        assertEquals("0", allCartDto.getSubtotal());
    }
}
