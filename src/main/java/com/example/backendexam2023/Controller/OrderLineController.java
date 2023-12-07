package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Service.OrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/order")
public class OrderLineController {

    private final OrderLineService orderLineService;

    @Autowired
    public OrderLineController(OrderLineService orderLineService){
        this.orderLineService = orderLineService;
    }

    @DeleteMapping("/{id}")
    public void deleteOrderLine(@PathVariable Long id){
        orderLineService.deleteOrderLine(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderLine> getOrderLineById(@PathVariable Long id){
        OrderLine orderLine = orderLineService.getOrderLineById(id);

        if (orderLine == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(orderLine, HttpStatus.OK);
    }
    @GetMapping("/page/{pageNumber}")
    public List<OrderLine> getOrderLinesByPage(@PathVariable int pageNumber) {
        return orderLineService.getOrderLinesPageable(pageNumber);
    }



/*
    @PostMapping("")
    public Order createOrder(@RequestBody OrderRequest orderRequest){
        return orderService.createOrder(orderRequest);
    }

 */






}
