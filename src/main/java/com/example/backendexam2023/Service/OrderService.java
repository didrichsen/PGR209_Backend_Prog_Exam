package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Records.DeletedOrder;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Order.OrderRequest;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Repository.CustomerRepository;
import com.example.backendexam2023.Repository.OrderLineRepository;
import com.example.backendexam2023.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {


    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final CustomerRepository customerRepository;


    @Autowired
    public OrderService(OrderRepository orderBatchRepository, OrderLineRepository orderLineRepository, CustomerRepository customerRepository){
        this.orderRepository = orderBatchRepository;
        this.orderLineRepository = orderLineRepository;
        this.customerRepository = customerRepository;
    }

    public Order getOrderById(Long id){
        return orderRepository.findById(id).orElse(null);
    };

    public List<Order> getOrdersPageable(int pageNumber) {
        return orderRepository.findAll(PageRequest.of(pageNumber, 5)).stream().toList();
    }

    public OperationResult<Object> createOrder(OrderRequest orderRequest) {
        Customer customer = customerRepository.findById(orderRequest.getCustomerId()).orElse(null);

        if (customer == null) {
            return new OperationResult<>(false, "Customer not found", null);
        }

        List<OrderLine> orderLines = new ArrayList<>();

        List<Long> orderLineIds = orderRequest.getOrderLineIds();

        if(orderLineIds.isEmpty()){
            return new OperationResult<>(false,"At least one order line has to be added.", null);
        }

        for (Long orderLineId : orderLineIds) {
            OrderLine orderLine = orderLineRepository.findById(orderLineId).orElse(null);
            if (orderLine == null) {
                return new OperationResult<>(false, "OrderLine not found", null);
            }
            orderLines.add(orderLine);
        }

        Order order = new Order(LocalDateTime.now());
        Integer totalPrice = 0;

        for (OrderLine orderLine : orderLines) {
            totalPrice += orderLine.getMachine().getPrice();
        }

        order.getOrderLines().addAll(orderLines);
        order.setTotalPrice(totalPrice);
        order.setCustomer(customer);

        Order createdOrder = orderRepository.save(order);

        return new OperationResult<>(true, null, createdOrder);
    }

    public OperationResult<Object> updateOrder(Long orderId, Order orderData){

        Order orderToUpdate = getOrderById(orderId);

        if (orderToUpdate == null) {
            return new OperationResult<>(false,"Couldn't find any order with id " + orderId, null);
        }

        if (orderData.getOrderLines() != null) orderToUpdate.getOrderLines().addAll(orderData.getOrderLines());
        if (orderData.getCustomer() != null) orderToUpdate.setCustomer(orderData.getCustomer());

        return new OperationResult<>(true, null,orderRepository.save(orderToUpdate));

    }

    public DeletedOrder deleteOrderById(Long orderId){

        Order order = getOrderById(orderId);


        if(order == null){
            return new DeletedOrder(false, Collections.emptyList(), null, "Couldn't find order with id " + orderId);
        }

        List<OrderLine> orderLinesToDelete = order.getOrderLines();

        orderRepository.deleteById(orderId);

        for (OrderLine orderline: orderLinesToDelete) {
            orderRepository.deleteById(orderline.getOrderLineId());
        }


        List<Object> customerAndOrderLines = List.of(order.getCustomer(),order.getOrderLines());

        return new DeletedOrder(true,customerAndOrderLines,"Order and Order Lines deleted",null);


    }





}
