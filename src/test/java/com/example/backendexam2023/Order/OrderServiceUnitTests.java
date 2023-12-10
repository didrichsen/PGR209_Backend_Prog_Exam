package com.example.backendexam2023.Order;


import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Model.Order.OrderRequest;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Repository.CustomerRepository;
import com.example.backendexam2023.Repository.OrderLineRepository;
import com.example.backendexam2023.Repository.OrderRepository;
import com.example.backendexam2023.Service.OrderService;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest
public class OrderServiceUnitTests {

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private OrderLineRepository orderLineRepository;

    @Autowired
    private OrderService orderService;

    @Test
    void Should_Not_Create_Order_Because_Customer_Is_Null_AKA_Invalid_ID(){

        OrderRequest orderRequest = new OrderRequest();

        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        OperationResult operationResult = orderService.createOrder(orderRequest);

        assert !operationResult.success();
        assert operationResult.errorMessage().equals("Customer not found");
        assert operationResult.createdObject() == null;

    }

    @Test
    void Should_Not_Create_Order_Because_OrderRequest_Has_Empty_OrderLine_Array(){

        //Setup
        Customer customer = new Customer("cust", "cust@c.com");
        customer.setCustomerId(1L);

        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(1L);

        OperationResult operationResult = orderService.createOrder(orderRequest);

        assert (!operationResult.success());
        assert (operationResult.errorMessage().equals("At least one order line has to be added."));
        assert (operationResult.createdObject() == null);

    }

    @Test
    void Should_Not_Create_Order_Because_OrderLine_Was_Null_AKA_Invalid_ID(){

        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(new Customer()));
        when(orderLineRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(1L);
        List<Long> orderLineIds = List.of(1L,2L,3L);
        orderRequest.getOrderLineIds().addAll(orderLineIds);

        OperationResult operationResult = orderService.createOrder(orderRequest);

        System.out.println(operationResult.errorMessage());

        assertFalse(operationResult.success());
        assert operationResult.errorMessage().equals("OrderLine not found");
        assert operationResult.createdObject() == null;

    }


    @Test
    void shouldCreateOrder(){

        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(new Customer()));
        when(orderLineRepository.findById(any(Long.class))).thenReturn(Optional.of(new OrderLine(new Machine("TestMachine",1000))));

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(1L);
        List<Long> orderLineIds = List.of(1L,2L,3L);
        orderRequest.getOrderLineIds().addAll(orderLineIds);


        when(orderRepository.save(any(Order.class))).thenReturn(new Order(List.of(new OrderLine()), new Customer()));

        OperationResult operationResult = orderService.createOrder(orderRequest);

        assertTrue(operationResult.success());
        assert operationResult.errorMessage() == null;
        assertTrue(operationResult.createdObject() instanceof Order);
    }

}
