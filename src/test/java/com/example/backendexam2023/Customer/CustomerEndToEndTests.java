package com.example.backendexam2023.Customer;

import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerEndToEndTests {

    @Autowired
    MockMvc mockMvc;


    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void should_create_customer() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Customer customer = new Customer("TestUser", "test@test.com");


        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").exists())
                .andExpect(jsonPath("$.customerName").value("TestUser"))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }
    @Test
    void should_add_address_to_customer() throws Exception {

        mockMvc.perform(post("/api/customer/" + 1L + "/add/" + 1L))
                .andExpect(status().isOk());

    }
    @Test
    void should_fail_to_add_address_to_customer() throws Exception {

        mockMvc.perform(post("/api/customer/" + 0L + "/add/" + 0L))
                .andExpect(status().isBadRequest());

    }
    @Test
    void should_get_customer_by_id() throws Exception {

        mockMvc.perform(get("/api/customer/" + 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1L));

    }
}

