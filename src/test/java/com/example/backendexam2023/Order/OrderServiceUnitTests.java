package com.example.backendexam2023.Order;


import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
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
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest
public class OrderServiceUnitTests {

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Test
    void shouldCreateOrder(){

        Customer customer = new Customer("cust", "cust@c.com");
        customer.setCustomerId(1L);

        Order order = new Order(LocalDateTime.now());

        OrderLine orderLine = new OrderLine();
        orderLine.setMachine(new Machine("machine", 100));
        orderLine.setOrder(order);

        OrderLine orderLine1 = new OrderLine();
        orderLine1.setMachine(new Machine("machine1", 100));
        orderLine1.setOrder(order);

        List<OrderLine> orderLines = new ArrayList<>();
        orderLines.add(orderLine);

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order newOrder = orderService.createOrder(orderLines, customer);

        assert newOrder.getOrderDate() == order.getOrderDate();
        assert newOrder.getTotalPrice() == 200;


    }


}
