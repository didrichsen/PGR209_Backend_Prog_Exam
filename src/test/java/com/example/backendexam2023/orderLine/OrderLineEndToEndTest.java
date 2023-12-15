package com.example.backendexam2023.orderLine;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderLineEndToEndTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void should_create_orderLine() throws Exception {

        mockMvc.perform(post("/api/order-line/" + 1L))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderLineId").exists())
                .andExpect(jsonPath("$.machine").isNotEmpty());

    }


}
