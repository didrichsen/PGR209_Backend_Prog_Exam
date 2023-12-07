package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order-batch")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);

        if (order == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(order, HttpStatus.OK);

    }

    @GetMapping("/page/{pageNumber}")
    public List<Order> getOrdersByPage(@PathVariable int pageNumber) {
        return orderService.getOrdersPageable(pageNumber);
    }
}
