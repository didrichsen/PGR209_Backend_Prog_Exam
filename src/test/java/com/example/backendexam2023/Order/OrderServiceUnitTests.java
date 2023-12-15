package com.example.backendexam2023.Order;

import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Model.Order.OrderRequest;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Records.DeleteResultObject;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Repository.CustomerRepository;
import com.example.backendexam2023.Repository.OrderLineRepository;
import com.example.backendexam2023.Repository.OrderRepository;
import com.example.backendexam2023.Service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
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
        orderRequest.setCustomerId(1L);

        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        OperationResult operationResult = orderService.createOrder(orderRequest);

        assertFalse(operationResult.success());
        assertEquals(operationResult.errorMessage(), "Customer not found");
        assertNull(operationResult.createdObject());


    }

    @Test
    void Should_Not_Create_Order_Because_OrderRequest_Has_Empty_OrderLine_Array(){

        //Setup
        Customer customer = new Customer("cust", "cust@c.com");
        customer.setCustomerId(1L);
        customer.setAddresses(List.of(new Address()));

        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(1L);

        OperationResult operationResult = orderService.createOrder(orderRequest);

        assert (!operationResult.success());
        assertEquals(operationResult.errorMessage(), "At least one order line has to be added.");
        assertNull(operationResult.createdObject());

    }

    @Test
    void Should_Not_Create_Order_Because_OrderLine_Was_Null_AKA_Invalid_ID(){

        Customer customer = new Customer();
        customer.getAddresses().add(new Address());
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));
        when(orderLineRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(1L);
        List<Long> orderLineIds = List.of(1L,2L,3L);
        orderRequest.getOrderLineIds().addAll(orderLineIds);

        OperationResult operationResult = orderService.createOrder(orderRequest);

        System.out.println(operationResult.errorMessage());

        assertFalse(operationResult.success());
        assertEquals(operationResult.errorMessage(),"OrderLine not found");
        assertNull(operationResult.createdObject());

    }


    @Test
    void shouldCreateOrder() {

        Customer customer = new Customer();
        customer.getAddresses().add(new Address());
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        List<OrderLine> mockOrderLines = List.of(new OrderLine(new Machine("TestMachine2", 1000)));
        when(orderLineRepository.findById(any(Long.class))).thenReturn(Optional.of(mockOrderLines.get(0)));

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(1L);
        List<Long> orderLineIds = List.of(1L, 2L, 3L);
        orderRequest.getOrderLineIds().addAll(orderLineIds);

        Order mockOrder = new Order(mockOrderLines, new Customer());
        mockOrder.setTotalPrice(1000);
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);


        OperationResult<Object> operationResult = orderService.createOrder(orderRequest);
        Order order = (Order) operationResult.createdObject();

        assertTrue(operationResult.success());
        assertNull(operationResult.errorMessage());
        assertNotNull(operationResult.createdObject());
        assertEquals(1000, order.getTotalPrice());
        assertEquals(1, order.getOrderLines().size());
    }

    @Test
    void update_order_should_because_fail_because_of_invalid_order_id(){

        Order order = new Order();
        order.setOrderId(1L);

        OrderRequest orderRequest = new OrderRequest();


        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        OperationResult operationResult = orderService.updateOrder(1L, orderRequest);

        assertFalse(operationResult.success());
        assertEquals("Couldn't find any order with id " + order.getOrderId(), operationResult.errorMessage());
        assertNull(operationResult.createdObject());
    }


    @Test
    void should_update_old_order() {

        Order existingOrder = new Order();
        existingOrder.setOrderId(1L);

        Customer newCustomer = new Customer();
        newCustomer.setEmail("update@update.no");
        newCustomer.setCustomerName("Updated Customer");

        OrderRequest newOrderInfo = new OrderRequest();
        newOrderInfo.setOrderLineIds(List.of(2L, 3L, 4L));
        newOrderInfo.setCustomerId(1L);
        newOrderInfo.setCustomerId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderLineRepository.findById(2L)).thenReturn(Optional.of(new OrderLine()));
        when(orderLineRepository.findById(3L)).thenReturn(Optional.of(new OrderLine()));
        when(orderLineRepository.findById(4L)).thenReturn(Optional.of(new OrderLine()));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(newCustomer));



        OperationResult operationResult = orderService.updateOrder(1L, newOrderInfo);
        Order updatedOrder = (Order) operationResult.createdObject();


        assertTrue(operationResult.success());
        assertNotNull(updatedOrder);
        assertEquals(3, updatedOrder.getOrderLines().size());
        assertEquals("Updated Customer", updatedOrder.getCustomer().getCustomerName());
        assertEquals("update@update.no", updatedOrder.getCustomer().getEmail());

    }

    @Test
    public void should_not_delete_order_not_found() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        DeleteResultObject result = orderService.deleteOrderById(orderId);

        assertFalse(result.success());
        assertEquals("Couldn't find order with id " + orderId, result.errorMessage());
        assertTrue(result.objects().isEmpty());
    }

    @Test
    public void should_delete_order_by_id() {

        Long orderId = 1L;
        Long orderLineId = 2l;

        Order order = new Order();
        order.setOrderId(orderId);
        order.setCustomer(new Customer("Test Customer", "test@test.no"));

        OrderLine orderLine = new OrderLine();
        orderLine.setOrderLineId(orderLineId);

        order.getOrderLines().add(orderLine);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        doNothing().when(orderRepository).deleteById(orderId);
        doNothing().when(orderLineRepository).deleteById(orderLineId);

        DeleteResultObject result = orderService.deleteOrderById(orderId);

        assertTrue(result.success());
        assertEquals("Order and Order Lines deleted", result.message());
        assertFalse(result.objects().isEmpty());

    }







}
