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
import ru.geekbrains.controller.dto.AddLineItemDto;
import ru.geekbrains.persist.model.Brand;
import ru.geekbrains.persist.model.Category;
import ru.geekbrains.persist.model.Product;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SimpMessagingTemplate webSocketTemplate;

    @Test
    public void testAddToCart() throws Exception {
        AddLineItemDto addLineItemDto = new AddLineItemDto(
                1L, "", "", true, true, 2
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post("/cart")
                .content(mapJson(addLineItemDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private static String mapJson(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }
}
