package com.example.backendexam2023.Part;

import com.example.backendexam2023.Model.Part.Part;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PartEndToEndTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void should_create_part() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Part part = new Part("fork");

        mockMvc.perform(post("/api/part")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(part)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.partId").exists())
                .andExpect(jsonPath("$.partName").value(part.getPartName()));


    }

}
