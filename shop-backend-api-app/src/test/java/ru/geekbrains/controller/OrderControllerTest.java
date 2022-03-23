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
import ru.geekbrains.persist.repository.*;
import ru.geekbrains.repository.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        userRepository.save(
                new User(1L, "Admin", "Admin", "admin@mail.ru", "", List.of(roleAdmin))
        );

        Role role = roleRepository.save(new Role(2L, "ROLE_USER"));
        User user = userRepository.save(
                new User(2L, "User", "User", "user@mail.ru", "", List.of(role))
        );

        OrderDetail orderDetail = new OrderDetail(1L, expectedProduct, 1, new BigDecimal(500), true, null);
        Order order = new Order(1L, null, new BigDecimal(500),
                OrderStatus.CREATED, user, new ArrayList<>());
        order.addDetail(orderDetail);

        expectedOrder = orderRepository.save(order);
    }

    @Test
    public void testGetOrderDetailsUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/order/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$", containsStringIgnoringCase("access is denied")));
    }

    @Test
    @WithMockUser(value = "user@mail.ru", password = "user", roles = {"USER"})
    public void testGetOrderDetails() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/order/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].qty", is(expectedOrder.getOrderDetails().get(0).getCount())))
                .andExpect(jsonPath("$[0].cost")
                        .value(expectedOrder.getOrderDetails().get(0).getCost().setScale(1)))
                .andExpect(jsonPath("$[0].giftWrap", is(expectedOrder.getOrderDetails().get(0).getGiftWrap())));
    }

    @Test
    @WithMockUser(value = "user@mail.ru", password = "user", roles = {"USER"})
    public void testGetOrderDetailsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/order/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$",
                        containsStringIgnoringCase("Order with id='2' not found")));

    }

    @Test
    @WithMockUser(value = "user@mail.ru", password = "user", roles = {"USER"})
    public void testGetUserOrders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/order/own")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].price").value(expectedOrder.getPrice().setScale(1)))
                .andExpect(jsonPath("$[0].status", is(expectedOrder.getStatus().getName())));
    }

    @Test
    @WithMockUser(value = "test@mail.ru", password = "test", roles = {"USER"})
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
