package ru.geekbrains.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.geekbrains.dto.CartItemDto;
import ru.geekbrains.persist.*;
import ru.geekbrains.repository.*;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class CartControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @MockBean
    private SimpMessagingTemplate webSocketTemplate;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        userRepository.save(new User(UUID.randomUUID(),
                "User",
                "User",
                "user@mail.ru",
                "$2a$12$EHV5GGgrnVF8WTvdDR06kukY9/9UoH5qDWNI0y4B3eZvRggu0BhZm",
                Set.of(
                        roleRepository.save(new Role(UUID.randomUUID(), "ROLE_USER"))
                )));


        productRepository.save(new Product(
                UUID.randomUUID(),
                "LG 27UP850",
                new BigDecimal(500),
                "4k Monitor",
                categoryRepository.save(new Category(UUID.randomUUID(), "Monitor")),
                brandRepository.save(new Brand(UUID.randomUUID(), "LG"))
        ));
    }

    @Test
    public void testAddToCart() throws Exception {
        CartItemDto lineItemDto = new CartItemDto(
                UUID.randomUUID().toString(), "Monitor LG", true, true, "200", "400", 2
        );

        // Test addToCart
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cart")
                        .content(mapJson(lineItemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("user@mail.ru", "user"))
                        .with(csrf()))
                .andExpect(status().isOk());

        // Test findAll
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cart/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("user@mail.ru", "user"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lineItems", hasSize(0)));
    }


    private static String mapJson(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }
}
