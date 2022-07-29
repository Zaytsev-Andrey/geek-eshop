package ru.geekbrains.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import ru.geekbrains.persist.*;
import ru.geekbrains.repository.*;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class OrderControllerTest {

    @Autowired
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

    private static final UUID categoryId = UUID.randomUUID();
    private static final UUID roleId = UUID.randomUUID();
    private static final UUID productId = UUID.randomUUID();
    private static final UUID adminRoleId = UUID.randomUUID();
    private static final UUID adminId = UUID.randomUUID();
    private static final UUID userRoleId = UUID.randomUUID();
    private static final UUID userId = UUID.randomUUID();
    private static final UUID userOrderDetailId = UUID.randomUUID();
    private static final UUID userOrderId = UUID.randomUUID();

    @BeforeEach
    public void init() {
        Category expectedCategory = categoryRepository.save(new Category(categoryId, "Monitor"));
        Brand expectedBrand = brandRepository.save(new Brand(roleId, "LG"));
        Product expectedProduct = productRepository.save(new Product(
                productId,
                "LG 27UP850",
                new BigDecimal(500),
                "4k Monitor",
                expectedCategory,
                expectedBrand
        ));

        Role roleAdmin = roleRepository.save(new Role(adminRoleId, "ROLE_ADMIN"));
        userRepository.save(
                new User(adminId, "Admin", "Admin", "admin@mail.ru", "", Set.of(roleAdmin))
        );

        Role role = roleRepository.save(new Role(userRoleId, "ROLE_USER"));
        User user = userRepository.save(
                new User(userId, "User", "User", "user@mail.ru", "", Set.of(role))
        );

        OrderDetail orderDetail = new OrderDetail(userOrderDetailId, expectedProduct, 1, new BigDecimal(500), true, null);
        Order order = new Order(userOrderId, null, new BigDecimal(500),
                OrderStatus.CREATED, user, new HashSet<>());
        order.addDetail(orderDetail);

        expectedOrder = orderRepository.save(order);
    }

    @Test
    public void testGetOrderDetailsUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/order/" + userOrderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$", containsStringIgnoringCase("access is denied")));
    }

    @Test
    @WithMockUser(value = "user@mail.ru", password = "user")
    public void testGetOrderDetails() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/order/" + userOrderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].qty", is(expectedOrder.getOrderDetails().iterator().next().getQty())))
                .andExpect(jsonPath("$[0].cost")
                        .value(expectedOrder.getOrderDetails().iterator().next().getCost().setScale(2)))
                .andExpect(jsonPath("$[0].giftWrap", is(expectedOrder.getOrderDetails().iterator().next().getGiftWrap())));
    }

    @Test
    @WithMockUser(value = "user@mail.ru", password = "user")
    public void testGetOrderDetailsNotFound() throws Exception {
        String notFoundOrderId = UUID.randomUUID().toString();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/order/" + notFoundOrderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$",
                        containsStringIgnoringCase("Order with id='" + notFoundOrderId + "' not found")));

    }

    @Test
    @WithMockUser(value = "user@mail.ru", password = "user")
    public void testGetUserOrders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/order/own")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].price").value(expectedOrder.getPrice().setScale(2)))
                .andExpect(jsonPath("$[0].status", is(expectedOrder.getStatus().toString())));
    }

    @Test
    @WithMockUser(value = "test@mail.ru", password = "test")
    public void testGetUserOrdersNotFoundUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/order/own")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$",
                        containsStringIgnoringCase("User with login='test@mail.ru' not found")));
    }

    @Test
    @WithMockUser(value = "admin@mail.ru", password = "admin", roles = {"ADMIN"})
    public void testGetEmptyUserOrders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/order/own")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }

}
