package ru.geekbrains.controller;

import org.junit.jupiter.api.Test;
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
import ru.geekbrains.persist.model.Brand;
import ru.geekbrains.persist.model.Category;
import ru.geekbrains.persist.model.Product;
import ru.geekbrains.persist.repository.BrandRepository;
import ru.geekbrains.persist.repository.CategoryRepository;
import ru.geekbrains.persist.repository.ProductRepository;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
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

    @Test
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
                .andExpect(jsonPath("$.content[0].categoryDto.title", is(product.getCategory().getTitle())))
                .andExpect(jsonPath("$.content[0].brandDto.title", is(product.getBrand().getTitle())));
    }

    @Test
    public void testProductDetailsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/product/all")
                        .param("sortField", "id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    public void testFindById() throws Exception {
        Product product = saveTestProduct();

        mockMvc.perform(MockMvcRequestBuilders
                .get("/product/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(product.getTitle())))
                .andExpect(jsonPath("$.cost").value(product.getCost().setScale(1)))
                .andExpect(jsonPath("$.description", is(product.getDescription())))
                .andExpect(jsonPath("$.categoryDto.title", is(product.getCategory().getTitle())))
                .andExpect(jsonPath("$.brandDto.title", is(product.getBrand().getTitle())));
    }

    @Test
    public void testFindByIdException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/product/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", containsString("not found")));
    }

    private Product saveTestProduct() {
        Category category = categoryRepository.save(new Category(null, "Monitor"));
        Brand brand = brandRepository.save(new Brand(null, "LG"));
        return productRepository.save(new Product(
                null,
                "LG 27UP850",
                new BigDecimal(500),
                "4k Monitor",
                category,
                brand
        ));
    }
}
