package ru.geekbrains.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.geekbrains.controller.dto.OrderDetailDto;
import ru.geekbrains.controller.dto.ProductDto;
import ru.geekbrains.persist.model.*;
import ru.geekbrains.persist.repository.OrderDetailRepository;
import ru.geekbrains.persist.repository.OrderRepository;
import ru.geekbrains.persist.repository.UserRepository;
import ru.geekbrains.service.dto.LineItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
public class OrderServiceTest {

    private OrderService orderService;

    private OrderRepository orderRepository;

    private UserRepository userRepository;

    private OrderDetailRepository orderDetailRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @BeforeEach
    public void init() {
        orderRepository = mock(OrderRepository.class);
        userRepository = mock(UserRepository.class);
        orderDetailRepository = mock(OrderDetailRepository.class);
        orderService = new OrderServiceImpl(orderRepository,
                userRepository,
                orderDetailRepository,
                rabbitTemplate,
                messagingTemplate);
    }

    @Test
    public void testGetOrderDetails() {
        Order expectedOrder = getExpectedOrder();
        OrderDetail expectedOrderDetail = expectedOrder.getOrderDetails().get(0);

        when(orderRepository.findById(eq(expectedOrder.getId())))
                .thenReturn(Optional.of(expectedOrder));

        List<OrderDetailDto> dtoOrderDetails = orderService.getOrderDetails(expectedOrder.getId());

        assertNotNull(dtoOrderDetails);
        assertEquals(1, dtoOrderDetails.size());
        OrderDetailDto orderDetailDto = dtoOrderDetails.get(0);
        assertEquals(expectedOrderDetail.getProduct().getId(), orderDetailDto.getProductDto().getId());
        assertEquals(expectedOrderDetail.getCount(), orderDetailDto.getQty());
        assertEquals(expectedOrderDetail.getCost(), orderDetailDto.getCost());
        assertFalse(orderDetailDto.getGiftWrap());
    }

    @Test
    public void testEditOrderDetail() {
        Order expectedOrder = getExpectedOrder();
        OrderDetail expectedOrderDetail = expectedOrder.getOrderDetails().get(0);

        when(orderDetailRepository.findById(eq(expectedOrderDetail.getId())))
                .thenReturn(Optional.of(expectedOrderDetail));

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setCost(new BigDecimal(500));

        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.setId(1L);
        orderDetailDto.setProductDto(productDto);
        orderDetailDto.setQty(2);
        orderDetailDto.setGiftWrap(true);

        Order editedOrder = orderService.editOrderDetail(orderDetailDto);
        assertNotNull(editedOrder.getOrderDetails());
        assertEquals(1, editedOrder.getOrderDetails().size());
        OrderDetail editedOrderDetail = editedOrder.getOrderDetails().get(0);

        assertEquals(2, editedOrderDetail.getCount());
        assertTrue(editedOrderDetail.getGiftWrap());
        assertEquals(new BigDecimal(1000), editedOrder.getPrice());
    }

    @Test
    public void testRemoveOrderDetail() {
        Order expectedOrder = getExpectedOrder();
        OrderDetail expectedOrderDetail = expectedOrder.getOrderDetails().get(0);

        when(orderDetailRepository.findById(eq(expectedOrderDetail.getId())))
                .thenReturn(Optional.of(expectedOrderDetail));

        Order editedOrder = orderService.removeOrderDetail(expectedOrderDetail.getId());

        assertNotNull(editedOrder.getOrderDetails());
        assertEquals(0, editedOrder.getOrderDetails().size());
    }

    private Order getExpectedOrder() {
        Product expectedProduct = new Product();
        expectedProduct.setId(1L);
        expectedProduct.setTitle("LG 27UP850");
        expectedProduct.setCost(new BigDecimal(500));
        expectedProduct.setDescription("4k Monitor");
        expectedProduct.setCategory(new Category(1L, "Monitor"));
        expectedProduct.setBrand(new Brand(1L, "LG"));
        OrderDetail expectedOrderDetail = new OrderDetail();
        expectedOrderDetail.setId(1L);
        expectedOrderDetail.setProduct(expectedProduct);
        expectedOrderDetail.setCount(1);
        expectedOrderDetail.setCost(new BigDecimal(500));
        expectedOrderDetail.setGiftWrap(false);

        Order expectedOrder = new Order();
        expectedOrder.setId(1L);
        expectedOrder.setPrice(new BigDecimal(500));
        expectedOrder.setStatus(OrderStatus.CREATED);
        expectedOrder.setUser(new User());
        expectedOrder.addDetail(expectedOrderDetail);

        return expectedOrder;
    }
}
