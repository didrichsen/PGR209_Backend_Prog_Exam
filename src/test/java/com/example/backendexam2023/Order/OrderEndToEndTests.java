package com.example.backendexam2023.Order;

import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Model.Order.OrderRequest;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Repository.OrderLineRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.Mac;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderEndToEndTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    OrderLineRepository orderLineRepository;

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
                .andExpect(jsonPath("$.deletedObjects").isNotEmpty());
    }


}
