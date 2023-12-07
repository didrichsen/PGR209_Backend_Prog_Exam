package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Model.Order.OrderRequest;
import com.example.backendexam2023.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {


    private final OrderRepository orderRepository;
    private final OrderLineService orderService;


    @Autowired
    public OrderService(OrderRepository orderBatchRepository, OrderLineService orderService){
        this.orderRepository = orderBatchRepository;
        this.orderService = orderService;
    }

    public Order getOrderById(Long id){
        return orderRepository.findById(id).orElse(null);
    };

    public List<Order> getOrdersPageable(int pageNumber) {
        return orderRepository.findAll(PageRequest.of(pageNumber, 5)).stream().toList();
    }

    public Order createOrder(List<OrderLine> orderLines, Customer customer) {
        Order order = new Order(LocalDateTime.now());
        Integer totalPrice = 0;

        for(int i = 0; i < orderLines.size(); i++){
            totalPrice += orderLines.get(i).getMachine().getPrice();
        }

        order.getOrderLines().addAll(orderLines);
        order.setTotalPrice(totalPrice);
        order.setCustomer(customer);

        return orderRepository.save(order);
    }




}
