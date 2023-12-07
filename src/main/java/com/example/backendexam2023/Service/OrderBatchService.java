package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.OrderBatch.OrderBatch;
import com.example.backendexam2023.OrderBatch.OrderBatchRequest;
import com.example.backendexam2023.Repository.OrderBatchRepository;
import com.example.backendexam2023.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderBatchService {


    private final OrderBatchRepository orderBatchRepository;
    private final OrderService orderService;


    @Autowired
    public OrderBatchService(OrderBatchRepository orderBatchRepository, OrderService orderService){
        this.orderBatchRepository = orderBatchRepository;
        this.orderService = orderService;
    }

    public OrderBatch getOrderBatchById(Long id){
        return orderBatchRepository.findById(id).orElse(null);
    };

    public List<OrderBatch> getOrderBatchesPageable(int pageNumber) {
        return orderBatchRepository.findAll(PageRequest.of(pageNumber, 5)).stream().toList();
    }

    public OrderBatch createOrderBatch(OrderBatchRequest orderBatchRequest) {
        OrderBatch orderBatch = new OrderBatch(LocalDateTime.now());
        List<Order> orders = new ArrayList<>();
        Integer totalPrice = 0;

        List<Long> orderIds = orderBatchRequest.getOrderIds();
        for (Long id : orderIds) {
            Order order = orderService.getOrderById(id);
            orders.add(order);
            totalPrice += order.getMachine().getPrice();
        }

        orderBatch.getOrders().addAll(orders);
        orderBatch.setTotalPrice(totalPrice);

        return orderBatchRepository.save(orderBatch);
    }




}
