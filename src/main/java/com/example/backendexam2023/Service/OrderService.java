package com.example.backendexam2023.Service;

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
import java.util.List;

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





}
