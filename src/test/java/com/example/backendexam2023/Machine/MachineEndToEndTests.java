package com.example.backendexam2023.Machine;

import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Machine.MachineRequest;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Repository.CustomerRepository;
import com.example.backendexam2023.Repository.MachineRepository;
import com.example.backendexam2023.Repository.SubassemblyRepository;
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
public class MachineEndToEndTests {

    @Autowired
    MockMvc mockMvc;


    @Autowired
    private SubassemblyRepository subassemblyRepository;


    @Test
    void should_create_machine() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Subassembly subassembly = new Subassembly("Handle");
        subassembly.setSubassemblyId(500L);
        subassemblyRepository.save(subassembly);

        MachineRequest machineRequest = new MachineRequest();
        machineRequest.setMachineName("Drill");
        machineRequest.setPrice(100);
        machineRequest.getSubassemblyIds().add(subassembly.getSubassemblyId());
        
        mockMvc.perform(post("/api/machine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(machineRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.machineId").exists())
                .andExpect(jsonPath("$.machineName").value("Drill"))
                .andExpect(jsonPath("$.price").value(100));
    }
}
