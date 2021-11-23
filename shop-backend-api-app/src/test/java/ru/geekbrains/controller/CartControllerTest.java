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
import ru.geekbrains.controller.dto.AddLineItemDto;
import ru.geekbrains.persist.model.*;
import ru.geekbrains.persist.repository.*;

import java.math.BigDecimal;
import java.util.List;

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

    private Product expectedProduct;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        userRepository.save(new User(2L,
                "User",
                "User",
                "user@mail.ru",
                "$2a$12$EHV5GGgrnVF8WTvdDR06kukY9/9UoH5qDWNI0y4B3eZvRggu0BhZm",
                List.of(
                        roleRepository.save(new Role(1L, "ROLE_USER"))
                )));


        expectedProduct = productRepository.save(new Product(
                1L,
                "LG 27UP850",
                new BigDecimal(500),
                "4k Monitor",
                categoryRepository.save(new Category(1L, "Monitor")),
                brandRepository.save(new Brand(1L, "LG"))
        ));
    }

    @Test
    public void testAddToCart() throws Exception {
        AddLineItemDto addLineItemDto = new AddLineItemDto(
                1L, "", "", true, true, 2
        );

        // Test addToCart
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cart")
                        .content(mapJson(addLineItemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("user@mail.ru", "user"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productDto.title", is(expectedProduct.getTitle())))
                .andExpect(jsonPath("$[0].giftWrap", is(addLineItemDto.isGiftWrap())))
                .andExpect(jsonPath("$[0].qty", is(addLineItemDto.getQty())));

        // Test findAll
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cart/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("user@mail.ru", "user"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lineItems", hasSize(0)));
    }


//    @Test
//    public void testAddToCart() throws Exception {
//        AddLineItemDto addLineItemDto = new AddLineItemDto(
//                1L, "", "", true, true, 2
//        );
//
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/cart")
//                .content(mapJson(addLineItemDto))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isForbidden());
//    }

    private static String mapJson(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }
}
