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
    void shouldCreateOrder(){

        //Setup
        Customer customer = new Customer("cust", "cust@c.com");
        customer.setCustomerId(1L);

        Order order = new Order(LocalDateTime.now());
        order.setCustomer(customer);

        OrderLine orderLine = new OrderLine();
        orderLine.setMachine(new Machine("machine", 100));
        orderLine.setOrderLineId(1L);

        OrderLine orderLine1 = new OrderLine();
        orderLine1.setMachine(new Machine("machine1", 100));
        orderLine1.setOrderLineId(2L);

        List<OrderLine> orderLinesToTest = new ArrayList<>();
        orderLinesToTest.add(orderLine);
        orderLinesToTest.add(orderLine1);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(1L);
        orderRequest.setOrderLineIds(Arrays.asList(1L, 2L));

        order.setOrderLines(orderLinesToTest);
        order.setTotalPrice(200);

        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));
        when(orderLineRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(orderLine), Optional.of(orderLine1));
        when(orderRepository.save(any(Order.class))).thenReturn(order);


        OperationResult operationResult = orderService.createOrder(orderRequest);

        assertTrue(operationResult.success());
        assertNull(operationResult.errorMessage());
        assertNotNull(operationResult.createdObject());
        assertTrue(operationResult.createdObject() instanceof Order);


        Order createdOrder = (Order) operationResult.createdObject();
        System.out.println(createdOrder.getOrderLines());
        System.out.println(createdOrder.getCustomer());
        System.out.println(createdOrder.getTotalPrice());

        assertEquals(customer.getCustomerName(), createdOrder.getCustomer().getCustomerName());
        assertEquals(orderLinesToTest.size(), createdOrder.getOrderLines().size());
        assertEquals(orderRequest.getOrderLineIds(), createdOrder.getOrderLines().stream()
                .map(orderLineFromCreatedOrder -> orderLineFromCreatedOrder.getOrderLineId())
                .collect(Collectors.toList()));
        assertEquals(200, createdOrder.getTotalPrice());

    }

    @Test
    void Should_Through_Error_Because_Of_Empty_OrderLine_In_OrderRequest_Array(){

        //Setup
        Customer customer = new Customer("cust", "cust@c.com");
        customer.setCustomerId(1L);

        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(1L);

        OperationResult operationResult = orderService.createOrder(orderRequest);

        assertFalse(operationResult.success());
        assert (operationResult.errorMessage().equals("At least one order line has to be added."));

    }

/*

    @Test
    void shouldCreateOrder2(){

        //Setup
        Customer customer = new Customer("cust", "cust@c.com");
        Machine machine = new Machine("Mock Machine", 10000);
        OrderLine orderLineMock = new OrderLine();
        orderLineMock.setMachine(machine);
        Order order = new Order(LocalDateTime.now());

        order.getOrderLines().add(orderLineMock);
        order.setCustomer(customer);
        order.setTotalPrice(10000);

        //Mock
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderLine orderLine2 = new OrderLine();
        orderLine2.setMachine(machine);

        List<OrderLine> orderLines = new ArrayList<>();
        orderLines.add(orderLineMock);
        orderLines.add(orderLine2);

        Order createdOrder = orderService.createOrder(orderLines,customer);

        assert createdOrder.getOrderLines().size() == order.getOrderLines().size();
        assert createdOrder.getTotalPrice() == order.getTotalPrice();

    }

     */


}
