package ru.geekbrains.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.geekbrains.controller.dto.CategoryDto;
import ru.geekbrains.persist.model.Category;
import ru.geekbrains.persist.repository.CategoryRepository;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockBean
    private SimpMessagingTemplate webSocketTemplate;

    @Test
    public void testFindAll() throws Exception {
        Category categoryMonitor = categoryRepository.save(new Category(null, "Monitor"));
        Category categoryLaptop = categoryRepository.save(new Category(null, "Laptop"));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/category/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath(
                        "$[*].title", hasItems(categoryMonitor.getTitle(), categoryLaptop.getTitle())));
    }

    @Test
    public void testFindAllEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/category/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

}
