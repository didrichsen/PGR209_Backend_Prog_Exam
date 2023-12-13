package com.example.backendexam2023.Customer;

import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Repository.AddressRepository;
import com.example.backendexam2023.Repository.CustomerRepository;
import com.example.backendexam2023.Service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerEndToEndTests {

    @Autowired
    MockMvc mockMvc;


    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressRepository addressRepository;

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
    @Test
    void should_delete_address_from_customer() throws Exception {

        Customer customer = customerRepository.save(new Customer("cust", "cust@a.com"));

        Address address = addressRepository.save(new Address("road", 1234));

        customer.setAddresses(List.of(address));
        customerRepository.save(customer);

        mockMvc.perform(delete("/api/customer/" + customer.getCustomerId() + "/remove/" + address.getAddressId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addresses").isEmpty());

    }
    @Test
    void should_delete_customer_by_id() throws Exception {

        Customer c = new Customer("abc", "abc@d.ef");
        Customer customer = customerRepository.save(c);

        mockMvc.perform(delete("/api/customer/" + customer.getCustomerId()))
                .andExpect(status().isNoContent());

    }
    @Test
    void should_fail_when_deleting_nonExistant_customer() throws Exception {

        mockMvc.perform(delete("/api/customer/" + 0L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Couldn't find customer with id " + 0L))
                .andExpect(jsonPath("$.related_ids").isEmpty());
    }

    @Test
    void should_update_customer() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Customer customer = new Customer("prev", "prev@p.no");
        Customer prevCust = customerRepository.save(customer);

        Customer newCust = new Customer("new", "new@n.no");

        mockMvc.perform(put("/api/customer/update/" + prevCust.getCustomerId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCust)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").exists())
                .andExpect(jsonPath("$.customerName").value("new"))
                .andExpect(jsonPath("$.email").value("new@n.no"));

    }
}

