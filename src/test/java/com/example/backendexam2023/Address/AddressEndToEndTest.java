package com.example.backendexam2023.Address;

import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Repository.AddressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        Address address = new Address("testAddress", 1);


        mockMvc.perform(post("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.addressId").exists())
                .andExpect(jsonPath("$.streetAddress").value("testAddress"))
                .andExpect(jsonPath("$.zipCode").value(1));
    }

    @Test
    void Should_Not_Create_Address_Because_ZipCode_Is_Blank() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Address address = new Address();
        address.setStreetAddress("testAddress");

        mockMvc.perform(post("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Address has to have a valid zip code\"}"));
    }

    @Test
    void Should_Not_Create_Address_Because_Address_Exist() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Address address = new Address("testAddress", 0666);

        Address addressFromDB = addressRepository.save(address);


        mockMvc.perform(post("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Address already exists with id " + addressFromDB.getAddressId() + "\"}"));

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

    @Test
    void should_delete_address() throws Exception {
        Address address = new Address("testAddress", 1234);
        Address addressFromDB = addressRepository.save(address);
        Long addressId = addressFromDB.getAddressId();

        mockMvc.perform(delete("/api/address/" + addressId))
                .andExpect(status().isNoContent());
    }


    @Test
    void should_fail_to_delete_address_when_address_has_cutomers() throws Exception {
        Address addressFromDb = addressRepository.findById(1L).orElse(null);

        mockMvc.perform(delete("/api/address/" + addressFromDb.getAddressId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Address has active customers."));

    }
    @Test
    void should_fail_to_delete_address_that_is_null() throws Exception {
        mockMvc.perform(delete("/api/address/" + 0L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Couldn't find address with id " + 0L))
                .andExpect(jsonPath("$.related_ids").isEmpty());

    }
















}
