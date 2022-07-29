package ru.geekbrains.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.geekbrains.dto.*;
import ru.geekbrains.mapper.IdUUIDMapper;
import ru.geekbrains.mapper.OrderDetailMapper;
import ru.geekbrains.mapper.ProductMapper;
import ru.geekbrains.persist.*;
import ru.geekbrains.repository.OrderDetailRepository;
import ru.geekbrains.repository.OrderRepository;
import ru.geekbrains.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
public class OrderServiceTest {

    private OrderService orderService;

    private OrderRepository orderRepository;

    private OrderDetailRepository orderDetailRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @BeforeEach
    public void init() {
        orderRepository = mock(OrderRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        orderDetailRepository = mock(OrderDetailRepository.class);
        UserService userService = mock(UserService.class);
        CartService cartService = mock(CartService.class);

        ModelMapper modelMapper = new ModelMapper();

        IdUUIDMapper<Category, CategoryDto> categoryMapper =
                new IdUUIDMapper<>(modelMapper, Category.class, CategoryDto.class);
        categoryMapper.initMapper();
        IdUUIDMapper<Brand, BrandDto> brandMapper = new IdUUIDMapper<>(modelMapper, Brand.class, BrandDto.class);
        brandMapper.initMapper();
        ProductMapper<Product, ProductDto> productMapper =
                new ProductMapper<>(modelMapper, Product.class, ProductDto.class, brandMapper, categoryMapper);
        productMapper.initMapper();
        IdUUIDMapper<Order, OrderDto> orderMapper = new IdUUIDMapper<>(modelMapper, Order.class, OrderDto.class);
        orderMapper.initMapper();
        OrderDetailMapper<OrderDetail, OrderDetailDto> orderDetailMapper =
                new OrderDetailMapper<>(modelMapper, OrderDetail.class, OrderDetailDto.class, productMapper);
        orderDetailMapper.initMapper();

        orderService = new OrderServiceImpl(orderRepository,
                userRepository,
                orderDetailRepository,
                userService,
                cartService,
                rabbitTemplate,
                messagingTemplate,
                orderMapper,
                orderDetailMapper);
    }

    @Test
    public void testGetOrderDetails() {
        Order expectedOrder = getExpectedOrder();
        OrderDetail expectedOrderDetail = expectedOrder.getOrderDetails().iterator().next();

        when(orderRepository.findById(eq(expectedOrder.getId())))
                .thenReturn(Optional.of(expectedOrder));

        List<OrderDetailDto> dtoOrderDetails = orderService.getOrderDetails(expectedOrder.getId());

        assertNotNull(dtoOrderDetails);
        assertEquals(1, dtoOrderDetails.size());
        OrderDetailDto orderDetailDto = dtoOrderDetails.get(0);
        assertEquals(expectedOrderDetail.getProduct().getId().toString(), orderDetailDto.getProductDto().getId());
        assertEquals(expectedOrderDetail.getQty(), orderDetailDto.getQty());
        assertEquals(expectedOrderDetail.getCost().toString(), orderDetailDto.getCost());
        assertFalse(orderDetailDto.getGiftWrap());
    }

    @Test
    public void testEditOrderDetail() {
        Order expectedOrder = getExpectedOrder();
        OrderDetail expectedOrderDetail = expectedOrder.getOrderDetails().iterator().next();
        expectedOrderDetail.setQty(2);

        when(orderDetailRepository.findById(eq(expectedOrderDetail.getId())))
                .thenReturn(Optional.of(expectedOrderDetail));

        when(orderRepository.findById(eq(expectedOrder.getId())))
                .thenReturn(Optional.of(expectedOrder));

        ProductDto productDto = new ProductDto();
        productDto.setId(expectedOrder.getId().toString());
        productDto.setCost("500");

        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.setId(expectedOrderDetail.getId().toString());
        orderDetailDto.setProductDto(productDto);
        orderDetailDto.setQty(2);
        orderDetailDto.setGiftWrap(true);

        List<OrderDetailDto> editedOrderDetailDtos = orderService.editOrderDetail(orderDetailDto);
        assertNotNull(editedOrderDetailDtos);
        assertEquals(1, editedOrderDetailDtos.size());
        OrderDetailDto editedOrderDetailDto = editedOrderDetailDtos.get(0);

        assertEquals(2, editedOrderDetailDto.getQty());
        assertTrue(editedOrderDetailDto.getGiftWrap());
        assertEquals(new BigDecimal(1000).toString(), editedOrderDetailDto.getCost());
    }

    @Test
    public void testRemoveOrderDetail() {
        Order expectedOrder = getExpectedOrder();
        OrderDetail expectedOrderDetail = expectedOrder.getOrderDetails().iterator().next();

        when(orderDetailRepository.findById(eq(expectedOrderDetail.getId())))
                .thenReturn(Optional.of(expectedOrderDetail));

        Order editedOrder = orderService.removeOrderDetail(expectedOrderDetail.getId());

        assertNotNull(editedOrder.getOrderDetails());
        assertEquals(0, editedOrder.getOrderDetails().size());
    }

    private Order getExpectedOrder() {
        Product expectedProduct = new Product(
                UUID.randomUUID(),
                "LG 27UP850",
                new BigDecimal(500),
                "4k Monitor",
                new Category("Monitor"),
                new Brand("LG")
        );

        OrderDetail expectedOrderDetail = new OrderDetail();
        expectedOrderDetail.setId(UUID.randomUUID());
        expectedOrderDetail.setProduct(expectedProduct);
        expectedOrderDetail.setQty(1);
        expectedOrderDetail.setCost(new BigDecimal(500));
        expectedOrderDetail.setGiftWrap(false);

        Order expectedOrder = new Order();
        expectedOrder.setId(UUID.randomUUID());
        expectedOrder.setPrice(new BigDecimal(500));
        expectedOrder.setStatus(OrderStatus.CREATED);
        expectedOrder.setUser(new User());
        expectedOrder.addDetail(expectedOrderDetail);

        return expectedOrder;
    }
}
