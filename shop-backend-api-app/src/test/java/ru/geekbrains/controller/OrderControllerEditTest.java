package ru.geekbrains.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
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
import ru.geekbrains.dto.BrandDto;
import ru.geekbrains.dto.CategoryDto;
import ru.geekbrains.dto.OrderDetailDto;
import ru.geekbrains.dto.ProductDto;
import ru.geekbrains.persist.*;
import ru.geekbrains.persist.Order;
import ru.geekbrains.repository.*;

import java.math.BigDecimal;
import java.util.*;

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

    private static final UUID categoryId = UUID.randomUUID();
    private static final UUID brandId = UUID.randomUUID();
    private static final UUID productId = UUID.randomUUID();
    private static final UUID adminRoleId = UUID.randomUUID();
    private static final UUID adminId = UUID.randomUUID();
    private static final UUID userRoleId = UUID.randomUUID();
    private static final UUID userId = UUID.randomUUID();
    private static final UUID userOrderDetailId = UUID.randomUUID();
    private static final UUID userOrderId = UUID.randomUUID();
    private static final UUID adminOrderDetailId = UUID.randomUUID();
    private static final UUID adminOrderId = UUID.randomUUID();

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        expectedCategory = categoryRepository.save(new Category(categoryId, "Monitor"));
        expectedBrand = brandRepository.save(new Brand(brandId, "LG"));
        expectedProduct = productRepository.save(new Product(
                productId,
                "LG 27UP850",
                new BigDecimal(500),
                "4k Monitor",
                expectedCategory,
                expectedBrand
        ));

        Role roleAdmin = roleRepository.save(new Role(adminRoleId, "ROLE_ADMIN"));
        User admin = userRepository.save(
                new User(adminId, "Admin", "Admin", "admin@mail.ru", "", Set.of(roleAdmin))
        );

        Role role = roleRepository.save(new Role(userRoleId, "ROLE_USER"));
        User user = userRepository.save(
                new User(userId, "User", "User", "user@mail.ru", "", Set.of(role))
        );

        OrderDetail orderDetailUser = new OrderDetail(userOrderDetailId, expectedProduct, 1, new BigDecimal(500), true, null);
        Order orderUser = new Order(userOrderId, null, new BigDecimal(500),
                OrderStatus.CREATED, user, new HashSet<>());
        orderUser.addDetail(orderDetailUser);

        expectedOrder = orderRepository.save(orderUser);

        OrderDetail orderDetailAdmin = new OrderDetail(adminOrderDetailId, expectedProduct, 3, new BigDecimal(1500), true, null);
        Order orderAdmin = new Order(adminOrderId, null, new BigDecimal(1500),
                OrderStatus.CREATED, admin, new HashSet<>());
        orderAdmin.addDetail(orderDetailAdmin);
        orderRepository.save(orderAdmin);
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    @WithMockUser(value = "user@mail.ru", password = "user")
    public void testEditOrderDetail() throws Exception {
        OrderDetail orderDetail = expectedOrder.getOrderDetails().iterator().next();
        ProductDto productDto = createProductDto();

        OrderDetailDto orderDetailDto = new OrderDetailDto(
                orderDetail.getId().toString(),
                productDto,
                2,
                "1100",
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
    @WithMockUser(value = "user@mail.ru", password = "user")
    public void testEditOrderDetailNotFound() throws Exception {
        ProductDto productDto = createProductDto();
        String notFoundOrderDetailId = UUID.randomUUID().toString();

        OrderDetailDto orderDetailDto = new OrderDetailDto(
                notFoundOrderDetailId,
                productDto,
                2,
                "1100",
                false
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writeObjectAsJson(orderDetailDto))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$",
                        containsStringIgnoringCase("Order detail with id='" + notFoundOrderDetailId + "' not found")));
    }

    @Test
    @org.junit.jupiter.api.Order(10)
    @WithMockUser(value = "user@mail.ru", password = "user")
    public void testRemoveOrderDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/order/detail/" + userOrderDetailId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    @org.junit.jupiter.api.Order(11)
    @WithMockUser(value = "user@mail.ru", password = "user")
    public void testRemoveOrderDetailNotFound() throws Exception {
        String notFoundOrderDetailId = UUID.randomUUID().toString();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/order/detail/" + notFoundOrderDetailId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$",
                        containsStringIgnoringCase("Order detail with id='" + notFoundOrderDetailId + "' not found")));
    }

    @Test
    @org.junit.jupiter.api.Order(60)
    @WithMockUser(value = "user@mail.ru", password = "user")
    public void testRemoveOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/order/" + userOrderId)
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
                expectedProduct.getId().toString(),
                expectedProduct.getTitle(),
                "550",
                expectedProduct.getDescription(),
                new CategoryDto(expectedCategory.getId().toString(), expectedCategory.getTitle()),
                new BrandDto(expectedBrand.getId().toString(), expectedBrand.getTitle()),
                new HashSet<>()
        );
    }
}
