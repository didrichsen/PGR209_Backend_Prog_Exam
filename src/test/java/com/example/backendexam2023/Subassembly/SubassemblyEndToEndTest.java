package com.example.backendexam2023.Subassembly;


import com.example.backendexam2023.Model.Subassembly.SubassemblyRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SubassemblyEndToEndTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void should_create_subassembly() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        SubassemblyRequest subassemblyRequest = new SubassemblyRequest();
        subassemblyRequest.setSubassemblyName("sub");
        subassemblyRequest.setPartIds(List.of(1L));

        mockMvc.perform(post("/api/subassembly")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subassemblyRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.subassemblyId").exists())
                .andExpect(jsonPath("$.subassemblyName").value(subassemblyRequest.getSubassemblyName()))
                .andExpect(jsonPath("$.parts").isNotEmpty());
    }

}
