package com.example.backendexam2023.Address;

import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Repository.AddressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressEndToEndTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    void shouldCreateAddressWhenCallingAddressApi() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Address address = new Address("testAddress", 0666);


        mockMvc.perform(post("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.errorMessage").doesNotExist())
                .andExpect(jsonPath("$.addressId").exists())
                .andExpect(jsonPath("$.streetAddress").value("testAddress"))
                .andExpect(jsonPath("$.zipCode").value(0666));

    }

    @Test
    void shouldGetAddressById() throws Exception {

        Address address = new Address("testAddress", 1234);
        address.setAddressId(666L);
        addressRepository.save(address);
        Long addressId = address.getAddressId();

        mockMvc.perform(get("/api/address/" + addressId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.addressId").value(address.getAddressId()))
                .andExpect(jsonPath("$.streetAddress").value(address.getStreetAddress()))
                .andExpect(jsonPath("$.zipCode").value(address.getZipCode()));

    }

    @Test
    void shouldReturn_StatusNotFound_WithInvalidAddressId() throws Exception {
        Long nonExistentAddressId = 0L;

        mockMvc.perform(get("/api/address/" + nonExistentAddressId))
                .andExpect(status().isNotFound());
    }















}
