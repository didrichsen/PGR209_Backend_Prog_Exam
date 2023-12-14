package com.example.backendexam2023.Order;

import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Model.Order.OrderRequest;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Repository.CustomerRepository;
import com.example.backendexam2023.Repository.OrderLineRepository;
import com.example.backendexam2023.Repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.Mac;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderEndToEndTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    OrderLineRepository orderLineRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void should_create_order() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Machine machine = new Machine();
        machine.setPrice(1000);

        OrderLine orderLine = new OrderLine();
        orderLine.setOrderLineId(500L);
        orderLine.setMachine(machine);
        orderLineRepository.save(orderLine);


        OrderRequest orderRequest = new OrderRequest();
        orderRequest.getOrderLineIds().add(orderLine.getOrderLineId());
        orderRequest.setCustomerId(1L);

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.orderDate").exists())
                .andExpect(jsonPath("$.totalPrice").exists())
                .andExpect(jsonPath("$.orderLines").isNotEmpty())
                .andExpect(jsonPath("$.customer").isNotEmpty());
    }

    @Test
    void should_delete_order() throws Exception {
        mockMvc.perform(delete("/api/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order and Order Lines deleted"))
                .andExpect(jsonPath("$.deletedOrder").isNotEmpty());
    }

    @Test
    void should_not_delete_order_because_order_is_not_found() throws Exception {
        mockMvc.perform(delete("/api/order/100000"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Couldn't find order with id 100000"));
    }

    @Test
    void should_get_order_with_id() throws Exception{
        mockMvc.perform(get("/api/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderLines").isNotEmpty());

    }

    @Test
    void should_update_order_with_new_customer() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Customer customer = new Customer();
        customer.getAddresses().add(new Address("TestAddress", 1234));
        customerRepository.save(customer);


        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderId(500L);
        order.setOrderLines(List.of(new OrderLine()));


        Order savedOrder = orderRepository.save(order);


        Customer newCust = new Customer("Updated User", "updated@test.no");
        Customer newCustomer = customerRepository.save(newCust);

        OrderRequest updatedOrder = new OrderRequest();
        updatedOrder.setCustomerId(newCustomer.getCustomerId());

        mockMvc.perform(put("/api/order/update/" + savedOrder.getOrderId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedOrder)))
                .andExpect(status().isOk());


        Order retrievedOrder = orderRepository.findById(savedOrder.getOrderId()).orElse(null);
        assertNotNull(retrievedOrder);
        assertEquals(newCustomer.getCustomerName(), retrievedOrder.getCustomer().getCustomerName());

    }






}
