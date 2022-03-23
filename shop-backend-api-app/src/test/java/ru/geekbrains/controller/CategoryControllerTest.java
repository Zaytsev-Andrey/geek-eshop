package ru.geekbrains.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import ru.geekbrains.persist.Category;
import ru.geekbrains.repository.CategoryRepository;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockBean
    private SimpMessagingTemplate webSocketTemplate;

//    @Test
//    @org.junit.jupiter.api.Order(1)
//    public void testFindAllEmpty() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/category/all")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isEmpty());
//    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void testFindAll() throws Exception {
        Category categoryMonitor = categoryRepository.save(new Category(1L, "Monitor"));
        Category categoryLaptop = categoryRepository.save(new Category(2L, "Laptop"));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/category/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath(
                        "$[*].title", hasItems(categoryMonitor.getTitle(), categoryLaptop.getTitle())));
    }

}
