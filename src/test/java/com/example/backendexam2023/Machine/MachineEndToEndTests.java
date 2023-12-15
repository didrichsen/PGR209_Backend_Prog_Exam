package com.example.backendexam2023.Machine;

import com.example.backendexam2023.Model.Machine.MachineRequest;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Repository.MachineRepository;
import com.example.backendexam2023.Repository.SubassemblyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MachineEndToEndTests {

    @Autowired
    MockMvc mockMvc;


    @Autowired
    private SubassemblyRepository subassemblyRepository;

    @Autowired
    private MachineRepository machineRepository;


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
    @Test
    void should_not_delete_machine_by_id_if_machine_is_null() throws Exception {

        mockMvc.perform(delete("/api/machine/" + 0L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Couldn't find machine with id " + 0L));
    }
}
