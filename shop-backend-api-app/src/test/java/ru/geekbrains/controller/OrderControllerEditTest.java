package ru.geekbrains.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.geekbrains.controller.dto.BrandDto;
import ru.geekbrains.controller.dto.CategoryDto;
import ru.geekbrains.controller.dto.OrderDetailDto;
import ru.geekbrains.controller.dto.ProductDto;
import ru.geekbrains.persist.model.*;
import ru.geekbrains.persist.repository.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderControllerEditTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private SimpMessagingTemplate webSocketTemplate;

    private Order expectedOrder;
    private Product expectedProduct;
    private Category expectedCategory;
    private Brand expectedBrand;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        expectedCategory = categoryRepository.save(new Category(1L, "Monitor"));
        expectedBrand = brandRepository.save(new Brand(1L, "LG"));
        expectedProduct = productRepository.save(new Product(
                1L,
                "LG 27UP850",
                new BigDecimal(500),
                "4k Monitor",
                expectedCategory,
                expectedBrand
        ));

        Role roleAdmin = roleRepository.save(new Role(1L, "ROLE_ADMIN"));
        User admin = userRepository.save(
                new User(1L, "Admin", "Admin", "admin@mail.ru", "", List.of(roleAdmin))
        );

        Role role = roleRepository.save(new Role(2L, "ROLE_USER"));
        User user = userRepository.save(
                new User(2L, "User", "User", "user@mail.ru", "", List.of(role))
        );

        OrderDetail orderDetailUser = new OrderDetail(1L, expectedProduct, 1, new BigDecimal(500), true, null);
        Order orderUser = new Order(1L, null, new BigDecimal(500),
                OrderStatus.CREATED, user, new ArrayList<>());
        orderUser.addDetail(orderDetailUser);

        expectedOrder = orderRepository.save(orderUser);

        OrderDetail orderDetailAdmin = new OrderDetail(2L, expectedProduct, 3, new BigDecimal(1500), true, null);
        Order orderAdmin = new Order(2L, null, new BigDecimal(1500),
                OrderStatus.CREATED, admin, new ArrayList<>());
        orderAdmin.addDetail(orderDetailAdmin);
        orderRepository.save(orderAdmin);
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    @WithMockUser(value = "user@mail.ru", password = "user", roles = {"USER"})
    public void testEditOrderDetail() throws Exception {
        OrderDetail orderDetail = expectedOrder.getOrderDetails().get(0);
        ProductDto productDto = createProductDto();

        OrderDetailDto orderDetailDto = new OrderDetailDto(
                orderDetail.getId(),
                productDto,
                2,
                new BigDecimal(1100),
                false
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writeObjectAsJson(orderDetailDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].qty", is(orderDetailDto.getQty())))
                .andExpect(jsonPath("$[0].cost").value(orderDetailDto.getCost()))
                .andExpect(jsonPath("$[0].giftWrap", is(orderDetailDto.getGiftWrap())));
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @WithMockUser(value = "user@mail.ru", password = "user", roles = {"USER"})
    public void testEditOrderDetailNotFound() throws Exception {
        ProductDto productDto = createProductDto();

        OrderDetailDto orderDetailDto = new OrderDetailDto(
                10L,
                productDto,
                2,
                new BigDecimal(1100),
                false
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writeObjectAsJson(orderDetailDto))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$",
                        containsStringIgnoringCase("Order detail with id='10' not found")));
    }

    @Test
    @org.junit.jupiter.api.Order(10)
    @WithMockUser(value = "user@mail.ru", password = "user", roles = {"USER"})
    public void testRemoveOrderDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/order/detail/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    @org.junit.jupiter.api.Order(11)
    @WithMockUser(value = "user@mail.ru", password = "user", roles = {"USER"})
    public void testRemoveOrderDetailNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/order/detail/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$",
                        containsStringIgnoringCase("Order detail with id='10' not found")));
    }

    @Test
    @org.junit.jupiter.api.Order(60)
    @WithMockUser(value = "user@mail.ru", password = "user", roles = {"USER"})
    public void testRemoveOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }

    private static String writeObjectAsJson(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }

    private ProductDto createProductDto() {
        return new ProductDto(
                expectedProduct.getId(),
                expectedProduct.getTitle(),
                new BigDecimal(550),
                expectedProduct.getDescription(),
                new CategoryDto(expectedCategory.getId(), expectedCategory.getTitle()),
                new BrandDto(expectedBrand.getId(), expectedBrand.getTitle()),
                new ArrayList<>()
        );
    }
}
