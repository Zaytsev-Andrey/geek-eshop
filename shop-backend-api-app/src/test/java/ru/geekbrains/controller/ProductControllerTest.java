package ru.geekbrains.controller;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.geekbrains.persist.Brand;
import ru.geekbrains.persist.Category;
import ru.geekbrains.persist.Product;
import ru.geekbrains.repository.BrandRepository;
import ru.geekbrains.repository.CategoryRepository;
import ru.geekbrains.repository.ProductRepository;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private SimpMessagingTemplate webSocketTemplate;

    private Category expectedCategory;
    private Brand expectedBrand;

    @Test
    @org.junit.jupiter.api.Order(1)
    public void testProductDetailsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/product/all")
                        .param("sortField", "id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void testProductDetails() throws Exception {
        Product product = saveTestProduct();

        mockMvc.perform(MockMvcRequestBuilders
                .get("/product/all")
                .param("sortField", "id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", is(product.getTitle())))
                .andExpect(jsonPath("$.content[0].cost").value(product.getCost().setScale(1)))
                .andExpect(jsonPath("$.content[0].description", is(product.getDescription())))
                .andExpect(jsonPath("$.content[0].categoryDto.title", is(expectedCategory.getTitle())))
                .andExpect(jsonPath("$.content[0].brandDto.title", is(expectedBrand.getTitle())));
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void testFindById() throws Exception {
        Product product = saveTestProduct();

        mockMvc.perform(MockMvcRequestBuilders
                .get("/product/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(product.getTitle())))
                .andExpect(jsonPath("$.cost").value(product.getCost().setScale(1)))
                .andExpect(jsonPath("$.description", is(product.getDescription())))
                .andExpect(jsonPath("$.categoryDto.title", is(expectedCategory.getTitle())))
                .andExpect(jsonPath("$.brandDto.title", is(expectedBrand.getTitle())));
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    public void testFindByIdException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/product/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", containsString("not found")));
    }

    private Product saveTestProduct() {
        expectedCategory = categoryRepository.save(new Category(1L, "Monitor"));
        expectedBrand = brandRepository.save(new Brand(1L, "LG"));
        return productRepository.save(new Product(
                1L,
                "LG 27UP850",
                new BigDecimal(500),
                "4k Monitor",
                expectedCategory,
                expectedBrand
        ));
    }
}
