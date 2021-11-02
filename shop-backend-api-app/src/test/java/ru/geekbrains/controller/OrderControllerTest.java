package ru.geekbrains.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.geekbrains.persist.model.*;
import ru.geekbrains.persist.repository.*;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
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
    private SimpMessagingTemplate webSocketTemplate;

    @Test
    public void testGetOrderDetails() throws Exception {
        Order order = getOrder();

        mockMvc.perform(MockMvcRequestBuilders
                .get("/order/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$", containsStringIgnoringCase("access is denied")));
    }

    private Order getOrder() {
        Category category = categoryRepository.save(new Category(null, "Monitor"));
        Brand brand = brandRepository.save(new Brand(null, "LG"));
        Product product = productRepository.save(new Product(
                null,
                "LG 27UP850",
                new BigDecimal(500),
                "4k Monitor",
                category,
                brand
        ));

        Role role = roleRepository.save(new Role(null, "USER"));
        User user = userRepository.save(
                new User(null, "First", "Last", "user@mail.ru", "", List.of(role))
        );

        OrderDetail orderDetail = new OrderDetail(product, 1, new BigDecimal(500), true);
        Order order = new Order(new BigDecimal(500), OrderStatus.CREATED, user);
        order.addDetail(orderDetail);

        return orderRepository.save(order);
    }
}
